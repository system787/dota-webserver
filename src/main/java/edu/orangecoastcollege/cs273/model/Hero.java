package edu.orangecoastcollege.cs273.model;

import edu.orangecoastcollege.cs273.controller.SQLController;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Hero {
    private int mId;
    private String mTokenName;
    private String mLocalizedName;

    public Hero(int id, String tokenName, String localizedName) {
        mId = id;
        mTokenName = tokenName;
        mLocalizedName = localizedName;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getTokenName() {
        return mTokenName;
    }

    public void setTokenName(String tokenName) {
        mTokenName = tokenName;
    }

    public String getLocalizedName() {
        return mLocalizedName;
    }

    public void setLocalizedName(String localizedName) {
        mLocalizedName = localizedName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hero hero = (Hero) o;

        if (mId != hero.mId) return false;
        if (mTokenName != null ? !mTokenName.equals(hero.mTokenName) : hero.mTokenName != null) return false;
        return mLocalizedName != null ? mLocalizedName.equals(hero.mLocalizedName) : hero.mLocalizedName == null;
    }

    @Override
    public int hashCode() {
        int result = mId;
        result = 31 * result + (mTokenName != null ? mTokenName.hashCode() : 0);
        result = 31 * result + (mLocalizedName != null ? mLocalizedName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Hero{" +
                "mId=" + mId +
                ", mTokenName='" + mTokenName + '\'' +
                ", mLocalizedName='" + mLocalizedName + '\'' +
                '}';
    }

    /* Database Methods */
    private static final String TAG = "Hero";

    public void saveToDB(SQLController dbc) {
        String insertStatement = "INSERT INTO heroes(id, token_name, localized_name) VALUES(?,?,?)";
        try {
            Connection connection = dbc.database();
            PreparedStatement preparedStatement = connection.prepareStatement(insertStatement);
            preparedStatement.setInt(1, mId);
            preparedStatement.setString(2, mTokenName);
            preparedStatement.setString(3, mLocalizedName);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "Error inserting into table \"heroes\"", e);
        }
    }

    public static List<Hero> getAllHeroes(SQLController dbc) {
        String selectStatement = "SELECT * FROM heroes";

        try {
            Connection connection = dbc.database();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectStatement);
            List<Hero> heroList = new ArrayList<>();
            while (resultSet.next()) {
                heroList.add(new Hero(resultSet.getInt("id"),
                        resultSet.getString("token_name"),
                        resultSet.getString("localized_name")));
            }
            return heroList;
        } catch (SQLException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "Error retrieving table \"heroes\"");
        }
        return null;
    }

    public static void deleteTable(SQLController dbc) {
        String deleteStatement = "DELETE FROM heroes";

        try {
            Connection connection = dbc.database();
            Statement statement = connection.createStatement();
            statement.executeUpdate(deleteStatement);
        } catch (SQLException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "Error deleting table \"heroes\"");
        }
    }


    public static class Model extends SQLController.LocalDataBaseModel {
        public Model() {
            super();
        }

        @Override
        public void createTable(Connection connection) {
            String createStatement = "CREATE TABLE IF NOT EXISTS heroes(id INTEGER PRIMARY KEY NOT NULL, "
                    + "token_name TEXT NOT NULL, "
                    + "localized_name TEXT NOT NULL);";

            try {
                Statement statement = connection.createStatement();
                statement.execute(createStatement);
            } catch (SQLException e) {
                Logger.getLogger("Hero.Model").log(Level.SEVERE, "Error creating table \"heroes\"");
            }
        }

        @Override
        public void deleteTable(Connection connection) {
            String deleteStatement = "DELETE FROM heroes";
            try {
                Statement statement = connection.createStatement();
                statement.execute(deleteStatement);
            } catch (SQLException e) {
                Logger.getLogger("Hero.Model").log(Level.SEVERE, "Error deleting table \"heroes\"");
            }
        }
    }
}
