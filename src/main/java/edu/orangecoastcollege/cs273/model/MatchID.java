package edu.orangecoastcollege.cs273.model;

import edu.orangecoastcollege.cs273.controller.SQLController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MatchID {
    private long mMatchId;
    private List<Long> mPlayersList;

    public MatchID(long matchId, List<Long> playersList) {
        mMatchId = matchId;
        mPlayersList = playersList;
    }

    public long getMatchId() {
        return mMatchId;
    }

    public void setMatchId(long matchId) {
        mMatchId = matchId;
    }

    public List<Long> getPlayersList() {
        return mPlayersList;
    }

    public void setPlayersList(List<Long> playersList) {
        mPlayersList = playersList;
    }

    private static final String TAG = "MatchID";

    public static List<MatchID> getAllMatchID(SQLController dbc) {
        return null;
    }

    public static List<MatchID> getAllUserMatchIDs(SQLController dbc, long steamId) {
        return null;
    }

    public static List<MatchID> getMatches(SQLController dbc, String selectStatement) {
        try {
            Connection connection = dbc.database();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(selectStatement);

            List<MatchID> matchIDList = new ArrayList<>();

            while (rs.next()) {
                // TODO: create modular method to accept different select queries
                // TODO: if searching for a single user, just make it a list of size 1
                // alternatively, we could create another private instance
                // variable and another overloaded constructor
            }
        } catch (SQLException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "Error retrieving entries from table \"match_id\" with query->" + selectStatement);
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
            String createStatement = "CREATE TABLE match_id(" +
                    "match_id BIGINT" +
                    "steam_id BIGINT);";

            try {
                Statement statement = connection.createStatement();
                statement.execute(createStatement);
            } catch (SQLException e) {
                Logger.getLogger(TAG).log(Level.SEVERE, "Error creating table \"match_id\"");
            }
        }

        @Override
        public void deleteTable(Connection connection) {
            String deleteStatement = "DELETE FROM match_id";
            try {
                Statement statement = connection.createStatement();
                statement.execute(deleteStatement);
            } catch (SQLException e) {
                Logger.getLogger(TAG).log(Level.SEVERE, "Error deleting table \"match_id\"");
            }
        }
    }
}
