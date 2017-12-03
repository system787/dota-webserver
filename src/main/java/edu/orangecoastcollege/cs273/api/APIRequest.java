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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class APIRequest {
    private static final String TAG = "APIRequest";

    private static final int REQUEST_TIMEOUT = 3000;

    private static final String API_KEY = APIKey.getAPIKey();
    private static final String API_KEY_2 = APIKey.getAPIKey2();
    private static final String API_KEY_3 = APIKey.getAPIKey3();

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

            if (status < 200 || status > 201) {
                Logger.getLogger(TAG).log(Level.INFO, "Response code received: " + status);
            }

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

            if (jsonArray.length() == 0) {
                User anonymous_user = new User(0, 1, 0, "Anonymous User", 0, "", "");
                userList.add(anonymous_user);
                return userList;
            }

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

                JSONArray matchPlayersArray = jsonLineItem.getJSONArray("players");
                long matchId = jsonLineItem.getLong("match_id");

                for (Object k : matchPlayersArray) {
                    JSONObject player = (JSONObject) k;
                    matchIDList.add(new MatchID(matchId, player.getLong("account_id")));
                }
            }


            return matchIDList;
        } catch (JSONException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "JSON exception in retrieving the user's last 25 matches", e);
        }
        return null;
    }

    public List<MatchDetails> getMatchDetails(long[] matchIdArray) {
        List<MatchDetails> matchDetailsList = new ArrayList<>();

        for (long matchId : matchIdArray) {
            String url = "http://api.steampowered.com/IDOTA2Match_570/GetMatchDetails/v1/?key=" + API_KEY_3 + "&match_id=" + matchId;
            try {
                String json = getJSON(url, REQUEST_TIMEOUT);
                JSONObject response = new JSONObject(json).getJSONObject("result");
                if (response == null) {
                    return null;
                }

                JSONArray playersArray = response.getJSONArray("players");
                List<MatchDetailPlayer> playerList = getPlayersList(playersArray, matchId);

                long matchSeqNum = response.getLong("match_seq_num");

                boolean radiantWin = response.getBoolean("radiant_win");
                long startTime = response.getLong("start_time");
                int duration = response.getInt("duration");
                int firstBloodTime = response.getInt("first_blood_time");
                int lobbyType = response.getInt("lobby_type");
                int numPlayers = response.getInt("human_players");
                int gameMode = response.getInt("game_mode");

                MatchDetails match = new MatchDetails(matchId, matchSeqNum, playerList,
                        radiantWin, startTime, duration, firstBloodTime,
                        lobbyType, numPlayers, gameMode);

                matchDetailsList.add(match);

            } catch (JSONException e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "JSON exception in retrieving match details for match " + matchId, e);
            }

        }

        return matchDetailsList;
    }

    private List<MatchDetailPlayer> getPlayersList(JSONArray playersArray, long matchId) {
        List<MatchDetailPlayer> playerList = new ArrayList<>();


        int arrayLength = playersArray.length();
        for (int i = 0; i < arrayLength; i++) {
            JSONObject playerJSON = playersArray.getJSONObject(i);

            long playerId = playerJSON.getLong("account_id");
            int heroId = playerJSON.getInt("hero_id");
            int playerSlot = playerJSON.getInt("player_slot");
            int[] itemArray = new int[]{playerJSON.getInt("item_0"), playerJSON.getInt("item_1"),
                    playerJSON.getInt("item_2"), playerJSON.getInt("item_3"),
                    playerJSON.getInt("item_4"), playerJSON.getInt("item_5")};
            int kills = playerJSON.getInt("kills");
            int deaths = playerJSON.getInt("deaths");
            int assists = playerJSON.getInt("assists");
            int leaverStatus = playerJSON.getInt("leaver_status");
            int gold = playerJSON.getInt("gold");
            int lastHits = playerJSON.getInt("last_hits");
            int denies = playerJSON.getInt("denies");
            int gpm = playerJSON.getInt("gold_per_min");
            int xpm = playerJSON.getInt("xp_per_min");
            int goldSpent = playerJSON.getInt("gold_spent");
            int heroDamage = playerJSON.getInt("hero_damage");
            int towerDamage = playerJSON.getInt("tower_damage");
            int heroHealing = playerJSON.getInt("hero_healing");
            int level = playerJSON.getInt("level");


            MatchDetailPlayer player = new MatchDetailPlayer(matchId, playerId, playerSlot, heroId, itemArray,
                    kills, deaths, assists, leaverStatus,
                    gold, lastHits, denies, gpm, xpm,
                    goldSpent, heroDamage, towerDamage, heroHealing,
                    level);


            try {
                JSONObject playerUnitObject = playerJSON.getJSONObject("additional_units");

                int[] unitItemArray = new int[]{playerUnitObject.getInt("item_0"),
                        playerUnitObject.getInt("item_1"),
                        playerUnitObject.getInt("item_2"),
                        playerUnitObject.getInt("item_3"),
                        playerUnitObject.getInt("item_4"),
                        playerUnitObject.getInt("item_5")};

                MatchDetailPlayerUnit playerUnit = new MatchDetailPlayerUnit(
                        matchId, playerId, playerUnitObject.getString("unitname"), unitItemArray);

                player.setMatchDetailPlayerUnit(playerUnit);
            } catch (JSONException e) {
                Logger.getLogger(TAG).log(Level.INFO, "Player " + playerJSON.getLong("account_id")
                        + " does not have a unit");
            }
            playerList.add(player);

        }

        return playerList;
    }


}
