package com.jakeporter.WellbeingTrackerAPI.controllers;

import com.jakeporter.WellbeingTrackerAPI.entities.DayLog;
import com.jakeporter.WellbeingTrackerAPI.entities.MetricEntry;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jake
 */

@RestController
@CrossOrigin
@RequestMapping("/api")
public class APIController {
    
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
    @PostMapping("/createuser")
    public ResponseEntity<UserAccount> createAccount(@RequestBody UserAccount user) throws InvalidUsernameException, InvalidPasswordException, InvalidEmailException{
        // validate user
        validateService.validateNewAccountSettings(user);
        // add user to DB
        user = addService.createNewAccount(user);
        return new ResponseEntity(user, HttpStatus.CREATED);
    }
    
    @PostMapping("/addmetrics/{userId}")
    public ResponseEntity<List<MetricType>> createMetricSettings(@PathVariable int userId, @RequestBody MetricType[] metricTypes) throws InvalidMetricTypeException{
        // lookup the user using the ID from the PathVariable and assign to each MetricType
        updateService.populateMetricTypesWithUser(userId, metricTypes);
        // validate user's initial MetricType settings
        validateService.validateMetricTypes(userId, metricTypes);
        // add MetricTypes to DB, get them back with IDs
        List<MetricType> populatedTypeList = addService.addMetricTypes(metricTypes);
        return new ResponseEntity(populatedTypeList, HttpStatus.CREATED);
    }
    
    // Gets all MetricEntries for a given UserAccount (default graph view endpoint)
    @GetMapping("/metricentries/{userId}")
    public ResponseEntity<List<List<MetricEntry>>> getAllMetricEntriesForUser(@PathVariable int userId){
        // Get all entries for a user
        List<MetricEntry> userEntries = lookupService.getMetricEntriesForUser(userId);
        
        // for each type the User has, grab all entries for that type and put them together
        
        // don't actually want to return this when finished
        return new ResponseEntity(userEntries, HttpStatus.CREATED);
        // return 
    }
}
