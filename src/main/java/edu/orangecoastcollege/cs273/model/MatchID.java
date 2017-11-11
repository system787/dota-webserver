package edu.orangecoastcollege.cs273.model;

import edu.orangecoastcollege.cs273.controller.SQLController;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vincenthoang on 8/2/17.
 */
public class MatchID {
    private long mMatchID;
    private long mMatchSeqNum;
    private long mStartTime;
    private int mLobbyType;
    private List<MatchPlayer> mMatchPlayers;

    public MatchID(long matchID, long matchSeqNum, long startTime, int lobbyType, List<MatchPlayer> matchPlayers) {
        mMatchID = matchID;
        mMatchSeqNum = matchSeqNum;
        mStartTime = startTime;
        mLobbyType = lobbyType;
        mMatchPlayers = matchPlayers;
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

    public long getStartTime() {
        return mStartTime;
    }

    public void setStartTime(long startTime) {
        mStartTime = startTime;
    }

    public int getLobbyType() {
        return mLobbyType;
    }

    public void setLobbyType(int lobbyType) {
        mLobbyType = lobbyType;
    }

    public List<MatchPlayer> getMatchPlayers() {
        return mMatchPlayers;
    }

    public void setMatchPlayers(List<MatchPlayer> matchPlayers) {
        mMatchPlayers = matchPlayers;
    }

    @Override
    public String toString() {
        return "MatchID{" +
                "mMatchID='" + mMatchID + '\'' +
                ", mMatchSeqNum='" + mMatchSeqNum + '\'' +
                ", mStartTime='" + mStartTime + '\'' +
                ", mLobbyType=" + mLobbyType +
                ", mMatchPlayers=" + mMatchPlayers +
                '}';
    }

    /* Database Methods */
    private static final String TAG = "MatchID";

    public void saveToDB(SQLController dbc) {
        String insertStatement = "INSERT INTO match_id(match_id, match_seq_num, start_time, lobby_type) VALUES (?, ?, ?, ?)";
        try {
            Connection connection = dbc.database();
            PreparedStatement preparedStatement = connection.prepareStatement(insertStatement);
            preparedStatement.setLong(1, mMatchID);
            preparedStatement.setLong(2, mMatchSeqNum);
            preparedStatement.setLong(3, mStartTime);
            preparedStatement.setInt(4, mLobbyType);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "Error inserting into table \"match_id\"");
        }
    }

    public static List<MatchID> getAllMatches(SQLController dbc) {
        String selectStatement = "SELECT * FROM match_id";

        try {
            Connection connection = dbc.database();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectStatement);
            List<MatchID> matchIDList = new ArrayList<>();

            while (resultSet.next()) {
                long matchId = resultSet.getLong("match_id");
                List<MatchPlayer> matchPlayersList = MatchPlayer.getPlayersInMatch(dbc, matchId);
                matchIDList.add(new MatchID(matchId,
                        resultSet.getLong("match_seq_num"),
                        resultSet.getLong("start_time"),
                        resultSet.getInt("lobby_type"),
                        matchPlayersList
                ));
            }

            return matchIDList;
        } catch (SQLException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "Error retrieving table \"match_id\"");
        }

        return null;
    }

    public static class Model extends SQLController.LocalDataBaseModel {
        public Model() {
            super();
        }

        @Override
        public void createTable(Connection connection) {
            String createStatement = "CREATE TABLE IF NOT EXISTS match_id(id INTEGER PRIMARY KEY NOT NULL, "
                    + "match_id INTEGER NOT NULL, "
                    + "match_seq_num INTEGER NOT NULL, "
                    + "start_time INTEGER NOT NULL, "
                    + "lobby_type INTEGER NOT NULL);";
            try {
                Statement statement = connection.createStatement();
                statement.execute(createStatement);
            } catch (SQLException e) {
                Logger.getLogger("MatchID.Model").log(Level.SEVERE, "Error creating table \"match_id\"");
            }
        }

        @Override
        public void deleteTable(Connection connection) {
            String deleteStatement = "DELETE FROM match_id";
            try {
                Statement statement = connection.createStatement();
                statement.execute(deleteStatement);
            } catch (SQLException e) {
                Logger.getLogger("MatchID.Model").log(Level.SEVERE, "Error deleting table \"match_id\"");
            }
        }
    }
}
