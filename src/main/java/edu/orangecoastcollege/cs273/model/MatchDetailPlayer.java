package edu.orangecoastcollege.cs273.model;

import edu.orangecoastcollege.cs273.controller.SQLController;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MatchDetailPlayer {
    private long mMatchId;
    private long mAccountId;
    private int mPlayerSlot;
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
            long matchId, long accountId, int playerSlot, int heroId, int[] items,
            int kills, int deaths, int assists,
            int leaverStatus, int gold, int lastHits,
            int denies, int GPM, int XPM, int goldSpent,
            int heroDamage, int towerDamage, int heroHealing,
            int level, MatchDetailPlayerUnit matchDetailPlayerUnit) {
        mMatchId = matchId;
        mAccountId = accountId;
        mPlayerSlot = playerSlot;
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

    public MatchDetailPlayer(long matchId, long accountId, int playerSlot, int heroId, int[] items,
                             int kills, int deaths, int assists, int leaverStatus,
                             int gold, int lastHits, int denies, int gpm, int xpm,
                             int goldSpent, int heroDamage, int towerDamage, int heroHealing,
                             int level) {
        mMatchId = matchId;
        mAccountId = accountId;
        mPlayerSlot = playerSlot;
        mHeroId = heroId;
        mItems = items;
        mKills = kills;
        mDeaths = deaths;
        mAssists = assists;
        mLeaverStatus = leaverStatus;
        mGold = gold;
        mLastHits = lastHits;
        mDenies = denies;
        mGPM = gpm;
        mXPM = xpm;
        mGoldSpent = goldSpent;
        mHeroDamage = heroDamage;
        mTowerDamage = towerDamage;
        mHeroHealing = heroHealing;
        mLevel = level;
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

    public int getPlayerSlot() {
        return mPlayerSlot;
    }

    public void setPlayerSlot(int playerSlot) {
        this.mPlayerSlot = playerSlot;
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
        // if (mMatchDetailPlayerUnit == null) {
        //     Logger.getLogger(TAG).log(Level.SEVERE,"Player " + String.valueOf(mAccountId) + " does not have a unit");
        // }

        return mMatchDetailPlayerUnit;
    }

    public void setMatchDetailPlayerUnit(MatchDetailPlayerUnit matchDetailPlayerUnit) {
        mMatchDetailPlayerUnit = matchDetailPlayerUnit;
    }

    private static final String TAG = "MatchDetailPlayer";

    public void saveToDB(SQLController dbc) {
        String insertStatement = "INSERT INTO match_detail_player (match_id, account_id, player_slot, hero_id, player_level, " +
                "kills, deaths, assists, leaver_status, " +
                "gold, last_hits, denies, gpm, " +
                "xpm, gold_spent, hero_damage, tower_damage, " +
                "hero_healing, slot_zero, slot_one, slot_two, " +
                "slot_three, slot_four, slot_five) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            Connection connection = dbc.database();
            PreparedStatement preparedStatement = connection.prepareStatement(insertStatement);
            preparedStatement.setLong(1, mMatchId);
            preparedStatement.setLong(2, mAccountId);
            preparedStatement.setInt(3, mPlayerSlot);
            preparedStatement.setInt(4, mHeroId);
            preparedStatement.setInt(5, mLevel);
            preparedStatement.setInt(6, mKills);
            preparedStatement.setInt(7, mDeaths);
            preparedStatement.setInt(8, mAssists);
            preparedStatement.setInt(9, mLeaverStatus);
            preparedStatement.setInt(10, mGold);
            preparedStatement.setInt(11, mLastHits);
            preparedStatement.setInt(12, mDenies);
            preparedStatement.setInt(13, mGPM);
            preparedStatement.setInt(14, mXPM);
            preparedStatement.setInt(15, mGoldSpent);
            preparedStatement.setInt(16, mHeroDamage);
            preparedStatement.setInt(17, mTowerDamage);
            preparedStatement.setInt(18, mHeroHealing);

            for (int i = 0; i < mItems.length; i++) {
                preparedStatement.setInt(19 + i, mItems[i]);
            }

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "Error inserting into table \"match_detail_player\"");
        }

    }

    public static MatchDetailPlayer getPlayerDetail(SQLController dbc, long matchId, long steamId) {
        String selectStatement = "SELECT * FROM match_detail_player WHERE (match_id =" + matchId + ") AND (steam_id =" + steamId + ")";

        try {
            Connection connection = dbc.database();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectStatement);
            MatchDetailPlayerUnit playerUnit = MatchDetailPlayerUnit.getPlayerUnit(dbc, matchId, steamId);

            int[] itemArray = new int[6];
            itemArray[0] = resultSet.getInt("slot_zero");
            itemArray[1] = resultSet.getInt("slot_one");
            itemArray[2] = resultSet.getInt("slot_two");
            itemArray[3] = resultSet.getInt("slot_three");
            itemArray[4] = resultSet.getInt("slot_four");
            itemArray[5] = resultSet.getInt("slot_five");

            MatchDetailPlayer player = new MatchDetailPlayer(
                    resultSet.getLong("match_id"), resultSet.getLong("steam_id"), resultSet.getInt("hero_id"), resultSet.getInt("player_slot"), itemArray,
                    resultSet.getInt("kills"), resultSet.getInt("deaths"), resultSet.getInt("assists"), resultSet.getInt("leaver_status"),
                    resultSet.getInt("gold"), resultSet.getInt("last_hits"), resultSet.getInt("denies"), resultSet.getInt("gpm"),
                    resultSet.getInt("xpm"), resultSet.getInt("gold_spent"), resultSet.getInt("hero_damage"), resultSet.getInt("tower_damage"),
                    resultSet.getInt("hero_healing"), resultSet.getInt("player_level"), playerUnit);

            return player;
        } catch (SQLException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "Error while retrieving player " + steamId + " details in match " + matchId);
        }
        return null;
    }

    public static List<MatchDetailPlayer> getPlayerDetailsList(SQLController dbc, long matchId) {
        String selectStatement = "SELECT * FROM match_detail_player WHERE (match_id =" + matchId + ")";

        try {
            Connection connection = dbc.database();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectStatement);

            List<MatchDetailPlayer> playerDetailsList = new ArrayList<>();

            while (resultSet.next()) {
                long steamId = resultSet.getLong("steam_id");
                MatchDetailPlayerUnit playerUnit = MatchDetailPlayerUnit.getPlayerUnit(dbc, matchId, steamId);

                int[] itemArray = new int[6];
                itemArray[0] = resultSet.getInt("slot_zero");
                itemArray[1] = resultSet.getInt("slot_one");
                itemArray[2] = resultSet.getInt("slot_two");
                itemArray[3] = resultSet.getInt("slot_three");
                itemArray[4] = resultSet.getInt("slot_four");
                itemArray[5] = resultSet.getInt("slot_five");

                MatchDetailPlayer player = new MatchDetailPlayer(
                        resultSet.getLong("match_id"),
                        resultSet.getLong("steam_id"),
                        resultSet.getInt("player_slot"),
                        resultSet.getInt("hero_id"),
                        itemArray,
                        resultSet.getInt("kills"),
                        resultSet.getInt("deaths"),
                        resultSet.getInt("assists"),
                        resultSet.getInt("leaver_status"),
                        resultSet.getInt("gold"),
                        resultSet.getInt("last_hits"),
                        resultSet.getInt("denies"),
                        resultSet.getInt("gpm"),
                        resultSet.getInt("xpm"),
                        resultSet.getInt("gold_spent"),
                        resultSet.getInt("hero_damage"),
                        resultSet.getInt("tower_damage"),
                        resultSet.getInt("hero_healing"),
                        resultSet.getInt("player_level"),
                        playerUnit);
                playerDetailsList.add(player);
            }

            return playerDetailsList;
        } catch (SQLException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "Error while retrieving player details list in match " + matchId);
        }
        return null;
    }

    /*
        public MatchDetailPlayer(
            long matchId, long accountId, int heroId, int[] items,
                             int kills, int deaths, int assists,
                             int leaverStatus, int gold, int lastHits,
                             int denies, int GPM, int XPM, int goldSpent,
                             int heroDamage, int towerDamage, int heroHealing,
                             int level, MatchDetailPlayerUnit matchDetailPlayerUnit)
     */

    public static class Model extends SQLController.LocalDataBaseModel {
        private static final String TAG = "MatchDetailPlayer.Model";

        public Model() {
            super();
        }

        @Override
        public void createTable(Connection connection) {
            String createStatement = "CREATE TABLE IF NOT EXISTS match_detail_player("
                    + "match_id BIGINT NOT NULL, "
                    + "account_id BIGINT NOT NULL, "
                    + "player_slot INT NOT NULL, "
                    + "hero_id INT NOT NULL, "
                    + "player_level INT NOT NULL, "
                    + "kills INT NOT NULL, "
                    + "deaths INT NOT NULL, "
                    + "assists INT NOT NULL, "
                    + "leaver_status INT NOT NULL, "
                    + "gold INT NOT NULL, "
                    + "last_hits INT NOT NULL, "
                    + "denies INT NOT NULL, "
                    + "gpm INT NOT NULL, "
                    + "xpm INT NOT NULL, "
                    + "gold_spent INT NOT NULL, "
                    + "hero_damage INT NOT NULL, "
                    + "tower_damage INT NOT NULL, "
                    + "hero_healing INT NOT NULL, "
                    + "slot_zero INT NOT NULL, "
                    + "slot_one INT NOT NULL, "
                    + "slot_two INT NOT NULL, "
                    + "slot_three INT NOT NULL, "
                    + "slot_four INT NOT NULL, "
                    + "slot_five INT NOT NULL,"
                    + "UNIQUE (match_id, account_id)"
                    + ");";
            try {
                Statement statement = connection.createStatement();
                statement.execute(createStatement);
            } catch (SQLException e) {
                Logger.getLogger(TAG).log(Level.SEVERE, "Error creating table \"match_detail_player\"", e);
            }
        }

        @Override
        public void deleteTable(Connection connection) {
            String deleteStatement = "DELETE FROM match_detail_player";
            try {
                Statement statement = connection.createStatement();
                statement.execute(deleteStatement);
            } catch (SQLException e) {
                Logger.getLogger(TAG).log(Level.SEVERE, "Error deleting table \"match_detail_player\"", e);
            }
        }
    }
}
