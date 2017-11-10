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
    private String mMatchID;
    private String mMatchSeqNum;
    private String mStartTime;
    private int mLobbyType;
    private List<MatchPlayer> mMatchPlayers;

    public MatchID(String matchID, String matchSeqNum, String startTime, int lobbyType, List<MatchPlayer> matchPlayers) {
        mMatchID = matchID;
        mMatchSeqNum = matchSeqNum;
        mStartTime = startTime;
        mLobbyType = lobbyType;
        mMatchPlayers = matchPlayers;
    }

    public String getmMatchID() {
        return mMatchID;
    }


    public String getMatchID() {
        return mMatchID;
    }

    public void setMatchID(String matchID) {
        mMatchID = matchID;
    }

    public String getMatchSeqNum() {
        return mMatchSeqNum;
    }

    public void setMatchSeqNum(String matchSeqNum) {
        mMatchSeqNum = matchSeqNum;
    }

    public String getStartTime() {
        return mStartTime;
    }

    public void setStartTime(String startTime) {
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
            preparedStatement.setString(1, mMatchID);
            preparedStatement.setString(2, mMatchSeqNum);
            preparedStatement.setString(3, mStartTime);
            preparedStatement.setInt(4, mLobbyType);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "Error inserting into table \"match_id\"");
        }
    }

    public static HashMap<String, MatchID> getAllMatches(SQLController dbc) {
        String selectStatement = "SELECT * FROM match_id";

        try {
            Connection connection = dbc.database();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectStatement);
            HashMap<String, MatchID> matchIDHashMap = new HashMap<>();

            while (resultSet.next()) {
                String matchId = resultSet.getString("match_id");
                List<MatchPlayer> matchPlayersList = MatchPlayer.getPlayersInMatch(dbc, matchId);
                matchIDHashMap.put(matchId, new MatchID(matchId,
                        resultSet.getString("match_seq_num"),
                        resultSet.getString("start_time"),
                        resultSet.getInt("lobby_type"),
                        matchPlayersList
                ));
            }

            return matchIDHashMap;
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
                    + "match_id TEXT NOT NULL, "
                    + "match_seq_num TEXT NOT NULL, "
                    + "start_time TEXT NOT NULL, "
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
