package edu.orangecoastcollege.cs273.controller;

import edu.orangecoastcollege.cs273.model.*;

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
            new Hero.Model(), new MatchID.Model(), new User.Model(),
            new MatchDetails.Model(), new MatchDetailPlayer.Model(), new MatchDetailPlayerUnit.Model()
    };

    public synchronized static SQLController getInstance() {
        try {
            if (instance == null) {
                instance = new SQLController();

                instance.openConnection();
                Connection connection = instance.database();
                instance.createDB(connection);
                instance.close();
            }

            return instance;
        } catch (SQLException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "UNABLE TO CREATE SQLController INSTANCE", e);
        }
        return null;
    }

    private SQLController() {

    }

    public synchronized void createDB(Connection connection) {
        for (LocalDataBaseModel model : models) {
            model.createTable(connection);
        }
    }

    public synchronized boolean resetAllTables() {
        try {
            if (mConnection.isClosed()) {
                instance.openConnection();
            }
            Connection connection = instance.database();
            for (LocalDataBaseModel model : models) {
                model.deleteTable(connection);
            }
            createDB(connection);
            instance.close();

            return true;
        } catch (SQLException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, "Error deleting all tables from local database models");
        }

        return false;
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

