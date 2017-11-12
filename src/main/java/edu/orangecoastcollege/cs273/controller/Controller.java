package edu.orangecoastcollege.cs273.controller;

import edu.orangecoastcollege.cs273.model.MatchID;
import edu.orangecoastcollege.cs273.model.MatchPlayer;
import edu.orangecoastcollege.cs273.model.User;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller {
    private static final String TAG = "Controller";
    private static Controller mController;
    private static SQLController mSQLController;

    private static HashMap<Long, MatchID> mAllMatchIDHashMap; // <match_id, MatchID>
    private static HashMap<Long, User> mAllUserHashMap; // <steam_id, User>
    private static HashMap<Long, Long> mAllUserMatchID; // <steam_id, match_id>

    // TODO: create MatchDetails class and MatchIDMatchDetails class

    private Controller() {

    }

    public static Controller getInstance() {
        if (mController == null) {
            mController = new Controller();
        }

        if (mSQLController == null) {
            mSQLController = SQLController.getInstance();
        }

        populateHashMaps();

        return mController;
    }

    private static void populateHashMaps() {
        try {
            mSQLController.openConnection();
            List<MatchID> matchIDList = MatchID.getAllMatches(mSQLController);
            List<User> userList = User.getAllUsers(mSQLController);
            mSQLController.close();

            mAllMatchIDHashMap = createMatchIDHashMap(matchIDList);
            mAllUserHashMap = createUserHashMap(userList);
            mAllUserMatchID = relateUserToMatchID(userList, matchIDList);

        } catch (SQLException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "Unable to read entries from database", e);
        }
    }

    private static HashMap<Long, MatchID> createMatchIDHashMap(List<MatchID> matchIDList) {
        HashMap<Long, MatchID> hashMap = new HashMap<>();

        for (MatchID m : matchIDList) {
            hashMap.put(m.getMatchID(), m);
        }

        return hashMap;
    }

    private static HashMap<Long, User> createUserHashMap(List<User> userList) {
        HashMap<Long, User> hashMap = new HashMap<>();

        for (User u : userList) {
            hashMap.put(u.getSteamId32(), u);
        }

        return hashMap;
    }

    private static HashMap<Long, Long> relateUserToMatchID(List<User> userList, List<MatchID> matchIDList) {
        HashMap<Long, Long> userMatchIDHashMap = new HashMap<>();
        for (User u : userList) {
            for (MatchID matchID : matchIDList) {
                if (matchID.getMatchPlayers().contains(u.getSteamId32())) {
                    userMatchIDHashMap.put(u.getSteamId32(), matchID.getMatchID());
                }
            }
        }

        return userMatchIDHashMap;
    }

}
