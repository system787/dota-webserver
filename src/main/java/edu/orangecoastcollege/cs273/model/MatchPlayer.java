package edu.orangecoastcollege.cs273.model;

import edu.orangecoastcollege.cs273.controller.SQLController;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MatchPlayer {
    private long mMatchID;
    private long mId32;
    private int mSlot;
    private int mHeroId;

    public MatchPlayer(long matchID, long id32, int slot, int heroId) {
        mMatchID = matchID;
        mId32 = id32;
        mSlot = slot;
        mHeroId = heroId;
    }

    public long getMatchID() {
        return mMatchID;
    }

    public void setMatchID(long matchID) {
        mMatchID = matchID;
    }

    public long getId32() {
        return mId32;
    }

    public void setId32(long id32) {
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
        String insertStatement = "INSERT INTO match_players(match_id, steam_id, slot, hero_id) VALUES(?, ?, ?, ?)";
        try {
            Connection connection = dbc.database();
            PreparedStatement preparedStatement = connection.prepareStatement(insertStatement);
            preparedStatement.setLong(1, mMatchID);
            preparedStatement.setLong(2, mId32);
            preparedStatement.setInt(3, mSlot);
            preparedStatement.setInt(4, mHeroId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger("MatchPlayers.Model").log(Level.SEVERE, "Error inserting into table \"match_players\"");
        }
    }

    public static List<MatchPlayer> getPlayersInMatch(SQLController dbc, long matchID) {
        String selectStatement = "SELECT * FROM match_players WHERE match_id=" + matchID;
        try {
            Connection connection = dbc.database();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectStatement);
            List<MatchPlayer> matchPlayersList = new ArrayList<>();

            while (resultSet.next()) {
                matchPlayersList.add(new MatchPlayer(resultSet.getLong("match_id"),
                        resultSet.getLong("steam_id"),
                        resultSet.getInt("slot"),
                        resultSet.getInt("hero_id")));
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
                    + "match_id INTEGER NOT NULL, "
                    + "steam_id INTEGER NOT NULL, "
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
