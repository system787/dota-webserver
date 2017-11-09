package edu.orangecoastcollege.cs273.api;

import edu.orangecoastcollege.cs273.controller.SQLController;
import edu.orangecoastcollege.cs273.model.Hero;
import edu.orangecoastcollege.cs273.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class DatabaseTesting {
    static final String TAG = "DatabaseTesting";
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

        List<Hero> readFromDB = new ArrayList<>();
        readFromDB = Hero.getAllHeroes(mSQLController);

        serverQueryList.sort((o1, o2) -> o1.getId() - o2.getId());
        readFromDB.sort((o1, o2) -> o1.getId() - o2.getId());

        for (int i = 0; i < readFromDB.size(); i++) {
            assertTrue(readFromDB.get(i).equals(serverQueryList.get(i)));
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

        List<User> userList = new ArrayList<>();
        userList.add(u1);
        userList.add(u2);
        userList.add(u3);
        userList.add(u4);
        userList.add(u5);
        userList.add(u6);

        for (User u : userList) {
            u.saveToDB(mSQLController);
        }

        List<User> readFromDB = User.getAllUsers(mSQLController);

        readFromDB.sort((o1,o2) -> Integer.parseInt(o1.getSteamId64()) - Integer.parseInt(o2.getSteamId64()));
        userList.sort((o1,o2) -> Integer.parseInt(o1.getSteamId64()) - Integer.parseInt(o2.getSteamId64()));

        for (int i = 0; i < userList.size(); i++) {
            assertTrue(userList.get(i).getPersonaName().equals(readFromDB.get(i).getPersonaName()));
        }
    }

    @Test
    public void getAllUsers() {
        List<User> allUsersList = User.getAllUsers(mSQLController);
        for (User u : allUsersList) {
            Logger.getLogger(TAG).log(Level.INFO, u.getPersonaName());
        }

        assertTrue(allUsersList.size() == 6);
    }




}