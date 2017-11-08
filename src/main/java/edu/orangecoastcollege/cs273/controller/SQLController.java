package edu.orangecoastcollege.cs273.controller;

import edu.orangecoastcollege.cs273.model.Hero;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class SQLController {

    public static abstract class LocalDataBaseModel {
        public LocalDataBaseModel() {

        }

        public void createTable(Connection connection) {

        }

        public void deleteTable(Connection connection) {
            try {
                Statement stmt = connection.createStatement();
                stmt.executeUpdate("DELETE FROM steamid");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private Connection mConnection;
    private int openConnections = 0;

    private static final String TAG = "SQLController";
    private static final String DATABASE = "dotaweb.db";
    private static SQLController instance = null;

    private final LocalDataBaseModel[] models = new LocalDataBaseModel[]{
            // add local database models here
            // format { new Object.Model(), new Object2.Model() };
            new Hero.Model()
    };

    public synchronized static SQLController getInstance() throws SQLException {
        if (instance == null) {
            instance = new SQLController();

            instance.openConnection();
            Connection connection = instance.database();
            instance.createDB(connection);
            instance.close();
        }

        return instance;
    }

    private SQLController() {

    }

    public synchronized void createDB(Connection connection) {
        for (LocalDataBaseModel model : models) {
            model.createTable(connection);
        }
    }

    public synchronized Connection database() {
        if (mConnection == null) {
            throw new IllegalStateException("Database has not been opened yet!");
        }
        return mConnection;
    }

    /**
     * Must be called from the same thread as openConnection() call
     */
    public synchronized void close() {
        if (mConnection == null || openConnections == 0) {
            throw new IllegalStateException("Connection already closed or has never been opened.");
        }
        openConnections--;
        if (openConnections != 0) {
            return;
        }

        try {
            mConnection.close();
        } catch (SQLException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "mConnection unable to close");
        }
    }

    /**
     * Opens a connection to the SQL driver
     * <p>
     * ALways use close() after and use database() to get the opened connection
     */
    public synchronized void openConnection() throws SQLException {
        getNewConnection();
    }

    /**
     * Do not call this method, use openConnection() and database() instead
     */
    public synchronized Connection getNewConnection() throws SQLException {
        if (mConnection == null || mConnection.isClosed()) {
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            mConnection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE);
        }
        openConnections++;
        return mConnection;
    }
}

