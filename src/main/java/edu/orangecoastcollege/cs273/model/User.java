package edu.orangecoastcollege.cs273.model;

import edu.orangecoastcollege.cs273.controller.SQLController;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class User {
    private String mSteamId64;
    private int mPrivacy; // 1 - private; 2 - Friends only; 3 - Friends of Friends; 4 - Users Only; 5 - Public
    private int mProfileState; // 0 - profile not configured; 1 - user has configured profile
    private String mPersonaName;
    private String mLastLogOff;
    private String mProfileUrl;
    private String mAvatarUrl;

    public User(String steamId64, int privacy, int profileState, String personaName, String lastLogOff, String profileUrl, String avatarUrl) {
        mSteamId64 = steamId64;
        mPrivacy = privacy;
        mProfileState = profileState;
        mPersonaName = personaName;
        mLastLogOff = lastLogOff;
        mProfileUrl = profileUrl;
        mAvatarUrl = avatarUrl;
    }

    public String getSteamId64() {
        return mSteamId64;
    }

    public void setSteamId64(String steamId64) {
        mSteamId64 = steamId64;
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

    public String getLastLogOff() {
        return mLastLogOff;
    }

    public void setLastLogOff(String lastLogOff) {
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

        if (mPrivacy != user.mPrivacy) return false;
        if (mProfileState != user.mProfileState) return false;
        if (mSteamId64 != null ? !mSteamId64.equals(user.mSteamId64) : user.mSteamId64 != null) return false;
        if (mPersonaName != null ? !mPersonaName.equals(user.mPersonaName) : user.mPersonaName != null) return false;
        if (mLastLogOff != null ? !mLastLogOff.equals(user.mLastLogOff) : user.mLastLogOff != null) return false;
        if (mProfileUrl != null ? !mProfileUrl.equals(user.mProfileUrl) : user.mProfileUrl != null) return false;
        return mAvatarUrl != null ? mAvatarUrl.equals(user.mAvatarUrl) : user.mAvatarUrl == null;
    }

    @Override
    public int hashCode() {
        int result = mSteamId64 != null ? mSteamId64.hashCode() : 0;
        result = 31 * result + mPrivacy;
        result = 31 * result + mProfileState;
        result = 31 * result + (mPersonaName != null ? mPersonaName.hashCode() : 0);
        result = 31 * result + (mLastLogOff != null ? mLastLogOff.hashCode() : 0);
        result = 31 * result + (mProfileUrl != null ? mProfileUrl.hashCode() : 0);
        result = 31 * result + (mAvatarUrl != null ? mAvatarUrl.hashCode() : 0);
        return result;
    }

    /* Database Methods */
    private static final String TAG = "User";

    public void saveToDB(SQLController dbc) {
        String insertStatement = "INSERT INTO users(steam_id, privacy, profile_state, persona_name, last_log_off, profile_url, avatar_url) VALUES(?,?,?,?,?,?,?)";

        try {
            Connection connection = dbc.database();
            PreparedStatement preparedStatement = connection.prepareStatement(insertStatement);
            preparedStatement.setString(1, mSteamId64);
            preparedStatement.setInt(2, mPrivacy);
            preparedStatement.setInt(3, mProfileState);
            preparedStatement.setString(4, mPersonaName);
            preparedStatement.setString(5, mLastLogOff);
            preparedStatement.setString(6, mProfileUrl);
            preparedStatement.setString(7, mAvatarUrl);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "Error inserting into table \"users\"");
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
            preparedStatement.setString(4, mLastLogOff);
            preparedStatement.setString(5, mProfileUrl);
            preparedStatement.setString(6, mAvatarUrl);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "Error updating user " + mSteamId64);
        }
    }

    public static HashMap<String, User> getAllUsers(SQLController dbc) {
        String selectStatement = "SELECT * FROM users";

        try {
            Connection connection = dbc.database();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectStatement);
            HashMap<String, User> userHashMap = new HashMap<>();

            while (resultSet.next()) {
                String steamId = resultSet.getString("steam_id");
                userHashMap.put(steamId, new User(
                        steamId,
                        resultSet.getInt("privacy"),
                        resultSet.getInt("profile_state"),
                        resultSet.getString("persona_name"),
                        resultSet.getString("last_log_off"),
                        resultSet.getString("profile_url"),
                        resultSet.getString("avatar_url")
                ));
            }

            return userHashMap;
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
                    + "steam_id TEXT NOT NULL, "
                    + "privacy INTEGER NOT NULL, "
                    + "profile_state INTEGER NOT NULL, "
                    + "persona_name TEXT NOT NULL, "
                    + "last_log_off TEXT NOT NULL, "
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
