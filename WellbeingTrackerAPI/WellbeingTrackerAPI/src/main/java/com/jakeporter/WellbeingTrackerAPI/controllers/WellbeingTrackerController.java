package com.jakeporter.WellbeingTrackerAPI.controllers;

import com.jakeporter.WellbeingTrackerAPI.entities.DayLog;
import com.jakeporter.WellbeingTrackerAPI.entities.MetricType;
import com.jakeporter.WellbeingTrackerAPI.entities.UserAccount;
import com.jakeporter.WellbeingTrackerAPI.exceptions.InvalidEmailException;
import com.jakeporter.WellbeingTrackerAPI.exceptions.InvalidMetricTypeException;
import com.jakeporter.WellbeingTrackerAPI.exceptions.InvalidPasswordException;
import com.jakeporter.WellbeingTrackerAPI.exceptions.InvalidUsernameException;
import com.jakeporter.WellbeingTrackerAPI.service.AddServiceImpl;
import com.jakeporter.WellbeingTrackerAPI.service.DeleteServiceImpl;
import com.jakeporter.WellbeingTrackerAPI.service.LookupServiceImpl;
import com.jakeporter.WellbeingTrackerAPI.service.UpdateServiceImpl;
import com.jakeporter.WellbeingTrackerAPI.service.ValidateServiceImpl;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author jake
 */

@Controller
@CrossOrigin // allows server requests from different origins
@RequestMapping("/")
public class WellbeingTrackerController {
    
    // TODO: CHANGE ALL SERVICE LAYER IMPLEMENTATION REFERENCES TO INTERFACES WHEN THEY'RE CREATED
    
    @Autowired
    AddServiceImpl addService;
    
    @Autowired
    LookupServiceImpl lookupService;
    
    @Autowired
    UpdateServiceImpl updateService;
    
    @Autowired
    DeleteServiceImpl deleteService;
    
    @Autowired
    ValidateServiceImpl validateService;
    
//    @GetMapping("/")
//    public String renderLandingPage(){
//        return "home";
//    }
    
//    @GetMapping("/login")
//    /* @RequestParam extracts query parameters, @PathVariable extracts data right from the URL */
//    public String renderLoginPage(@RequestParam(value = "error", required = false) String error, 
//                            @RequestParam(value = "logout", required = false) String logout,
//                            Model model){
//        String errorMessage = null;
//        if(error != null) {
//            errorMessage = "Username or password is incorrect.";
//        }
//        if(logout != null) {
//            errorMessage = "You have been successfully logged out.";
//        }
//        model.addAttribute("errorMessage", errorMessage);
//        return "login";
//    }
//    
//    @GetMapping("/logout")
//    public String renderLogoutPage(HttpServletRequest request, HttpServletResponse response){
//        // get Authentication details of current user
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth == null){
//            new SecurityContextLogoutHandler().logout(request, response, auth);
//        }
//        return "redirect:/login?logout=true";
//    }
//    
//    @GetMapping({"/","/dataview"})
//    public String renderDataView(){
//        return "dataView";
//    }

    // Creates a new user account
    @PostMapping("/api/createUser")
    public ResponseEntity<UserAccount> createAccount(@RequestBody UserAccount user) throws InvalidUsernameException, InvalidPasswordException, InvalidEmailException{
        // validate user
        validateService.validateNewAccountSettings(user);
        // add user to DB
        user = addService.createNewAccount(user);
        return new ResponseEntity(user, HttpStatus.CREATED);
    }
    
    @PostMapping("/api/addMetrics/{userId}")
    public ResponseEntity<List<MetricType>> createMetricSettings(@PathVariable int userId, @RequestBody MetricType[] metricTypes) throws InvalidMetricTypeException{
        // lookup the user using the ID from the PathVariable and assign to each MetricType
        updateService.populateMetricTypesWithUser(userId, metricTypes);
        // validate user's initial MetricType settings
        validateService.validateMetricTypes(userId, metricTypes);
        // add MetricTypes to DB, get them back with IDs
        List<MetricType> populatedTypeList = addService.addMetricTypes(metricTypes);
        return new ResponseEntity(populatedTypeList, HttpStatus.CREATED);
    }
    
    // Gets all DayLogs for a given UserAccount
    @GetMapping("/api/dayLogs/{userId}")
    public ResponseEntity<List<DayLog>> getDayLogsForUser(){
        // List<DayLog> userLogs = service.getDayLogsForUser(userId;
        //
        
        String test = "test";
        return new ResponseEntity(test, HttpStatus.CREATED);
    }
}
