package edu.orangecoastcollege.cs273.controller;

import edu.orangecoastcollege.cs273.api.APIRequest;
import edu.orangecoastcollege.cs273.model.Hero;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ControllerTest {
    public static Controller mController;

    @BeforeClass
    public static void beforeClass() throws Exception {
        mController = Controller.getInstance();
        mController.resetAllTables();
    }

    @Before
    public void beforeMethod() throws Exception {

    }

    public int saveHeroesToDB() throws Exception {
        APIRequest apiRequest = new APIRequest();
        List<Hero> heroList = apiRequest.getAllHeroes();
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

}