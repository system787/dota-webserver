package edu.orangecoastcollege.cs273.controller;

import com.google.gson.Gson;
import edu.orangecoastcollege.cs273.api.APIRequest;
import edu.orangecoastcollege.cs273.model.*;

import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller {
    private static final String TAG = "Controller";
    private static Controller mController;
    private static SQLController mSQLController;
    private static QueryExecutor mQueryExecutor;

    private static final int MAX_USER_CAPACITY = 5000;
    private static final int MAX_MATCH_ID_CAPACITY = 50000;

    public static final long STEAM_ID_64_DIFFERENCE = 76561197960265728L;

    private static HashSet<Long> mMatchIdHash;
    private static HashSet<Long> mUserIdHash;
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

    public List<Hero> updateHeroesList() {
        List<Hero> heroListDB = getHeroesList();
        heroListDB.sort((o1, o2) -> o1.getId() - o2.getId());

        List<Hero> heroListServer = mQueryExecutor.scheduleQueryHeroesList();
        heroListServer.sort((o1, o2) -> o1.getId() - o2.getId());

        if (!heroListDB.equals(heroListServer)) {
            try {
                mSQLController.openConnection();
                Hero.deleteTable(mSQLController);
                for (Hero h : heroListServer) {
                    h.saveToDB(mSQLController);
                }
                mSQLController.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return heroListServer;
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
        mMatchIdHash = new HashSet<Long>();
        mUserIdHash = new HashSet<Long>();
        try {
            mSQLController.openConnection();
            List<Long> matchIdList = MatchID.getAllMatchID(mSQLController);
            List<Long> userIdList = User.getAllUserId(mSQLController);
            mSQLController.close();

            mMatchIdHash.addAll(matchIdList);
            mUserIdHash.addAll(userIdList);

            // mMatchIdHash.addAll(matchIdList);
            // mUserIdHash.addAll(userIdList);

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

    public boolean signUpNewUser(long steamId64) {
        if (steamId64 < STEAM_ID_64_DIFFERENCE) {
            steamId64 += STEAM_ID_64_DIFFERENCE;
        }

        if (!checkUserRegistration(steamId64)) {
            List<User> userList = mQueryExecutor.scheduleQueryUserSummaries(new long[]{steamId64});
            saveUsersToDB(userList);

            if (userList.size() == 0) {
                return false;
            }

            long userId = userList.get(0).getSteamId32();
            mUserIdHash.add(userId);

            return true;
        }

        return false;
    }

    public List<User> getAllUsers() {
        try {
            mSQLController.openConnection();
            List<User> userList = User.getAllUsers(mSQLController);
            mSQLController.close();
            return userList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getUserSummaries(long[] steamId64) {
        try {
            mSQLController.openConnection();
            for (long l : steamId64) {
                if (l < STEAM_ID_64_DIFFERENCE) {
                    l += STEAM_ID_64_DIFFERENCE;
                }
            }
            List<User> userList = User.getUsersByID(mSQLController, steamId64);
            mSQLController.close();
            return userList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String toGson(Object o) {
        Gson gson = new Gson();
        String output = gson.toJson(o);

        return output;
    }

    public List<MatchDetails> getLatestMatchesSingleUser(long steamId32) {
        if (steamId32 > STEAM_ID_64_DIFFERENCE) {
            steamId32 -= STEAM_ID_64_DIFFERENCE;
        }

        List<MatchID> matchIDList = mQueryExecutor.scheduleQueryMatchTask(steamId32);

        List<MatchID> matchesNotInHash = new ArrayList<>();

        for (MatchID m : matchIDList) {
            if (!mMatchIdHash.contains(m.getmMatchId())) {
                matchesNotInHash.add(m);
            }
        }

        for (MatchID m : matchesNotInHash) {
            // TODO: create scheduleQueryMatchDetails first
        }

        return null;
    }

}
