package edu.orangecoastcollege.cs273.model;

import edu.orangecoastcollege.cs273.controller.SQLController;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserMatchID {
    private long mId32;
    private long mMatchID;

    public UserMatchID(long id32, long matchID) {
        mId32 = id32;
        mMatchID = matchID;
    }

    public long getId32() {
        return mId32;
    }

    public void setId32(long id32) {
        mId32 = id32;
    }

    public long getMatchID() {
        return mMatchID;
    }

    public void setMatchID(long matchID) {
        mMatchID = matchID;
    }

    private static final String TAG = "UserMatchID";

    public static void saveUserMatch(SQLController dbc, User user, MatchID match) {
        String insertStatement = "INSERT INTO user_match(steam_id, match_id) VALUES(?,?)";
        try {
            Connection connection = dbc.database();
            PreparedStatement preparedStatement = connection.prepareStatement(insertStatement);
            preparedStatement.setLong(1, user.getSteamId32());
            preparedStatement.setLong(2, match.getMatchID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "Error inserting into table \"user_match\"");
        }
    }

    public static List<UserMatchID> getAllUserMatch(SQLController dbc) {
        String selectStatement = "SELECT * FROM user_match";
        try {
            Connection connection = dbc.database();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectStatement);
            List<UserMatchID> userMatchIDList = new ArrayList<>();
            while (resultSet.next()) {
                userMatchIDList.add(new UserMatchID(resultSet.getLong("steam_id"), resultSet.getLong("match_id")));
            }
            return userMatchIDList;
        } catch (SQLException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "Error retrieving table \"user_match\"");
        }
        return null;
    }

    public static class Model extends SQLController.LocalDataBaseModel {
        public Model() {
            super();
        }

        @Override
        public void createTable(Connection connection) {
            String createStatement = "CREATE TABLE IF NOT EXISTS user_match(id INTEGER PRIMARY KEY NOT NULL, " +
                    "steam_id INTEGER NOT NULL, " +
                    "match_id INTEGER NOT NULL);";
            try {
                Statement statement = connection.createStatement();
                statement.execute(createStatement);
            } catch (SQLException e) {
                Logger.getLogger("UserMatchID.Model").log(Level.SEVERE, "Error creating table \"user_match\"");
            }
        }

        @Override
        public void deleteTable(Connection connection) {
            String deleteStatement = "DELETE FROM user_match";
            try {
                Statement statement = connection.createStatement();
                statement.execute(deleteStatement);
            } catch (SQLException e) {
                Logger.getLogger("UserMatchID.Model").log(Level.SEVERE, "Error deleting table \"user_match\"");
            }
        }
    }
}
