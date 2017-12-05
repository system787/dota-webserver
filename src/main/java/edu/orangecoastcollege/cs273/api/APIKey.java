package edu.orangecoastcollege.cs273.api;

import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class APIKey {

    public static String getAPIKey() {
        String apiKey = "apiKey";
        return propertiesReader(apiKey);
    }

    public static String getAPIKey2() {
        String apiKey = "apiKey2";
        return propertiesReader(apiKey);
    }

    public static String getAPIKey3() {
        String apiKey = "apiKey3";
        return propertiesReader(apiKey);
    }

    private static String propertiesReader(String apiKey) {
        Properties properties = new Properties();
        try {
            //Loads properties from properties file
            properties.load(new FileInputStream(new File("gradle.properties")));
            //Grabs the first and only key, uses it to fetch property
            return properties.getProperty(apiKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getAPIDomain() {
        return "systemdota";
    }
}
