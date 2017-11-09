package edu.orangecoastcollege.cs273.dotaweb;

import edu.orangecoastcollege.cs273.api.APIRequest;
import edu.orangecoastcollege.cs273.model.Hero;
import edu.orangecoastcollege.cs273.model.MatchID;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class HelloWorldController {
    private static final String TAG = "HelloWorldController";
    private APIRequest mAPIRequest = new APIRequest();

    @RequestMapping("/")
    public String index() {
        Logger.getLogger(TAG).log(Level.INFO, "Query call to /index");
        return "Greetings from Spring Boot!\n";
    }

    @RequestMapping("/vincent")
    public String vincent() {
        Logger.getLogger(TAG).log(Level.INFO, "Query call to /vincent");
        return "Vincent was here!\n";
    }

    @RequestMapping("/derek")
    public String test() {
        Logger.getLogger(TAG).log(Level.INFO, "Query call to /derek");
        return "Derek was here!\n";
    }

    @RequestMapping("/vanityto64")
    public String vanityto64() {
        Logger.getLogger(TAG).log(Level.INFO, "Attempting to get 64bit SteamId from Vanity URL");
        return mAPIRequest.get64FromVanity("system787");
    }

    @RequestMapping("/getlast25")
    public String getLast25() {
        return "Hello World!";
    }

    @RequestMapping("/getallheroes")
    public String getAllHeroes() {
        Logger.getLogger(TAG).log(Level.INFO, "Attempting to retrieve all heroes from master server");
        HashMap<Integer, Hero> heroesHashMap = mAPIRequest.getAllHeroes();

        StringBuilder sb = new StringBuilder();

        for (Hero h : heroesHashMap.values()) {
            String output = h.getId() + " " + h.getTokenName() + " " + h.getLocalizedName() + "\n";
            sb.append(output);
        }

        return sb.toString();
    }

}
