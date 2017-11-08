package edu.orangecoastcollege.cs273.api;

import edu.orangecoastcollege.cs273.controller.SQLController;
import edu.orangecoastcollege.cs273.model.Hero;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class APIRequestTest {
    static SQLController mSQLController;
    static APIRequest mAPIRequest;

    @BeforeClass
    public static void databaseConnections() throws SQLException {
        mSQLController = SQLController.getInstance();

        mAPIRequest = new APIRequest();
    }

    @Before
    public void openNewConnection() throws SQLException {
        mSQLController.openConnection();
        Hero.deleteTable(mSQLController);
    }

    @After
    public void closeConnection() throws SQLException {
        mSQLController.close();
    }

    @Test
    public void getAllHeroes() throws Exception {
        List<Hero> serverQueryList = new ArrayList<>();
        serverQueryList = mAPIRequest.getAllHeroes();


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

}