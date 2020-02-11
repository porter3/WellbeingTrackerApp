package com.jakeporter.WellbeingTrackerAPI.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author jake
 */

@Controller
public class HomeController {

    @GetMapping({"/", "/home"})
    public String displayHomePage() {
        
        return "home";
    }
}
