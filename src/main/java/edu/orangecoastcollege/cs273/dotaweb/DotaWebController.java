package edu.orangecoastcollege.cs273.dotaweb;

import edu.orangecoastcollege.cs273.api.APIRequest;
import edu.orangecoastcollege.cs273.controller.Controller;
import edu.orangecoastcollege.cs273.model.MatchDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @RequestMapping(value = "/dotaweb/register", method = RequestMethod.POST)
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
        long steamId = Long.parseLong(steamId32);


        List<MatchDetails> matchDetailsList = mController.getLatestMatchesSingleUser(steamId);

        if (matchDetailsList == null) {
            return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY).toString();
        }

        return mController.toGson(matchDetailsList);
    }

}
