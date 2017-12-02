package edu.orangecoastcollege.cs273.dotaweb;

import edu.orangecoastcollege.cs273.api.APIRequest;
import edu.orangecoastcollege.cs273.controller.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
