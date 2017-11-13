package edu.orangecoastcollege.cs273.model;

import edu.orangecoastcollege.cs273.controller.SQLController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MatchDetailPlayer {
    private long mMatchId;
    private long mAccountId;
    private int mHeroId;
    private int[] mItems;
    private int mKills;
    private int mDeaths;
    private int mAssists;
    private int mLeaverStatus;      // 0 - NONE - finished match, no abandon.
                                    // 1 - DISCONNECTED - player DC, no abandon.
                                    // 2 - DISCONNECTED_TOO_LONG - player DC > 5min, abandoned.
                                    // 3 - ABANDONED - player DC, clicked leave, abandoned.
                                    // 4 - AFK - player AFK, abandoned.
                                    // 5 - NEVER_CONNECTED - player never connected, no abandon.
                                    // 6 - NEVER_CONNECTED_TOO_LONG - player took too long to connect, no abandon.
    private int mGold;
    private int mLastHits;
    private int mDenies;
    private int mGPM; // gold per minute
    private int mXPM; // xp per minute
    private int mGoldSpent;
    private int mHeroDamage;
    private int mTowerDamage;
    private int mHeroHealing;
    private int mLevel;
    private MatchDetailPlayerUnit mMatchDetailPlayerUnit;

    public MatchDetailPlayer(
            long matchId, long accountId, int heroId, int[] items,
                             int kills, int deaths, int assists,
                             int leaverStatus, int gold, int lastHits,
                             int denies, int GPM, int XPM, int goldSpent,
                             int heroDamage, int towerDamage, int heroHealing,
                             int level, MatchDetailPlayerUnit matchDetailPlayerUnit) {
        mMatchId = matchId;
        mAccountId = accountId;
        mHeroId = heroId;
        mItems = items;
        mKills = kills;
        mDeaths = deaths;
        mAssists = assists;
        mLeaverStatus = leaverStatus;
        mGold = gold;
        mLastHits = lastHits;
        mDenies = denies;
        mGPM = GPM;
        mXPM = XPM;
        mGoldSpent = goldSpent;
        mHeroDamage = heroDamage;
        mTowerDamage = towerDamage;
        mHeroHealing = heroHealing;
        mLevel = level;
        mMatchDetailPlayerUnit = matchDetailPlayerUnit;
    }

    public MatchDetailPlayer() {

    }

    public long getMatchId() {
        return mMatchId;
    }

    public void setMatchId(long matchId) {
        mMatchId = matchId;
    }

    public long getAccountId() {
        return mAccountId;
    }

    public void setAccountId(long accountId) {
        mAccountId = accountId;
    }

    public int getHeroId() {
        return mHeroId;
    }

    public void setHeroId(int heroId) {
        mHeroId = heroId;
    }

    public int[] getItems() {
        return mItems;
    }

    public void setItems(int[] items) {
        mItems = items;
    }

    public int getKills() {
        return mKills;
    }

    public void setKills(int kills) {
        mKills = kills;
    }

    public int getDeaths() {
        return mDeaths;
    }

    public void setDeaths(int deaths) {
        mDeaths = deaths;
    }

    public int getAssists() {
        return mAssists;
    }

    public void setAssists(int assists) {
        mAssists = assists;
    }

    public int getLeaverStatus() {
        return mLeaverStatus;
    }

    public void setLeaverStatus(int leaverStatus) {
        mLeaverStatus = leaverStatus;
    }

    public int getGold() {
        return mGold;
    }

    public void setGold(int gold) {
        mGold = gold;
    }

    public int getLastHits() {
        return mLastHits;
    }

    public void setLastHits(int lastHits) {
        mLastHits = lastHits;
    }

    public int getDenies() {
        return mDenies;
    }

    public void setDenies(int denies) {
        mDenies = denies;
    }

    public int getGPM() {
        return mGPM;
    }

    public void setGPM(int GPM) {
        mGPM = GPM;
    }

    public int getXPM() {
        return mXPM;
    }

    public void setXPM(int XPM) {
        mXPM = XPM;
    }

    public int getGoldSpent() {
        return mGoldSpent;
    }

    public void setGoldSpent(int goldSpent) {
        mGoldSpent = goldSpent;
    }

    public int getHeroDamage() {
        return mHeroDamage;
    }

    public void setHeroDamage(int heroDamage) {
        mHeroDamage = heroDamage;
    }

    public int getTowerDamage() {
        return mTowerDamage;
    }

    public void setTowerDamage(int towerDamage) {
        mTowerDamage = towerDamage;
    }

    public int getHeroHealing() {
        return mHeroHealing;
    }

    public void setHeroHealing(int heroHealing) {
        mHeroHealing = heroHealing;
    }

    public int getLevel() {
        return mLevel;
    }

    public void setLevel(int level) {
        mLevel = level;
    }

    public MatchDetailPlayerUnit getMatchDetailPlayerUnit() {
        return mMatchDetailPlayerUnit;
    }

    public void setMatchDetailPlayerUnit(MatchDetailPlayerUnit matchDetailPlayerUnit) {
        mMatchDetailPlayerUnit = matchDetailPlayerUnit;
    }

    private final String TAG = "MatchDetailPlayer";

    public void saveToDB(SQLController dbc) {
        String insertStatement = "INSERT INTO match_detail_player (match_id, account_id, hero_id, level, " +
                "kills, deaths, assists, leaver_status, " +
                "gold, last_hits, denies, gpm, " +
                "xpm, gold_spent, hero_damage, tower_damage, " +
                "hero_healing, slot_zero, slot_one, slot_two, " +
                "slot_three, slot_four, slot_five) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            Connection connection = dbc.database();
            PreparedStatement preparedStatement = connection.prepareStatement(insertStatement);
            preparedStatement.setLong(1, mMatchId);
            preparedStatement.setLong(2, mAccountId);
            preparedStatement.setInt(3, mHeroId);
            preparedStatement.setInt(4, mLevel);
            preparedStatement.setInt(5, mKills);
            preparedStatement.setInt(6, mDeaths);
            preparedStatement.setInt(7, mAssists);
            preparedStatement.setInt(8, mLeaverStatus);
            preparedStatement.setInt(9, mGold);
            preparedStatement.setInt(10, mLastHits);
            preparedStatement.setInt(11, mDenies);
            preparedStatement.setInt(12, mGPM);
            preparedStatement.setInt(13, mXPM);
            preparedStatement.setInt(14, mGoldSpent);
            preparedStatement.setInt(15, mHeroDamage);
            preparedStatement.setInt(16, mTowerDamage);
            preparedStatement.setInt(17, mHeroHealing);

            for (int i = 0; i < mItems.length; i++) {
                preparedStatement.setInt(18 + i, mItems[i]);
            }

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "Error inserting into table \"match_detail_player\"");
        }

    }

    public static class Model extends SQLController.LocalDataBaseModel {
        private static final String TAG = "MatchDetailPlayer.Model";

        public Model() {
            super();
        }

        @Override
        public void createTable(Connection connection) {
            String createStatement = "CREATE TABLE IF NOT EXISTS match_detail_player(id INTEGER PRIMARY KEY NOT NULL, "
                    + "match_id INTEGER NOT NULL, "
                    + "account_id INTEGER NOT NULL, "
                    + "hero_id INTEGER NOT NULL, "
                    + "level INTEGER NOT NULL, "
                    + "kills INTEGER NOT NULL, "
                    + "deaths INTEGER NOT NULL, "
                    + "assists INTEGER NOT NULL, "
                    + "leaver_status INTEGER NOT NULL, "
                    + "gold INTEGER NOT NULL, "
                    + "last_hits INTEGER NOT NULL, "
                    + "denies INTEGER NOT NULL, "
                    + "gpm INTEGER NOT NULL, "
                    + "xpm INTEGER NOT NULL, "
                    + "gold_spent INTEGER NOT NULL, "
                    + "hero_damage INTEGER NOT NULL, "
                    + "tower_damage INTEGER NOT NULL, "
                    + "hero_healing INTEGER NOT NULL, "
                    + "slot_zero INTEGER NOT NULL, "
                    + "slot_one INTEGER NOT NULL, "
                    + "slot_two INTEGER NOT NULL, "
                    + "slot_three INTEGER NOT NULL, "
                    + "slot_four INTEGER NOT NULL, "
                    + "slot_five INTEGER NOT NULL"
                    + ");";

            try {
                Statement statement = connection.createStatement();
                statement.execute(createStatement);
            } catch (SQLException e) {
                Logger.getLogger(TAG).log(Level.SEVERE, "Error creating table \"match_detail_player\"");
            }
        }

        @Override
        public void deleteTable(Connection connection) {
            String deleteStatement = "DELETE FROM match_detail_player";
            try {
                Statement statement = connection.createStatement();
                statement.execute(deleteStatement);
            } catch (SQLException e) {
                Logger.getLogger(TAG).log(Level.SEVERE, "Error deleting table \"match_detail_player\"");
            }
        }
    }
}
