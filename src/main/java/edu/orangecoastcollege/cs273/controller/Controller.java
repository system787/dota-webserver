package edu.orangecoastcollege.cs273.controller;

import edu.orangecoastcollege.cs273.api.APIRequest;
import edu.orangecoastcollege.cs273.model.*;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller {
    private static final String TAG = "Controller";
    private static Controller mController;
    private static SQLController mSQLController;
    private static QueryExecutor mQueryExecutor;

    private static final int MAX_USER_CAPACITY = 5000;
    private static final int MAX_MATCH_ID_CAPACITY = 50000;

    private static HashSet mMatchIdHash;
    private static HashSet mUserIdHash;
    private static APIRequest mApiRequest;


    private Controller() {
    }

    public static Controller getInstance() {
        if (mController == null) {
            mController = new Controller();
        }
        if (mSQLController == null) {
            mSQLController = SQLController.getInstance();
        }

        mApiRequest = new APIRequest();

        if (mQueryExecutor == null) {
            mQueryExecutor = QueryExecutor.getInstance(mApiRequest);
        }

        loadSteamIds();


        return mController;
    }


    // restrict access after testing everything else
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

    public void updateHeroesList() {
        List<Hero> heroListDB = getHeroesList();

        // TODO: low priority - create method to get new list of heroes and compare, then replace if updated
    }

    public void saveUsersToDB(List<User> userList) {
        try {
            mSQLController.openConnection();
            for (User u : userList) {
                u.saveToDB(mSQLController);
            }
            mSQLController.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void loadSteamIds() {
        mMatchIdHash = new HashSet();
        mUserIdHash = new HashSet();
        try {
            mSQLController.openConnection();
            List<Long> matchIdList = MatchID.getAllMatchID(mSQLController);
            List<Long> userIdList = User.getAllUserId(mSQLController);
            mSQLController.close();

            mMatchIdHash.addAll(matchIdList);
            mUserIdHash.addAll(userIdList);

            Logger.getLogger(TAG).log(Level.INFO, "Steam IDs and Match IDs were successfully loaded");
        } catch (SQLException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "Error loading steam or match ids from database", e);
        } catch (NullPointerException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "Either mMatchIdList or mUserIdList was null", e);
        }
    }

    public boolean checkUserRegistration(long steamId) {
        return mUserIdHash.contains(steamId);
    }

    public boolean signUpNewUser(long steamId) {
        if (!checkUserRegistration(steamId)) {
            List<User> userList = mQueryExecutor.scheduleQueryUserSummaries(new long[]{steamId});
            saveUsersToDB(userList);

            long userId = userList.get(0).getSteamId32();
            mUserIdHash.add(userId);

            return true;
        }

        return false;
    }
}
