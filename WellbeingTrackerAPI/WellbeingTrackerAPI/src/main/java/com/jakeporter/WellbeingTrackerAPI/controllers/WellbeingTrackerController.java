package com.jakeporter.WellbeingTrackerAPI.controllers;

import com.jakeporter.WellbeingTrackerAPI.entities.DayLog;
import com.jakeporter.WellbeingTrackerAPI.entities.UserAccount;
import com.jakeporter.WellbeingTrackerAPI.exceptions.InvalidEmailException;
import com.jakeporter.WellbeingTrackerAPI.exceptions.InvalidPasswordException;
import com.jakeporter.WellbeingTrackerAPI.exceptions.InvalidUsernameException;
import com.jakeporter.WellbeingTrackerAPI.service.AddServiceImpl;
import com.jakeporter.WellbeingTrackerAPI.service.DeleteServiceImpl;
import com.jakeporter.WellbeingTrackerAPI.service.LookupServiceImpl;
import com.jakeporter.WellbeingTrackerAPI.service.UpdateServiceImpl;
import com.jakeporter.WellbeingTrackerAPI.service.ValidateServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jake
 */

@RestController
@RequestMapping("/tracker/api")
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

    // Creates a new user account
    @PostMapping("/createUser")
    public ResponseEntity<UserAccount> createAccount(@RequestBody UserAccount user) throws InvalidUsernameException, InvalidPasswordException, InvalidEmailException{
        // validate user
        validateService.validateNewAccountSettings(user);
        // add user to DB
        user = addService.createNewAccount(user);
        return new ResponseEntity(user, HttpStatus.CREATED);
    }
    
    // Gets all DayLogs for a given UserAccount
    @GetMapping("/dayLogs/{userId}")
    public ResponseEntity<List<DayLog>> getDayLogsForUser(){
        // List<DayLog> userLogs = service.getDayLogsForUser(userId;
        //
        
        String test = "test";
        return new ResponseEntity(test, HttpStatus.CREATED);
    }
}
