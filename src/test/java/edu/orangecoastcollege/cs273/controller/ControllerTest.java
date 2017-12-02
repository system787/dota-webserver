package edu.orangecoastcollege.cs273.controller;

import edu.orangecoastcollege.cs273.api.APIRequest;
import edu.orangecoastcollege.cs273.model.Hero;
import edu.orangecoastcollege.cs273.model.User;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

public class ControllerTest {
    private static Controller mController;

    @BeforeClass
    public static void beforeClass() throws Exception {
        mController = Controller.getInstance();
        mController.resetAllTables();

    }

    @Before
    public void beforeMethod() throws Exception {

    }

    private int saveHeroesToDB() throws Exception {
        APIRequest apiRequest = new APIRequest();
        List<Hero> heroList = mController.updateHeroesList();
        mController.saveHeroesToDB(heroList);

        return heroList.size();
    }

    @Test
    public void readHeroesFromDB() throws Exception {
        int heroListSize = saveHeroesToDB();
        List<Hero> heroListFromDB = mController.getHeroesList();
        int listSizeFromDB = heroListFromDB.size();

        String message = "size from server->" + heroListSize + "; size from database->" + listSizeFromDB;

        assertEquals(message, heroListSize, heroListFromDB.size());
    }

    private void signUpNewUsers() throws Exception {
        mController.signUpNewUser(114611);
        mController.signUpNewUser(4294967295L);
        mController.signUpNewUser(49973220L);
        mController.signUpNewUser(39974939);
    }

    @Test
    public void printUserObjects() throws Exception {
        signUpNewUsers();
        List<User> allUsersList = mController.getAllUsers();
        for (User u : allUsersList) {
            System.out.println(u.toString());
        }
    }

    @Test
    public void getUserSummaries() throws Exception {
        signUpNewUsers();
        List<User> allUsersList = mController.getAllUsers();

        List<User> filteredList = mController.getUserSummaries(new long[]{39974939, 114611});

        for (User u : filteredList) {
            assertTrue(allUsersList.contains(u));
        }
    }

}