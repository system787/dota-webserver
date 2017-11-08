package edu.orangecoastcollege.cs273.model;

import edu.orangecoastcollege.cs273.controller.SQLController;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MatchPlayers {
    private String mMatchID;
    private int mId32;
    private int mSlot;
    private int mHeroId;

    public MatchPlayers(String matchID, int id32, int slot, int heroId) {
        mMatchID = matchID;
        mId32 = id32;
        mSlot = slot;
        mHeroId = heroId;
    }

    public String getMatchID() {
        return mMatchID;
    }

    public void setMatchID(String matchID) {
        mMatchID = matchID;
    }

    public int getId32() {
        return mId32;
    }

    public void setId32(int id32) {
        mId32 = id32;
    }

    public int getSlot() {
        return mSlot;
    }

    public void setSlot(int slot) {
        mSlot = slot;
    }

    public int getHeroId() {
        return mHeroId;
    }

    public void setHeroId(int heroId) {
        mHeroId = heroId;
    }

    /* Database Methods */
    private static final String TAG = "MatchPlayers";

    public void saveToDB(SQLController dbc) {
        String insertStatement = "INSERT INTO match_players(match_id, id32, slot, hero_id) VALUES(?, ?, ?, ?)";
        try {
            Connection connection = dbc.database();
            PreparedStatement preparedStatement = connection.prepareStatement(insertStatement);
            preparedStatement.setString(1, mMatchID);
            preparedStatement.setInt(2, mId32);
            preparedStatement.setInt(3, mSlot);
            preparedStatement.setInt(4, mHeroId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger("MatchPlayers.Model").log(Level.SEVERE, "Error inserting into table \"match_players\"");
        }
    }

    public static List<MatchPlayers> getPlayersInMatch(SQLController dbc, String matchID) {
        String selectStatement = "SELECT * FROM match_players WHERE match_id=" + matchID;
        try {
            Connection connection = dbc.database();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectStatement);
            List<MatchPlayers> matchPlayersList = new ArrayList<>();

            while (resultSet.next()) {
                matchPlayersList.add(new MatchPlayers(resultSet.getString(0),
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getInt(3)));
            }

            return matchPlayersList;
        } catch (SQLException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "Error while retrieving players in match " + matchID);
        }
        return null;
    }

    public static class Model extends SQLController.LocalDataBaseModel {
        public Model() {
            super();
        }

        @Override
        public void createTable(Connection connection) {
            String createStatement = "CREATE TABLE IF NOT EXISTS match_players(id INTEGER PRIMARY KEY NOT NULL, "
                    + "match_id TEXT NOT NULL, "
                    + "id32 INTEGER NOT NULL, "
                    + "slot INTEGER NOT NULL, "
                    + "hero_id INTEGER NOT NULL);";

            try {
                Statement statement = connection.createStatement();
                statement.execute(createStatement);
            } catch (SQLException e) {
                Logger.getLogger("MatchPlayers.Model").log(Level.SEVERE, "Error creating table \"match_players\"");
            }
        }

        @Override
        public void deleteTable(Connection connection) {
            String deleteStatement = "DELETE FROM match_players";
            try {
                Statement statement = connection.createStatement();
                statement.execute(deleteStatement);
            } catch (SQLException e) {
                Logger.getLogger("Hero.Model").log(Level.SEVERE, "Error deleting table \"match_players\"");
            }
        }
    }
}