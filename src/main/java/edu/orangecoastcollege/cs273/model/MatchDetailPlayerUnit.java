package edu.orangecoastcollege.cs273.model;

import edu.orangecoastcollege.cs273.controller.SQLController;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MatchDetailPlayerUnit {
    private long mMatchId;
    private long mSteamId32;
    private String mUnitName;
    private int[] mItems;

    public MatchDetailPlayerUnit(long matchId, long steamId32, String unitName, int[] items) {
        mMatchId = matchId;
        mSteamId32 = steamId32;
        mUnitName = unitName;
        mItems = items;
    }

    public long getMatchId() {
        return mMatchId;
    }

    public void setMatchId(long matchId) {
        mMatchId = matchId;
    }

    public long getSteamId32() {
        return mSteamId32;
    }

    public void setSteamId32(long steamId32) {
        mSteamId32 = steamId32;
    }

    public String getUnitName() {
        return mUnitName;
    }

    public void setUnitName(String unitName) {
        mUnitName = unitName;
    }

    public int[] getItems() {
        return mItems;
    }

    public void setItems(int[] items) {
        mItems = items;
    }

    private static final String TAG = "MatchDetailPlayerUnit";

    private void saveToDB(SQLController dbc) {
        String insertStatement = "INSERT INTO player_unit (match_id, steam_id, unit_name, slot_zero, slot_one, slot_two, slot_three, slot_four, slot_five) VALUES (?,?,?,?,?,?,?,?,?)";
        try {
            Connection connection = dbc.database();
            PreparedStatement preparedStatement = connection.prepareStatement(insertStatement);
            preparedStatement.setLong(1, mMatchId);
            preparedStatement.setLong(2, mSteamId32);
            preparedStatement.setString(3, mUnitName);
            for (int i = 0; i < mItems.length; i++) {
                preparedStatement.setInt(i + 4, mItems[i]);
            }

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "Error inserting into table \"player_unit\"");
        }
    }

    public static MatchDetailPlayerUnit getPlayerUnit(SQLController dbc, long matchId, long steamId32) {
        String selectStatement = "SELECT * FROM player_unit WHERE (match_id =" + matchId + ") AND (steam_id =" + steamId32 + ")";

        try {
            Connection connection = dbc.database();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectStatement);

            int[] itemArray = new int[]{
                    resultSet.getInt("slot_zero"),
                    resultSet.getInt("slot_one"),
                    resultSet.getInt("slot_two"),
                    resultSet.getInt("slot_three"),
                    resultSet.getInt("slot_four"),
                    resultSet.getInt("slot_five"),
            };

            MatchDetailPlayerUnit playerUnit = new MatchDetailPlayerUnit(resultSet.getLong("match_id"),
                    resultSet.getLong("steam_id"),
                    resultSet.getString("unit_name"),
                    itemArray);

            return playerUnit;
        } catch (SQLException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "Error retrieving object from \"player_unit\"");
        }

        return null;
    }

    public static class Model extends SQLController.LocalDataBaseModel {
        private final String TAG = "MatchDetailPlayerUnit.Model";

        public Model() {
            super();
        }

        @Override
        public void createTable(Connection connection) {

            String createStatement = "CREATE TABLE IF NOT EXISTS player_unit(id INTEGER PRIMARY KEY NOT NULL, "
                    + "match_id INTEGER NOT NULL, "
                    + "steam_id INTEGER NOT NULL, "
                    + "unit_name TEXT NOT NULL, "
                    + "slot_zero INTEGER NOT NULL, "
                    + "slot_one INTEGER NOT NULL, "
                    + "slot_two INTEGER NOT NULL, "
                    + "slot_three INTEGER NOT NULL, "
                    + "slot_four INTEGER NOT NULL, "
                    + "slot_five INTEGER NOT NULL "
                    + ");";

            try {
                Statement statement = connection.createStatement();
                statement.execute(createStatement);
            } catch (SQLException e) {
                Logger.getLogger(TAG).log(Level.SEVERE, "Error creating table \"player_unit\"");
            }
        }

        @Override
        public void deleteTable(Connection connection) {
            String deleteStatement = "DELETE FROM player_unit";

            try {
                Statement statement = connection.createStatement();
                statement.execute(deleteStatement);
            } catch (SQLException e) {
                Logger.getLogger(TAG).log(Level.SEVERE, "Error deleting table \"player_unit\"");
            }
        }
    }
}
