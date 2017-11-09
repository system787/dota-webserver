package edu.orangecoastcollege.cs273.api;

import edu.orangecoastcollege.cs273.model.Hero;
import edu.orangecoastcollege.cs273.model.MatchID;
import edu.orangecoastcollege.cs273.model.MatchPlayers;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class APIRequest {
    private static final String TAG = "APIRequest";
    private static final String API_KEY = APIKey.getAPIKey();
    private static final String API_DOMAIN = APIKey.getAPIDomain();
    private static final int REQUEST_TIMEOUT = 15000;

    public APIRequest() {

    }

    /**
     * @param url     request URL to send
     * @param timeout length of time in milliseconds until the request is canceled
     * @return a formatted JSON in String format
     * @throws JSONException logged for each exception seen below
     */
    public String getJSON(String url, int timeout) throws JSONException {
        HttpURLConnection c = null;
        try {
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(timeout);
            c.setReadTimeout(timeout);
            c.connect();
            int status = c.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    return sb.toString();
                default:
                    Logger.getLogger(TAG).log(Level.SEVERE, "Error; Response code: " + status);
                    return null;
            }
        } catch (MalformedURLException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "MalformedURLException caught", e);
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "IOException caught", e);
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception e) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Unexpected Exception thrown", e);
                }
            }
        }
        return null;
    }

    /**
     * Gets the user's display name using their 64-bit Steam ID
     *
     * @param steamId64 specified user's 64-bit Steam ID
     * @return String containing user's "personaname"/display name
     */
    public String getDisplayName(String steamId64) {
        String url = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=" + API_KEY + "&steamids=" + steamId64;
        try {
            String json = getJSON(url, REQUEST_TIMEOUT);
            JSONObject response = new JSONObject(json).getJSONObject("response");
            if (response == null) {
                return null;
            }

            return response.getString("personaname");

        } catch (JSONException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "JSON exception in retrieving user's display name");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets the 64-bit SteamID for a specified user from their vanity url
     * (e.g. "http://steamcommunity.com/id/system787" with "system787" being the part of the vanity url that is used)
     *
     * @param vanity
     * @return
     */
    public String get64FromVanity(String vanity) {
        String url = "http://api.steampowered.com/ISteamUser/ResolveVanityURL/v0001/?key=" + API_KEY + "&vanityurl=" + vanity.trim();
        try {
            String json = getJSON(url, REQUEST_TIMEOUT);
            JSONObject response = new JSONObject(json).getJSONObject("response");
            if (response == null) {
                return null;
            }
            return response.getString("steamid");
        } catch (JSONException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "JSON exception in retrieving user's 64bit SteamId", e);
        }
        return null;
    }

    /**
     * Converts a 64-bit SteamID to 32 bit format
     *
     * @param steamId64 passed in as a String to be parsed
     * @return 32-bit SteamID stored as a String
     */
    public String convert64to32(String steamId64) {
        return String.valueOf(Long.parseLong(steamId64) - 76561197960265728L);
    }

    public HashMap<Integer, Hero> getAllHeroes() {
        String url = "https://api.steampowered.com/IEconDOTA2_570/GetHeroes/v0001/?language=en_US&key=" + API_KEY;

        try {
            String json = getJSON(url, REQUEST_TIMEOUT);
            JSONObject response = new JSONObject(json).getJSONObject("result");
            if (response == null) {
                return null;
            }

            JSONArray jsonArray = response.getJSONArray("heroes");
            HashMap<Integer, Hero> heroHashMap = new HashMap<>();
            //ArrayList<Hero> heroArrayList = new ArrayList<>();

            for (Object o : jsonArray) {
                JSONObject jsonLineItem = (JSONObject) o;
                int id = jsonLineItem.getInt("id");
                Hero hero = new Hero(id, jsonLineItem.getString("name"), jsonLineItem.getString("localized_name"));
                heroHashMap.put(id, hero);
            }
            return heroHashMap;
        } catch (JSONException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "JSON exception in retrieving heroes list from master server", e);
        }
        return null;
    }

    public List<MatchID> getMatches(String id64, int numRequested) {
        String requestedAmount = String.valueOf(numRequested);
        String id32 = convert64to32(id64);
        String url = "https://api.steampowered.com/IDOTA2Match_570/GetMatchHistory/V001/?account_id=" + id32 + "&matches_requested=" + requestedAmount + "&key=" + API_KEY;

        try {
            String json = getJSON(url, REQUEST_TIMEOUT);
            JSONObject response = new JSONObject(json).getJSONObject("result");
            if (response == null) {
                return null;
            }

            JSONArray jsonArray = response.getJSONArray("matches");
            ArrayList<MatchID> matchIDArrayList = new ArrayList<>();

            for (Object o : jsonArray) {
                JSONObject jsonLineItem = (JSONObject) o;

                List<MatchPlayers> matchPlayersList = new ArrayList<>();
                JSONArray matchPlayersArray = jsonLineItem.getJSONArray("players");
                String matchId = String.valueOf(jsonLineItem.getLong("match_id"));

                for (Object k : matchPlayersArray) {
                    JSONObject matchPlayerJSON = (JSONObject) k;
                    MatchPlayers player = new MatchPlayers(matchId, matchPlayerJSON.getInt("account_id"), matchPlayerJSON.getInt("player_slot"), matchPlayerJSON.getInt("hero_id"));
                    matchPlayersList.add(player);
                }

                MatchID match = new MatchID(matchId,
                        String.valueOf(jsonLineItem.getLong("match_seq_num")),
                        String.valueOf(jsonLineItem.getLong("start_time")),
                        jsonLineItem.getInt("lobby_type"),
                        matchPlayersList);

                matchIDArrayList.add(match);
            }

            return matchIDArrayList;
        } catch (JSONException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "JSON exception in retrieving the user's last 25 matches", e);
        }
        return null;
    }

}
