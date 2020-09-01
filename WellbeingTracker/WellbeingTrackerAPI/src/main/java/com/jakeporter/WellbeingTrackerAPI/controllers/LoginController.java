package com.jakeporter.WellbeingTrackerAPI.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author jake
 */

@Controller
@CrossOrigin
public class LoginController {

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
}
