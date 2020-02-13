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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    
    // get all log dates for a user (FOR GRAPH VIEW)
    @GetMapping("/dates/{userId}")
    public ResponseEntity<List<LocalDate>> getAllLogDatesForUser(@PathVariable int userId){
        return new ResponseEntity(lookupService.getDatesForUser(userId), HttpStatus.OK);
    }
    
    // get all metric types for a user (FOR GRAPH VIEW)
    @GetMapping("/metrictypes/{userId}")
    public ResponseEntity<List<MetricType>> getAllMetricTypesForUser(@PathVariable int userId){
        return new ResponseEntity(lookupService.getMetricTypesForUser(userId), HttpStatus.OK);
    }
    
    // get all metric entries for a user (FOR GRAPH VIEW)
    @GetMapping("/metricentries/{userId}")
    public ResponseEntity<List<List<MetricEntry>>> getAllMetricEntriesForUser(@PathVariable int userId){
        // Get all types for a user
        List<MetricType> userTypes = lookupService.getMetricTypesForUser(userId);
        // List of MetricEntry-containing Lists
        List<List<MetricEntry>> entryLists = new ArrayList();
        // for each type the user has, get all the entries for that type in a list
        for (MetricType type : userTypes){
            List<MetricEntry> entryList = lookupService.getMetricEntriesForType(type.getMetricTypeId());
            // add all entries for that type to the list of MetricEntry-containing lists
            entryLists.add(entryList);
        }
        return new ResponseEntity(entryLists, HttpStatus.OK);
    }
    
    // get all entries for a user by date
    @GetMapping("/metricentries/{userId}/{date}")
    public ResponseEntity<List<MetricEntry>> getMetricEntriesByDate(@PathVariable int userId, @PathVariable String date){
        LocalDate convertedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        return new ResponseEntity(lookupService.getMetricEntriesByDate(userId, convertedDate), HttpStatus.OK);
    }
}
