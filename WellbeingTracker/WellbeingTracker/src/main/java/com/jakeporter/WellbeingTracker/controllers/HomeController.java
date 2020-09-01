package com.jakeporter.WellbeingTracker.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@CrossOrigin
public class HomeController {
    @GetMapping({"/", "/home"})
    public String displayHomePage() {
        return "home";
    }

}
