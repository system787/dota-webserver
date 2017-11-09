package edu.orangecoastcollege.cs273.model;

import edu.orangecoastcollege.cs273.controller.SQLController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserMatchID {
    private static final String TAG = "UserMatchID";

    public static void saveUserMatch(SQLController dbc, User user, MatchID match) {
        String insertStatement = "INSERT INTO user_match(steam_id, match_id) VALUES(?,?)";
        try {
            Connection connection = dbc.database();
            PreparedStatement preparedStatement = connection.prepareStatement(insertStatement);
            preparedStatement.setString(1, user.getSteamId64());
            preparedStatement.setString(2, match.getMatchID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "Error inserting into table \"user_match\"");
        }
    }

    public static class Model extends SQLController.LocalDataBaseModel {
        public Model() {
            super();
        }

        @Override
        public void createTable(Connection connection) {
            String createStatement = "CREATE TABLE IF NOT EXISTS user_match(id INTEGER PRIMARY KEY NOT NULL, " +
                    "steam_id TEXT NOT NULL, " +
                    "match_id TEXT NOT NULL);";
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