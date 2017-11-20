package edu.orangecoastcollege.cs273.model;

import edu.orangecoastcollege.cs273.controller.SQLController;

import java.sql.Connection;
import java.util.List;

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

    public static class Model extends SQLController.LocalDataBaseModel {
        public Model() {
            super();
        }

        @Override
        public void createTable(Connection connection) {
            super.createTable(connection);
        }

        @Override
        public void deleteTable(Connection connection) {
            super.deleteTable(connection);
        }
    }
}
