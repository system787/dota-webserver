package edu.orangecoastcollege.cs273.model;

import edu.orangecoastcollege.cs273.controller.SQLController;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MatchID {
    private long mMatchId;
    private long mPlayerId;

    public MatchID(long mMatchId, long mPlayerId) {
        this.mMatchId = mMatchId;
        this.mPlayerId = mPlayerId;
    }

    public long getmMatchId() {
        return mMatchId;
    }

    public void setmMatchId(long mMatchId) {
        this.mMatchId = mMatchId;
    }

    public long getmPlayerId() {
        return mPlayerId;
    }

    public void setmPlayerId(long mPlayerId) {
        this.mPlayerId = mPlayerId;
    }

    private static final String TAG = "MatchID";

    public void saveToDB(SQLController dbc) {
        String insertStatement = "INSERT INTO match_id_table (match_id, steam_id) VALUES (?,?)";
        try {
            Connection connection = dbc.database();
            PreparedStatement pstmt = connection.prepareStatement(insertStatement);
            pstmt.setLong(1, mMatchId);
            pstmt.setLong(2, mPlayerId);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "Error inserting into table \"match_id_table\" on match_id " + mMatchId + " on steam_id " + mPlayerId);
        }
    }

    public static List<Long> getAllMatchID(SQLController dbc) {
        String selectStatement = "SELECT match_id FROM match_id_table";
        return getMatches(dbc, selectStatement, true);
    }

    public static List<Long> getMatchIDbyMatchID(SQLController dbc, long[] matchId) {
        StringBuilder sb = new StringBuilder("SELECT match_id FROM match_id_table WHERE match_id IN (");
        for (int i = 0; i < matchId.length; i++) {
            sb.append(String.valueOf(matchId[i]));
            if (i < matchId.length - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");
        String selectStatement = sb.toString();

        return getMatches(dbc, selectStatement, true);
    }

    public static List<Long> getAllUsersByMatchID(SQLController dbc, long matchId) {
        String selectStatement = "SELECT steam_id FROM match_id_table WHERE (match_id = " + String.valueOf(matchId) + ")";

        return getMatches(dbc, selectStatement, false);
    }

    public static List<Long> getAllMatchIDsByUser(SQLController dbc, long steamId) {
        String selectStatement = "SELECT match_id FROM match_id_table WHERE (steam_id = " + String.valueOf(steamId) + ")";

        return getMatches(dbc, selectStatement, true);
    }

    public static List<Long> getAllMatchIDsByUser(SQLController dbc, long[] steamId) {
        StringBuilder sb = new StringBuilder("SELECT match_id FROM match_id_table WHERE steam_id IN (");
        for (int i = 0; i < steamId.length; i++) {
            sb.append(String.valueOf(steamId[i]));
            if (i < steamId.length - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");
        String selectStatement = sb.toString();


        return getMatches(dbc, selectStatement, true);
    }

    private static List<Long> getMatches(SQLController dbc, String selectStatement, boolean searchByUser) {
        try {
            Connection connection = dbc.database();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(selectStatement);

            List<Long> output = new ArrayList<>();
            long id;

            if (searchByUser) {
                while (rs.next()) {
                    id = rs.getLong("match_id");
                    output.add(id);
                }
            } else {
                while (rs.next()) {
                    id = rs.getLong("steam_id");
                    output.add(id);
                }
            }

            return output;
        } catch (SQLException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "Error retrieving entries from table \"match_id_table\" with query->" + selectStatement);
        }

        return null;
    }

    public static class Model extends SQLController.LocalDataBaseModel {
        private static final String TAG = "MatchID.Model";

        public Model() {
            super();
        }

        @Override
        public void createTable(Connection connection) {
            String createStatement = "CREATE TABLE match_id_table(" +
                    "match_id BIGINT NOT NULL, " +
                    "steam_id BIGINT NOT NULL, " +
                    "UNIQUE (match_id, steam_id)" +
                    ");";

            try {
                Statement statement = connection.createStatement();
                statement.execute(createStatement);
            } catch (SQLException e) {
                Logger.getLogger(TAG).log(Level.SEVERE, "Error creating table \"match_id_table\"", e);
            }
        }

        @Override
        public void deleteTable(Connection connection) {
            String deleteStatement = "DELETE FROM match_id_table";
            try {
                Statement statement = connection.createStatement();
                statement.execute(deleteStatement);
            } catch (SQLException e) {
                Logger.getLogger(TAG).log(Level.SEVERE, "Error deleting table \"match_id_table\"", e);
            }
        }
    }
}
