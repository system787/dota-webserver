package edu.orangecoastcollege.cs273.model;

import edu.orangecoastcollege.cs273.controller.SQLController;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
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
