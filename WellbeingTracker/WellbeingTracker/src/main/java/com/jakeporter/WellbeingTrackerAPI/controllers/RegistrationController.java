package com.jakeporter.WellbeingTrackerAPI.controllers;

import com.jakeporter.WellbeingTrackerAPI.entities.UserAccount;
import com.jakeporter.WellbeingTrackerAPI.service.AddService;
import com.jakeporter.WellbeingTrackerAPI.service.ValidateService;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author jake
 */

@Controller
@CrossOrigin
public class RegistrationController {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    AddService addService;
    
    @Autowired
    ValidateService validateService;
    
    @Autowired
    private PasswordEncoder encoder;
    
    private Set<String> violations = new HashSet();
    
    @GetMapping("/signup")
    public String displaySignUp(Model model){
        if (!violations.isEmpty()){
            model.addAttribute("violations", violations);
        }
        return "signUp";
    }
    
    @PostMapping("/adduser")
    public String addUser(HttpServletRequest request, Model model){
        logger.debug("User creation attempt started");
        UserAccount user = addService.populateNewUserFromForm(request);
        violations.clear();
        validateService.validateNewAccountSettings(violations, user, request.getParameter("confirmPassword"));
       if (!violations.isEmpty()){
            logger.error("User creation rule violation");
            return "redirect:/signup";
        }

        // encrypt password
        String unencryptedPassword = user.getPassword();
        user.setPassword(encoder.encode(unencryptedPassword));

        addService.createNewAccount(user);
        logger.info("-- User created --\n ID: {}, NAME: {} {}, USERNAME: {}, EMAIL: {}, CREATION TIME: {}, TIMEZONE: {}",
                user.getUserAccountId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail(), user.getCreationTime(), user.getTimeZone());

        try {
            request.login(user.getUsername(), unencryptedPassword);
        } catch (ServletException e) {
            logger.error("Login error - ", e.getMessage());
            return "redirect:/signup";
        }

        return "redirect:/content";
    }
}
