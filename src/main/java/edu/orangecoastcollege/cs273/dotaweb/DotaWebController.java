package edu.orangecoastcollege.cs273.dotaweb;

import edu.orangecoastcollege.cs273.api.APIKey;
import edu.orangecoastcollege.cs273.api.APIRequest;
import edu.orangecoastcollege.cs273.controller.Controller;
import edu.orangecoastcollege.cs273.model.MatchDetailPlayer;
import edu.orangecoastcollege.cs273.model.MatchDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class DotaWebController {
    private static final String TAG = "DotaWebController";
    private static Controller mController = Controller.getInstance();


    @RequestMapping("/")
    public String index() {
        return "DotaWebController";
    }

    @RequestMapping(value = "/dotaweb/fetch/players", method = RequestMethod.GET)
    public String getPlayerSummaryById(@RequestParam("steamid") String steamId64) {
        long steamId = Long.parseLong(steamId64);

        return mController.toGson(mController.getUserSummaries(new long[]{steamId}));
    }

    @RequestMapping(value = "/dotaweb/register", method = { RequestMethod.POST, RequestMethod.GET })
    public ResponseEntity signUpNewUser(@RequestParam("steamId64") String steamId64) {
        long steamId = Long.parseLong(steamId64);

        boolean signUpSuccess = mController.signUpNewUser(steamId);

        if (signUpSuccess) {
            return new ResponseEntity(HttpStatus.ACCEPTED);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/dotaweb/fetch/refresh", method = RequestMethod.GET)
    public String getLatest25Matches(@RequestParam("steamId32") String steamId32) {
        Logger.getLogger(TAG).log(Level.INFO, "Refresh called with steamId32->"+steamId32);

        long steamId = Long.parseLong(steamId32);

        List<MatchDetails> matchDetailsList = mController.getLatestMatches(steamId);

        StringBuilder sb = new StringBuilder();

       if (matchDetailsList == null) {
           return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY).toString();
       }

       return mController.toGson(matchDetailsList);
    }

    @RequestMapping(value = "/dotaweb/test/reset", method = RequestMethod.GET)
    public String resetTables(@RequestParam("pw") String password) {
        if (password.equals(APIKey.getAPIKey())) {
            Logger.getLogger(TAG).log(Level.INFO, "Resetting all tables");
            mController.resetAllTables();
            return "Success";
        }
        return "Failed";
    }

}
