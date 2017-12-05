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

    public boolean signUpNewUser(long steamId64) {
        if (steamId64 < STEAM_ID_64_DIFFERENCE) {
            steamId64 += STEAM_ID_64_DIFFERENCE;
        }

        List<User> userList = mQueryExecutor.scheduleQueryUserSummaries(new long[]{steamId64});
        saveUsersToDB(userList);

        if (userList.size() == 0) {
            return false;
        }

        long userId = userList.get(0).getSteamId32();

        return true;
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

    public List<Long> getUserMatches(long... steamId32) {
        try {
            mSQLController.openConnection();
            List<Long> matchIDList = MatchID.getAllMatchIDsByUser(mSQLController, steamId32);
            mSQLController.close();

            return matchIDList;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean saveMatchDetails(List<MatchDetails> matchDetails) {
        try {
            mSQLController.openConnection();
            for (MatchDetails m : matchDetails) {
                m.saveToDB(mSQLController);
                for (MatchDetailPlayer mp : m.getMatchDetailPlayerList()) {
                    mp.saveToDB(mSQLController);
                }
            }
            mSQLController.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Long> checkDBMatches(long[] matchIdArray) {
        try {
            mSQLController.openConnection();
            List<Long> matchIdList = MatchID.getMatchIDbyMatchID(mSQLController, matchIdArray);
            mSQLController.close();
            Set<Long> hs = new HashSet<>();
            hs.addAll(matchIdList);
            matchIdList.clear();
            matchIdList.addAll(hs);
            return matchIdList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<MatchDetails> getMatchDetails(List<Long> matchIDList) {
        try {
            mSQLController.openConnection();
            List<MatchDetails> matchDetailsList = MatchDetails.getMatchDetails(mSQLController, matchIDList);
            mSQLController.close();
            return matchDetailsList;
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

    public boolean saveMatchID(List<MatchID> matchIDList) {
        try {
            mSQLController.openConnection();
            for (MatchID m : matchIDList) {
                m.saveToDB(mSQLController);
            }
            mSQLController.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<MatchDetails> getLatestMatches(long steamId32) {
        if (steamId32 > STEAM_ID_64_DIFFERENCE) {
            steamId32 -= STEAM_ID_64_DIFFERENCE;
        }

        List<MatchID> matchIDList = mQueryExecutor.scheduleQueryMatchTask(steamId32); // Retrieve a list from Steam API of 25 latest matches

        List<Long> matchIDListFiltered = new ArrayList<>();

        for (MatchID m : matchIDList) {
            matchIDListFiltered.add(m.getmMatchId());
        }

        Set<Long> hs = new HashSet<>();
        hs.addAll(matchIDListFiltered);
        matchIDListFiltered.clear();
        matchIDListFiltered.addAll(hs);

        long[] matchIDArray = new long[matchIDListFiltered.size()];

        for (int i = 0; i < matchIDArray.length; i++) {
            matchIDArray[i] = matchIDList.get(i).getmMatchId();
        }

        List<Long> retrievedMatches = checkDBMatches(matchIDArray); // Retrieve a list of all match ids that exist in database

        saveMatchID(matchIDList);

        if (retrievedMatches.size() == 0) { // If list from database is empty, query all 25 matches and return that list.
            Logger.getLogger(TAG).log(Level.WARNING, "RetrievedMatches from database is 0!");
            List<MatchDetails> matchDetailsList = mQueryExecutor.scheduleMatchDetailsList(matchIDArray);
            saveMatchDetails(matchDetailsList);
            return matchDetailsList;
        }

        List<MatchDetails> matchDetailsList = getMatchDetails(retrievedMatches); // Retrieve match details from the match ids earlier


        if (retrievedMatches.size() < matchIDListFiltered.size()) { // If retrieved matches < list queried from server, remove all the matches that were already queried.
            // TODO:
            Logger.getLogger(TAG).log(Level.INFO, "retrievedMatches.size() == " + retrievedMatches.size() + "; matchIdList.size() == " + matchIDListFiltered.size());
            List<Long> matchIDlongs = new ArrayList<>();

            matchIDlongs.addAll(matchIDListFiltered);

            matchIDlongs.removeAll(retrievedMatches);
            // TODO:
            for (long l : matchIDlongs) {
                Logger.getLogger(TAG).log(Level.INFO, "matchIDLongs to be queried: " + String.valueOf(l));
            }

            long[] array = matchIDlongs.stream().mapToLong(l -> l).toArray();

            List<MatchDetails> partialList = mQueryExecutor.scheduleMatchDetailsList(array); // Query server with ids that weren't removed, save them to database, add them to the output list
            saveMatchDetails(partialList);
            matchDetailsList.addAll(partialList);
        }

        return matchDetailsList;
    }
}
