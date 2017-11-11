package edu.orangecoastcollege.cs273.api;

import edu.orangecoastcollege.cs273.controller.SQLController;
import edu.orangecoastcollege.cs273.model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertTrue;

public class APIRequestTest {
    static final String TAG = "APIRequestTest";
    static SQLController mSQLController;
    static APIRequest mAPIRequest;
    static List<Hero> serverQueryList;

    @BeforeClass
    public static void databaseConnections() throws SQLException {
        mSQLController = SQLController.getInstance();
        mAPIRequest = new APIRequest();

        TimerTask heroQueryTask = new TimerTask() {
            @Override
            public void run() {
                serverQueryList = mAPIRequest.getAllHeroes();
            }
        };

        Timer timer = new Timer(true);
        timer.schedule(heroQueryTask, 1000);
        heroQueryTask.run();
        mSQLController.resetAllTables();
    }

    @Before
    public void openNewConnection() throws SQLException {
        mSQLController.openConnection();

    }

    @After
    public void closeConnection() throws SQLException {
        mSQLController.close();
    }

    @Test
    public void getAllHeroes() throws Exception {

        for (Hero h : serverQueryList) {
            h.saveToDB(mSQLController);
        }

        List<Hero> readFromDB = Hero.getAllHeroes(mSQLController);

        serverQueryList.sort((o1, o2) -> o1.getId() - o2.getId());
        readFromDB.sort((o1, o2) -> o1.getId() - o2.getId());


        for (int i = 0; i < readFromDB.size(); i++) {
            assertTrue(readFromDB.get(i).equals(serverQueryList.get(i)));
        }
    }

    private static List<User> getUserList() {
        User u1 = new User(97942759, 5, 1,
                "personaname1", 12345, "url1", "ava1");
        User u2 = new User(4294967295L, 5, 1,
                "personaname2", 12345, "url2", "ava2");
        User u3 = new User(114611, 5, 1,
                "system", 12345, "url3", "ava3");
        User u4 = new User(49973220, 5, 1,
                "personaname4", 12345, "url4", "ava4");
        User u5 = new User(101892699, 5, 1,
                "personaname5", 12345, "url5", "ava5");
        User u6 = new User(39974939, 5, 1,
                "personaname6", 12345, "url6", "ava6");

        List<User> userList = new ArrayList<>();
        userList.add(u1);
        userList.add(u2);
        userList.add(u3);
        userList.add(u4);
        userList.add(u5);
        userList.add(u6);

        return userList;
    }

    @Test
    public void checkUserDatabase() {

        List<User> seedDataList = getUserList();

        for (User u : seedDataList) {
            u.saveToDB(mSQLController);
        }

        List<User> readFromDBList = User.getAllUsers(mSQLController);

        readFromDBList.sort((o1, o2) -> Long.compare(o1.getSteamId32(), o2.getSteamId32()));
        seedDataList.sort((o1, o2) -> Long.compare(o1.getSteamId32(), o2.getSteamId32()));

        for (int i = 0; i < readFromDBList.size(); i++) {
            assertTrue(seedDataList.get(i).getPersonaName().equals(readFromDBList.get(i).getPersonaName()));
        }
    }

    @Test
    public void getAllUsers() {
        List<User> userList = User.getAllUsers(mSQLController);
        assertTrue(userList.size() == 6);
    }

    public List<MatchID> getMatchIDFromJSON() {
        String jsonString;
        List<MatchID> matchIDList = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("sample-matches.json"));
            StringBuilder sb = new StringBuilder();

            while ((jsonString = reader.readLine()) != null) {
                sb.append(jsonString).append("\n");
            }

            jsonString = sb.toString();

            reader.close();

            JSONObject json = new JSONObject(jsonString).getJSONObject("result");
            JSONArray matchesArray = json.getJSONArray("matches");


            for (Object o : matchesArray) {
                JSONObject lineItem = (JSONObject) o;
                long matchId = lineItem.getLong("match_id");
                JSONArray matchPlayerArray = lineItem.getJSONArray("players");
                List<MatchPlayer> matchPlayersList = new ArrayList<>();

                for (Object l : matchPlayerArray) {
                    JSONObject matchPlayerJSON = (JSONObject) l;
                    MatchPlayer player = new MatchPlayer(matchId, matchPlayerJSON.getInt("account_id"), matchPlayerJSON.getInt("player_slot"), matchPlayerJSON.getInt("hero_id"));
                    matchPlayersList.add(player);
                }

                MatchID match = new MatchID(matchId,
                        lineItem.getLong("match_seq_num"),
                        lineItem.getLong("start_time"),
                        lineItem.getInt("lobby_type"),
                        matchPlayersList);

                matchIDList.add(match);
            }

        } catch (FileNotFoundException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "Unable to find file \"sample-matches.json\"");
        } catch (IOException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "IOException while reading line from file");
        }

        return matchIDList;
    }

    public void writeUserMatchToDB() {
        List<MatchID> matchIDList = getMatchIDFromJSON();
        List<User> userList = User.getAllUsers(mSQLController);

        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            List<MatchID> playerMatches = new ArrayList<>();

            for (int j = 0; j < matchIDList.size(); j++) {
                List<MatchPlayer> matchPlayerList = matchIDList.get(j).getMatchPlayers();

                for (int k = 0; k < matchPlayerList.size(); k++) {

                    if (matchPlayerList.get(k).getId32() == user.getSteamId32()) {
                        UserMatchID.saveUserMatch(mSQLController, user, matchIDList.get(j));
                    }
                }
            }
        }
    }

    @Test
    public void checkUserMatchID() {
        writeUserMatchToDB();

        HashMap<Long, Long> userMatchIDHashMap = UserMatchID.getAllUserMatch(mSQLController);
    }

}