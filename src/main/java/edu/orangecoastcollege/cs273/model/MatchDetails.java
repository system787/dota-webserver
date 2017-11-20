package edu.orangecoastcollege.cs273.model;

import edu.orangecoastcollege.cs273.controller.SQLController;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MatchDetails {
    private long mMatchID;
    private long mMatchSeqNum;
    private List<MatchDetailPlayer> mMatchDetailPlayerList;
    private boolean mRadiantWin;
    private int mDuration;
    private int mFirstBloodTime;
    private int mLobbyType;
    private int mNumPlayers;
    private int mGameMode;      // 0 - None
                                // 1 - All Pick
                                // 2 - Captain's Mode
                                // 3 - Random Draft
                                // 4 - Single Draft
                                // 5 - All Random
                                // 6 - Intro
                                // 7 - Diretide
                                // 8 - Reverse Captain's Mode
                                // 9 - The Greeviling
                                // 10 - Tutorial
                                // 11 - Mid Only
                                // 12 - Least Played
                                // 13 - New Player Pool
                                // 14 - Compendium Matchmaking
                                // 15 - Co-op vs Bots
                                // 16 - Captains Draft
                                // 18 - Ability Draft
                                // 20 - All Random Deathmatch
                                // 21 - 1v1 Mid Only
                                // 22 - Ranked Matchmaking


    public MatchDetails(long matchID, long matchSeqNum, List<MatchDetailPlayer> matchDetailPlayerList,
                        boolean radiantWin, int duration, int firstBloodTime,
                        int lobbyType, int numPlayers, int gameMode) {
        mMatchID = matchID;
        mMatchSeqNum = matchSeqNum;
        mMatchDetailPlayerList = matchDetailPlayerList;
        mRadiantWin = radiantWin;
        mDuration = duration;
        mFirstBloodTime = firstBloodTime;
        mLobbyType = lobbyType;
        mNumPlayers = numPlayers;
        mGameMode = gameMode;
    }

    public long getMatchID() {
        return mMatchID;
    }

    public void setMatchID(long matchID) {
        mMatchID = matchID;
    }

    public long getMatchSeqNum() {
        return mMatchSeqNum;
    }

    public void setMatchSeqNum(long matchSeqNum) {
        mMatchSeqNum = matchSeqNum;
    }

    public List<MatchDetailPlayer> getMatchDetailPlayerList() {
        return mMatchDetailPlayerList;
    }

    public void setMatchDetailPlayerList(List<MatchDetailPlayer> matchDetailPlayerList) {
        mMatchDetailPlayerList = matchDetailPlayerList;
    }

    public boolean isRadiantWin() {
        return mRadiantWin;
    }

    public void setRadiantWin(boolean radiantWin) {
        mRadiantWin = radiantWin;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public int getFirstBloodTime() {
        return mFirstBloodTime;
    }

    public void setFirstBloodTime(int firstBloodTime) {
        mFirstBloodTime = firstBloodTime;
    }

    public int getLobbyType() {
        return mLobbyType;
    }

    public void setLobbyType(int lobbyType) {
        mLobbyType = lobbyType;
    }

    public int getNumPlayers() {
        return mNumPlayers;
    }

    public void setNumPlayers(int numPlayers) {
        mNumPlayers = numPlayers;
    }

    public int getGameMode() {
        return mGameMode;
    }

    public void setGameMode(int gameMode) {
        mGameMode = gameMode;
    }

    private static final String TAG = "MatchDetails";

    public void saveToDB(SQLController dbc) {
        String insertStatement = "INSERT INTO match_details (match_id, match_seq_num, duration, first_blood, lobby_type, num_players, game_mode, radiant_win) VALUES (?,?,?,?,?,?,?,?)";

        try {
            Connection connection = dbc.database();
            PreparedStatement preparedStatement = connection.prepareStatement(insertStatement);
            preparedStatement.setLong(1, mMatchID);
            preparedStatement.setLong(2, mMatchSeqNum);
            preparedStatement.setInt(3, mDuration);
            preparedStatement.setInt(4, mFirstBloodTime);
            preparedStatement.setInt(5, mLobbyType);
            preparedStatement.setInt(6, mNumPlayers);
            preparedStatement.setInt(7, mGameMode);
            preparedStatement.setInt(8, mRadiantWin ? 1 : 0);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "Error inserting into table \"match_details\"", e);
        }
    }

    public static List<MatchDetails> getAllMatchDetailsList(SQLController dbc) {
        String selectStatement = "SELECT match_id, match_seq_num, duration, first_blood, lobby_type, num_players, game_mode, radiant_win FROM match_details";
        try {
            Connection connection = dbc.database();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectStatement);

            List<MatchDetails> matchDetailsList = new ArrayList<>();

            while (resultSet.next()) {
                long matchId = resultSet.getLong("match_id");
                List<MatchDetailPlayer> matchDetailPlayerList = MatchDetailPlayer.getPlayerDetailsList(dbc, matchId);
                MatchDetails matchDetails = new MatchDetails(
                        matchId, resultSet.getLong("match_seq_num"), matchDetailPlayerList,
                        resultSet.getBoolean("radiant_win"), resultSet.getInt("duration"), resultSet.getInt("first_blood"),
                        resultSet.getInt("lobby_type"), resultSet.getInt("num_players"), resultSet.getInt("game_mode")
                );
                matchDetailsList.add(matchDetails);
            }
            return matchDetailsList;
        } catch (SQLException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "Error retrieving all entries from table \"match_details\"");
        }
        return null;
    }

    public static class Model extends SQLController.LocalDataBaseModel {
        private static final String TAG = "MatchDetails.Model";

        public Model() {
            super();
        }

        @Override
        public void createTable(Connection connection) {
            String createStatement = "CREATE TABLE match_details(" +
                    "match_id BIGINT PRIMARY KEY NOT NULL, " +
                    "match_seq_num BIGINT NOT NULL, " +
                    "radiant_win INT NOT NULL, " +
                    "duration INT NOT NULL, " +
                    "first_blood INT NOT NULL, " +
                    "lobby_type INT NOT NULL, " +
                    "num_players INT NOT NULL, " +
                    "game_mode INT NOT NULL);";
            try {
                Statement statement = connection.createStatement();
                statement.execute(createStatement);
            } catch (SQLException e) {
                Logger.getLogger(TAG).log(Level.SEVERE, "Error creating table \"match_details\"");
            }
        }

        @Override
        public void deleteTable(Connection connection) {
            String deleteStatement = "DELETE FROM match_details";
            try {
                Statement statement = connection.createStatement();
                statement.execute(deleteStatement);
            } catch (SQLException e) {
                Logger.getLogger(TAG).log(Level.SEVERE, "Error deleting table \"match_details\"");
            }
        }
    }
}
