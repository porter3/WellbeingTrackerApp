package com.jakeporter.WellbeingTrackerAPI.controllers;

import com.jakeporter.WellbeingTrackerAPI.entities.UserAccount;
import com.jakeporter.WellbeingTrackerAPI.service.AddService;
import com.jakeporter.WellbeingTrackerAPI.service.LookupService;
import com.jakeporter.WellbeingTrackerAPI.service.ValidateService;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
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
    
    @Autowired
    AddService addService;
    
    @Autowired
    LookupService lookupService;
    
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
        System.out.println("ADDUSER IS EXECUTING");
        // populates UserAccount object from form
        UserAccount user = addService.populateNewUserFromForm(request);
        // validate user in case client-side doesn't exist/work
        validateService.validateNewAccountSettings(violations, user, request.getParameter("confirmPassword"));
        if (!violations.isEmpty()){
            for (String violation: violations){ // test statement
                System.out.println(violation);
            }
            return "redirect:/signup";
        }
        // encrypt user's password
        user.setPassword(encoder.encode(user.getPassword()));
        addService.createNewAccount(user);
        
        // TODO: log user in
        return "redirect:/login";
    }
}
