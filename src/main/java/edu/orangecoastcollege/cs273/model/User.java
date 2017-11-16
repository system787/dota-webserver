package edu.orangecoastcollege.cs273.model;

import edu.orangecoastcollege.cs273.controller.SQLController;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class User {
    private long mSteamId32;
    private int mPrivacy; // 1 - private; 2 - Friends only; 3 - Friends of Friends; 4 - Users Only; 5 - Public
    private int mProfileState; // 0 - profile not configured; 1 - user has configured profile
    private String mPersonaName;
    private long mLastLogOff;
    private String mProfileUrl;
    private String mAvatarUrl;

    public User(long steamId32, int privacy, int profileState, String personaName, long lastLogOff, String profileUrl, String avatarUrl) {
        mSteamId32 = steamId32;
        mPrivacy = privacy;
        mProfileState = profileState;
        mPersonaName = personaName;
        mLastLogOff = lastLogOff;
        mProfileUrl = profileUrl;
        mAvatarUrl = avatarUrl;
    }

    public long getSteamId32() {
        return mSteamId32;
    }

    public void setSteamId32(long steamId32) {
        mSteamId32 = steamId32;
    }

    public long getSteamId64() {
        return mSteamId32 + 76561197960265728L;
    }

    public int getPrivacy() {
        return mPrivacy;
    }

    public void setPrivacy(int privacy) {
        mPrivacy = privacy;
    }

    public int getProfileState() {
        return mProfileState;
    }

    public void setProfileState(int profileState) {
        mProfileState = profileState;
    }

    public String getPersonaName() {
        return mPersonaName;
    }

    public void setPersonaName(String personaName) {
        mPersonaName = personaName;
    }

    public long getLastLogOff() {
        return mLastLogOff;
    }

    public void setLastLogOff(long lastLogOff) {
        mLastLogOff = lastLogOff;
    }

    public String getProfileUrl() {
        return mProfileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        mProfileUrl = profileUrl;
    }

    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        mAvatarUrl = avatarUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (mSteamId32 != user.mSteamId32) return false;
        if (mPrivacy != user.mPrivacy) return false;
        if (mProfileState != user.mProfileState) return false;
        if (mLastLogOff != user.mLastLogOff) return false;
        if (mPersonaName != null ? !mPersonaName.equals(user.mPersonaName) : user.mPersonaName != null) return false;
        if (mProfileUrl != null ? !mProfileUrl.equals(user.mProfileUrl) : user.mProfileUrl != null) return false;
        return mAvatarUrl != null ? mAvatarUrl.equals(user.mAvatarUrl) : user.mAvatarUrl == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (mSteamId32 ^ (mSteamId32 >>> 32));
        result = 31 * result + mPrivacy;
        result = 31 * result + mProfileState;
        result = 31 * result + (mPersonaName != null ? mPersonaName.hashCode() : 0);
        result = 31 * result + (int) (mLastLogOff ^ (mLastLogOff >>> 32));
        result = 31 * result + (mProfileUrl != null ? mProfileUrl.hashCode() : 0);
        result = 31 * result + (mAvatarUrl != null ? mAvatarUrl.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "mSteamId32=" + mSteamId32 +
                ", mPrivacy=" + mPrivacy +
                ", mProfileState=" + mProfileState +
                ", mPersonaName='" + mPersonaName + '\'' +
                ", mLastLogOff=" + mLastLogOff +
                ", mProfileUrl='" + mProfileUrl + '\'' +
                ", mAvatarUrl='" + mAvatarUrl + '\'' +
                '}';
    }

    /* Database Methods */
    private static final String TAG = "User";

    public void saveToDB(SQLController dbc) {
        String insertStatement = "INSERT INTO users(steam_id, privacy, profile_state, persona_name, last_log_off, profile_url, avatar_url) VALUES(?,?,?,?,?,?,?)";

        try {
            Connection connection = dbc.database();
            PreparedStatement preparedStatement = connection.prepareStatement(insertStatement);
            preparedStatement.setLong(1, mSteamId32);
            preparedStatement.setInt(2, mPrivacy);
            preparedStatement.setInt(3, mProfileState);
            preparedStatement.setString(4, mPersonaName);
            preparedStatement.setLong(5, mLastLogOff);
            preparedStatement.setString(6, mProfileUrl);
            preparedStatement.setString(7, mAvatarUrl);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "Error inserting into table \"users\"", e);
        }
    }

    public void updateUser(SQLController dbc, User user) {
        String updateStatement = "UPDATE users SET  "
                + "privacy = ? , " // 1
                + "profile_state = ? , " // 2
                + "persona_name = ? , " // 3
                + "last_log_off = ? , " // 4
                + "profile_url = ? , " // 5
                + "avatar_url = ? " // 6
                + "WHERE steam_id = ?"; // 7
        try {
            Connection connection = dbc.database();
            PreparedStatement preparedStatement = connection.prepareStatement(updateStatement);
            preparedStatement.setInt(1, mPrivacy);
            preparedStatement.setInt(2, mProfileState);
            preparedStatement.setString(3, mPersonaName);
            preparedStatement.setLong(4, mLastLogOff);
            preparedStatement.setString(5, mProfileUrl);
            preparedStatement.setString(6, mAvatarUrl);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "Error updating user " + mSteamId32);
        }
    }

    public static List<User> getAllUsers(SQLController dbc) {
        String selectStatement = "SELECT * FROM users";

        try {
            Connection connection = dbc.database();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectStatement);
            List<User> userList = new ArrayList<>();

            while (resultSet.next()) {
                userList.add(new User(
                        resultSet.getLong("steam_id"),
                        resultSet.getInt("privacy"),
                        resultSet.getInt("profile_state"),
                        resultSet.getString("persona_name"),
                        resultSet.getLong("last_log_off"),
                        resultSet.getString("profile_url"),
                        resultSet.getString("avatar_url")
                ));
            }

            return userList;
        } catch (SQLException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "Error retrieving table \"users\"");
        }
        return null;
    }

    public static class Model extends SQLController.LocalDataBaseModel {
        public Model() {
            super();
        }

        @Override
        public void createTable(Connection connection) {
            String createStatement = "CREATE TABLE IF NOT EXISTS users(id INTEGER PRIMARY KEY NOT NULL, "
                    + "steam_id INTEGER NOT NULL,"
                    + "privacy INTEGER NOT NULL, "
                    + "profile_state INTEGER NOT NULL, "
                    + "persona_name TEXT NOT NULL, "
                    + "last_log_off INTEGER NOT NULL, "
                    + "profile_url TEXT NOT NULL, "
                    + "avatar_url TEXT NOT NULL);";

            try {
                Statement statement = connection.createStatement();
                statement.execute(createStatement);
            } catch (SQLException e) {
                Logger.getLogger("User.Model").log(Level.SEVERE, "Error creating table \"users\"");
            }
        }

        @Override
        public void deleteTable(Connection connection) {
            String deleteStatement = "DELETE FROM users";
            try {
                Statement statement = connection.createStatement();
                statement.execute(deleteStatement);
            } catch (SQLException e) {
                Logger.getLogger("User.Model").log(Level.SEVERE, "Error deleting table \"users\"");
            }
        }
    }
}
