package edu.orangecoastcollege.cs273.api;

import edu.orangecoastcollege.cs273.controller.SQLController;
import edu.orangecoastcollege.cs273.model.Hero;
import edu.orangecoastcollege.cs273.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertTrue;

public class APIRequestTest {
    static final String TAG = "APIRequestTest";
    static SQLController mSQLController;
    static APIRequest mAPIRequest;
    static HashMap<Integer, Hero> serverQueryHashMap;

    @BeforeClass
    public static void databaseConnections() throws SQLException {
        mSQLController = SQLController.getInstance();
        mAPIRequest = new APIRequest();

        TimerTask heroQueryTask = new TimerTask() {
            @Override
            public void run() {
                serverQueryHashMap = mAPIRequest.getAllHeroes();
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

        for (Hero h : serverQueryHashMap.values()) {
            h.saveToDB(mSQLController);
        }

        HashMap<Integer, Hero> readFromDB = new HashMap<>();
        readFromDB = Hero.getAllHeroes(mSQLController);

        for (Integer i : serverQueryHashMap.keySet()) {
            assertTrue(readFromDB.get(i).equals(serverQueryHashMap.get(i)));
        }
    }

    @Test
    public void checkUserDatabase() {
        User u1 = new User("1", 5, 1,
                "personaname1", "12345", "url1", "ava1");
        User u2 = new User("2", 5, 1,
                "personaname2", "12345", "url2", "ava2");
        User u3 = new User("3", 5, 1,
                "personaname3", "12345", "url3", "ava3");
        User u4 = new User("4", 5, 1,
                "personaname4", "12345", "url4", "ava4");
        User u5 = new User("5", 5, 1,
                "personaname5", "12345", "url5", "ava5");
        User u6 = new User("6", 5, 1,
                "personaname6", "12345", "url6", "ava6");

        HashMap<String, User> userHashMap = new HashMap<>();
        userHashMap.put(u1.getSteamId64(), u1);
        userHashMap.put(u2.getSteamId64(), u2);
        userHashMap.put(u3.getSteamId64(), u3);
        userHashMap.put(u4.getSteamId64(), u4);
        userHashMap.put(u5.getSteamId64(), u5);
        userHashMap.put(u6.getSteamId64(), u6);

        for (User u : userHashMap.values()) {
            u.saveToDB(mSQLController);
        }

        HashMap<String, User> readFromDB = User.getAllUsers(mSQLController);
        List<User> readFromDBList = new ArrayList<User>(readFromDB.values());
        List<User> seedDataList = new ArrayList<User>(userHashMap.values());

        readFromDBList.sort((o1, o2) -> Integer.valueOf(o1.getSteamId64()) - Integer.valueOf(o2.getSteamId64()));
        seedDataList.sort((o1, o2) -> Integer.valueOf(o1.getSteamId64()) - Integer.valueOf(o2.getSteamId64()));

        for (int i = 0; i < readFromDBList.size(); i++) {
            assertTrue(seedDataList.get(i).getPersonaName().equals(readFromDBList.get(i).getPersonaName()));
        }
    }

    @Test
    public void getAllUsers() {
        HashMap<String, User> allUsersHashMap = User.getAllUsers(mSQLController);
        for (User u : allUsersHashMap.values()) {
            Logger.getLogger(TAG).log(Level.INFO, u.getPersonaName());
        }

        assertTrue(allUsersHashMap.size() == 6);
    }

}