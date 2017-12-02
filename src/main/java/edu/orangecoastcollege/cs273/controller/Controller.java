package edu.orangecoastcollege.cs273.controller;

import edu.orangecoastcollege.cs273.api.APIRequest;
import edu.orangecoastcollege.cs273.model.*;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller {
    private static final String TAG = "Controller";
    private static Controller mController;
    private static SQLController mSQLController;



    private Controller() {
    }

    public static Controller getInstance() {
        if (mController == null) {
            mController = new Controller();
        }

        if (mSQLController == null) {
            mSQLController = SQLController.getInstance();
        }

        return mController;
    }

    public void resetAllTables() {
            boolean success = mSQLController.resetAllTables();
            if (!success) {
                Logger.getLogger(TAG).log(Level.SEVERE, "resetAllTables() was called but an error has occurred");
            }
    }


    public void saveHeroesToDB(List<Hero> heroList) {
        try {
            mSQLController.openConnection();
            for (Hero h : heroList) {
                h.saveToDB(mSQLController);
            }
            mSQLController.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Hero> getHeroesList() {
        try {
            mSQLController.openConnection();
            return Hero.getAllHeroes(mSQLController);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
