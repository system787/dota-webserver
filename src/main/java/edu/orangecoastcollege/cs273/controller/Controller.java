package edu.orangecoastcollege.cs273.controller;

public class Controller {
    private static Controller mController;
    private static SQLController mSQLController;

    private Controller() {

    }

    public static Controller getInstance() {
        if (mController == null) {
            mController = new Controller();
        }

        if (mSQLController == null) {
            mSQLController = SQLController.getInstance();
        }

        return mController;
    }

}
