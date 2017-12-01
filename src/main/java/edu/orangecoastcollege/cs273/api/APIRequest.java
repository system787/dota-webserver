package edu.orangecoastcollege.cs273.api;

import edu.orangecoastcollege.cs273.model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class APIRequest {
    private static final String TAG = "APIRequest";

    public APIRequest() {
    }

    /**
     * @param url     request URL to send
     * @param timeout length of time in milliseconds until the request is canceled
     * @return a formatted JSON in String format
     * @throws JSONException logged for each exception seen below
     */
    public String getJSON(String url, int timeout) throws JSONException {
        HttpURLConnection c = null;
        try {
            Logger.getLogger(TAG).log(Level.INFO, "Building new HTTP connection");
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(timeout);
            c.setReadTimeout(timeout);
            Logger.getLogger(TAG).log(Level.INFO, "Attempting to connect to URL");
            c.connect();
            int status = c.getResponseCode();
            Logger.getLogger(TAG).log(Level.INFO, "Response code received: " + status);

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    return sb.toString();
                default:
                    Logger.getLogger(TAG).log(Level.SEVERE, "Error; Response code: " + status);
                    return null;
            }
        } catch (MalformedURLException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "MalformedURLException caught", e);
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "IOException caught", e);
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception e) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Unexpected Exception thrown", e);
                }
            }
        }
        return null;
    }

}
