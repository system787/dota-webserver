package edu.orangecoastcollege.cs273.api;

import edu.orangecoastcollege.cs273.model.*;
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
    private static final String API_KEY_2 = APIKey.getAPIKey2();
    private static final String API_KEY_3 = APIKey.getAPIKey3();

    private static final int REQUEST_TIMEOUT = 3000;

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
            Logger.getLogger(TAG).log(Level.INFO, "Building new HTTP connection");
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(timeout);
            c.setReadTimeout(timeout);
            Logger.getLogger(TAG).log(Level.INFO, "Attempting to connect to URL");
            c.connect();
            int status = c.getResponseCode();
            Logger.getLogger(TAG).log(Level.INFO, "Response code received: " + status);

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
     * Gets the 64-bit SteamID for a specified user from their vanity url
     * (e.g. "http://steamcommunity.com/id/system787" with "system787" being the part of the vanity url that is used)
     *
     * @param vanity
     * @return
     */
    public long get64FromVanity(String vanity) {
        String url = "http://api.steampowered.com/ISteamUser/ResolveVanityURL/v0001/?key=" + API_KEY + "&vanityurl=" + vanity.trim();
        try {
            String json = getJSON(url, REQUEST_TIMEOUT);
            JSONObject response = new JSONObject(json).getJSONObject("response");
            if (response == null) {
                return -1;
            }
            return response.getLong("steamid");
        } catch (JSONException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "JSON exception in retrieving user's 64bit SteamId", e);
        }
        return -1;
    }

    public long get32FromVanity(String vanity) {
        return convert64to32(get64FromVanity(vanity));
    }

    /**
     * Converts a 64-bit SteamID to 32 bit format
     *
     * @param steamId64 passed in as a String to be parsed
     * @return 32-bit SteamID stored as a String
     */
    public long convert64to32(long steamId64) {
        return steamId64 - 76561197960265728L;
    }

    public long convert32to64(long steamId32) {
        return steamId32 + 76561197960265728L;
    }

    public User getUserSummary(long steamId64) {
        String url = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=" + API_KEY_3 + "&steamids=" + steamId64;
        try {
            String json = getJSON(url.toString(), REQUEST_TIMEOUT);
            JSONObject response = new JSONObject(json).getJSONObject("result");
            if (response == null) {
                return null;
            }
            JSONArray jsonArray = response.getJSONArray("players");
            JSONObject jObject = jsonArray.getJSONObject(0);
            User user = new User(convert64to32(jObject.getLong("steamid")),
                    jObject.getInt("communityvisibilitystate"),
                    jObject.getInt("profilestate"),
                    jObject.getString("personaname"),
                    jObject.getLong("lastlogoff"),
                    jObject.getString("profileurl"),
                    jObject.getString("avatarfull"));
            return user;
        } catch (JSONException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "JSON exception in retrieving user summaries", e);
        }
        return null;
    }

    public List<User> getUserSummaries(long[] steamId64) {
        StringBuilder url = new StringBuilder("http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=").append(API_KEY_3).append("&steamids=");
        if (steamId64.length == 1) {
            url.append(String.valueOf(steamId64[0]));
        } else {
            for (int i = 0; i < steamId64.length; i++) {
                url.append(String.valueOf(steamId64[i]));
                if (i + 1 < steamId64.length) {
                    url.append(",");
                }
            }
        }
        try {
            String json = getJSON(url.toString(), REQUEST_TIMEOUT);
            JSONObject response = new JSONObject(json).getJSONObject("response");

            if (response == null) {
                return null;
            }

            JSONArray jsonArray = response.getJSONArray("players");
            List<User> userList = new ArrayList<>();

            for (Object o : jsonArray) {
                JSONObject jsonLineItem = (JSONObject) o;
                User user = new User(convert64to32(jsonLineItem.getLong("steamid")),
                        jsonLineItem.getInt("communityvisibilitystate"),
                        jsonLineItem.optInt("profilestate", 0), //jsonLineItem.getInt("profilestate"),
                        jsonLineItem.getString("personaname"),
                        jsonLineItem.getLong("lastlogoff"),
                        jsonLineItem.getString("profileurl"),
                        jsonLineItem.getString("avatarfull"));
                userList.add(user);
            }
            return userList;
        } catch (JSONException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "JSON exception in retrieving user summaries", e);
        }
        return null;
    }

    public List<Hero> getAllHeroes() {
        String url = "https://api.steampowered.com/IEconDOTA2_570/GetHeroes/v0001/?language=en_US&key=" + API_KEY;

        try {
            String json = getJSON(url, REQUEST_TIMEOUT);
            JSONObject response = new JSONObject(json).getJSONObject("result");
            if (response == null) {
                return null;
            }

            JSONArray jsonArray = response.getJSONArray("heroes");
            List<Hero> heroList = new ArrayList<>();

            for (Object o : jsonArray) {
                JSONObject jsonLineItem = (JSONObject) o;
                Hero hero = new Hero(jsonLineItem.getInt("id"), jsonLineItem.getString("name"), jsonLineItem.getString("localized_name"));
                heroList.add(hero);
            }
            return heroList;
        } catch (JSONException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "JSON exception in retrieving heroes list from master server", e);
        }
        return null;
    }

    public List<MatchID> getMatches(long id32, int numRequested) {
        String requestedAmount = String.valueOf(numRequested);
        String url = "https://api.steampowered.com/IDOTA2Match_570/GetMatchHistory/V001/?account_id=" + id32 + "&matches_requested=" + requestedAmount + "&key=" + API_KEY;

        try {
            String json = getJSON(url, REQUEST_TIMEOUT);
            JSONObject response = new JSONObject(json).getJSONObject("result");
            if (response == null) {
                return null;
            }

            int statusCode = response.getInt("status");
            if (statusCode == 15) {
                return null;
            }


            JSONArray jsonArray = response.getJSONArray("matches");
            List<MatchID> matchIDList = new ArrayList<>();

            for (Object o : jsonArray) {
                JSONObject jsonLineItem = (JSONObject) o;

                List<Long> playersList = new ArrayList<>();
                JSONArray matchPlayersArray = jsonLineItem.getJSONArray("players");
                long matchId = jsonLineItem.getLong("match_id");

                for (Object k : matchPlayersArray) {
                    JSONObject matchPlayerJSON = (JSONObject) k;
                    playersList.add(((JSONObject) k).getLong("account_id"));
                }

                MatchID match = new MatchID(matchId, playersList);

                matchIDList.add(match);
            }


            return matchIDList;
        } catch (JSONException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "JSON exception in retrieving the user's last 25 matches", e);
        }
        return null;
    }

    // TODO: varargs parameter
    public List<MatchDetails> getMatchDetails(long[] matchId) {
        String url = "http://api.steampowered.com/IDOTA2Match_570/GetMatchDetails/v1/?key=" + API_KEY_3 + "&match_id=" + matchId;

        try {
            String json = getJSON(url, REQUEST_TIMEOUT);
            JSONObject response = new JSONObject(json).getJSONObject("result");
            if (response == null) {
                return null;
            }

            // TODO: figure out a different way to grab nested JSON arrays
            // this method wont work if it gets all additional units as part of one array
            JSONArray playersArray = response.getJSONArray("players");
            List<MatchDetailPlayer> playerList = new ArrayList<>();

            for (Object o : playersArray) {
                JSONArray playerUnitArray = response.getJSONObject("players").getJSONArray("additional_units");
                //MatchDetailPlayerUnit playerUnit = new MatchDetailPlayerUnit(playerUnitArray);
            }



            // TODO: make private classes for readability
            // First create player units, then player details, then match details



        } catch (JSONException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "JSON exception in retrieving match details for match " + matchId, e);
        }

        return null;
    }

}
