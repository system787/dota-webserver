package edu.orangecoastcollege.cs273.dotaweb;

import edu.orangecoastcollege.cs273.api.APIRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DotaWebController {
    private static final String TAG = "DotaWebController";
    private APIRequest mAPIRequest = new APIRequest();


    @RequestMapping("/")
    public String index() {
        return "DotaWebController";
    }

}
