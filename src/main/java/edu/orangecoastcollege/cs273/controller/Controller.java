package edu.orangecoastcollege.cs273.controller;

public class Controller {
    private static Controller mController;

    private Controller() {

    }

    public static Controller getInstance() {
        if (mController == null) {
            mController = new Controller();
        }

        return mController;
    }
}
