package com.jakeporter.WellbeingTrackerAPI.controllers;

import com.jakeporter.WellbeingTrackerAPI.entities.UserAccount;
import com.jakeporter.WellbeingTrackerAPI.exceptions.InvalidEmailException;
import com.jakeporter.WellbeingTrackerAPI.exceptions.InvalidPasswordException;
import com.jakeporter.WellbeingTrackerAPI.exceptions.InvalidUsernameException;
import com.jakeporter.WellbeingTrackerAPI.service.AddService;
import com.jakeporter.WellbeingTrackerAPI.service.DeleteService;
import com.jakeporter.WellbeingTrackerAPI.service.LookupService;
import com.jakeporter.WellbeingTrackerAPI.service.ValidateService;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
public class ContentController {
    
    @Autowired
    ValidateService validateService;
    
    @Autowired
    AddService addService;
    
    @Autowired
    LookupService lookupService;
    
    @Autowired
    DeleteService deleteService;
    
    
    private Set<String> violations = new HashSet();

    @GetMapping("/content")
    public String displayContentPage(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        UserAccount user = lookupService.getUserByUsername(currentUser.getUsername()); // CHANGE TO SERVICE METHOD
        model.addAttribute("currentUser", user);
        
        return "dataView_content";
    }
    
    @GetMapping("/addmetrics")
    public String displayAddMetrics(){
        return "addMetrics";
    }
    
    @GetMapping("/removemetrics")
    public String displayRemoveMetrics(Model model){
        // HARDCODED USER ID
        model.addAttribute("typeList", lookupService.getMetricTypesForUser(1));
        return "removeMetrics";
    }
    
    @GetMapping("/deleteconfirmation")
    public String deleteConfirmation(String id, Model model){
        int typeId = Integer.parseInt(id);
        model.addAttribute("type", lookupService.getMetricTypeById(typeId));
        model.addAttribute("numberOfEntries", lookupService.getMetricEntriesForType(typeId).size());
        return "deleteConfirmation";
    }
    
    @GetMapping("/deletemetrictype")
    public String performTypeDeletion(String id){
        int typeId = Integer.parseInt(id);
        // perform deletion
        deleteService.deleteMetricType(typeId);
        return "redirect:/removemetrics";
    }
    
    @GetMapping("/usersettings")
    public String displayUserSettings(){
        return "userSettings";
    }
    
    @GetMapping("/signup")
    public String displaySignUp(){
        return "signUp";
    }
    
    @PostMapping("/adduser")
    public String addUser(UserAccount user, HttpServletRequest request, Model model){
        // validate user. User should be validated client-side first, should theoretically never have to throw exceptions here.
        try{
            validateService.validateNewAccountSettings(user);
        }
        catch(InvalidUsernameException | InvalidPasswordException | InvalidEmailException e){
            violations.add(e.getMessage());
        }
        if (!violations.isEmpty()){
            model.addAttribute("violations", violations);
            return "redirect:/signUp";
        }
        addService.createNewAccount(user);
        
        // log user in
        
        return "redirect:/content";
    }
}
