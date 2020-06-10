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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author jake
 */

@Controller
@CrossOrigin
public class ContentController {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    
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
        UserAccount user = lookupService.getUserByUsername(currentUser.getUsername());
        logger.info("Generating content for user: {}", user.getUsername());
        model.addAttribute("currentUser", user);
        
        return "dataView_content";
    }
    
    // used the longer @RequestMapping annotation just to compare to the shortcut versions
    @RequestMapping(value = "/addmetrics", method = RequestMethod.GET)
    public String displayAddMetrics(Model model, @AuthenticationPrincipal UserDetails currentUser){
        UserAccount user = lookupService.getUserByUsername(currentUser.getUsername()); // CHANGE TO SERVICE METHOD
        model.addAttribute("currentUser", user);
        return "addMetrics";
    }
    
    @GetMapping("/removemetrics")
    public String displayRemoveMetrics(Model model, @AuthenticationPrincipal UserDetails currentUser){
        UserAccount user = lookupService.getUserByUsername(currentUser.getUsername());
        model.addAttribute("typeList", lookupService.getMetricTypesForUser(user.getUserAccountId()));
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
    
}
