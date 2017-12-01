package edu.orangecoastcollege.cs273.dotaweb;

import edu.orangecoastcollege.cs273.api.APIRequest;
import edu.orangecoastcollege.cs273.model.Hero;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class DotaWebController {
    private static final String TAG = "DotaWebController";
    private APIRequest mAPIRequest = new APIRequest();


    @RequestMapping("/")
    public String index() {
        return "DotaWebController";
    }

    @RequestMapping("/getallheroes")
    public String getAllHeroes() {
        Logger.getLogger(TAG).log(Level.INFO, "Attempting to retrieve all heroes from master server");
        List<Hero> heroesList = mAPIRequest.getAllHeroes();
        Logger.getLogger(TAG).log(Level.INFO, "Data received from server");
        StringBuilder sb = new StringBuilder();

        for (Hero h : heroesList) {
            String output = h.getId() + " " + h.getTokenName() + " " + h.getLocalizedName() + "\n";
            sb.append(output);
        }

        Logger.getLogger(TAG).log(Level.INFO, "Processing complete");
        return sb.toString();
    }

}
