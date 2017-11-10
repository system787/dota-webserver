package edu.orangecoastcollege.cs273.api;

import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class APIKey {

    public static String getAPIKey() {
        Properties properties = new Properties();
        try {
            //Loads properties from properties file
            properties.load(new FileInputStream(new File("api_key.properties")));
            //Grabs the first and only key, uses it to fetch property
            return properties.getProperty((String) properties.keys().nextElement());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getAPIDomain() {
        return "systemdota";
    }
}
