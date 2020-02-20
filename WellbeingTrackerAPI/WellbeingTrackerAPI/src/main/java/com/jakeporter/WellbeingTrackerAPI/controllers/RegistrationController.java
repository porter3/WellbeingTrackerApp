package com.jakeporter.WellbeingTrackerAPI.controllers;

import com.jakeporter.WellbeingTrackerAPI.entities.UserAccount;
import com.jakeporter.WellbeingTrackerAPI.exceptions.InvalidEmailException;
import com.jakeporter.WellbeingTrackerAPI.exceptions.InvalidPasswordException;
import com.jakeporter.WellbeingTrackerAPI.exceptions.InvalidUsernameException;
import com.jakeporter.WellbeingTrackerAPI.service.AddService;
import com.jakeporter.WellbeingTrackerAPI.service.LookupService;
import com.jakeporter.WellbeingTrackerAPI.service.ValidateService;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    private Set<String> violations = new HashSet();
    
    @GetMapping("/signup")
    public String displaySignUp(){
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
            model.addAttribute("violations", violations);
            return "redirect:/signup";
        }
        addService.createNewAccount(user);
        
        // TODO: log user in
        
        return "redirect:/login";
    }
}
