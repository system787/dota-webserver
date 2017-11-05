package edu.orangecoastcollege.cs273.dotaweb;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class HelloWorldController {

    @RequestMapping("/")
    public String index() {
        Logger.getLogger("HelloWorldController").log(Level.INFO, "index() - Query call from terminal");
        return "Greetings from Spring Boot!\n";
    }

    @RequestMapping("/vincent")
    public String test() {
        return "Vincent was here!\n";
    }

}
