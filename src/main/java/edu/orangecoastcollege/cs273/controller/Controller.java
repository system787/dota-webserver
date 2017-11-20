package edu.orangecoastcollege.cs273.controller;

import edu.orangecoastcollege.cs273.api.APIRequest;

public class Controller {
    private static final String TAG = "Controller";
    private static APIRequest mAPIRequest;
    private static Controller mController;
    private static SQLController mSQLController;

    // TODO: create MatchDetails class and MatchIDMatchDetails class

    private Controller() {

    }

    public static Controller getInstance() {
        if (mController == null) {
            mController = new Controller();
        }

        if (mSQLController == null) {
            mSQLController = SQLController.getInstance();
        }

        mAPIRequest = new APIRequest();

        return mController;
    }
}
