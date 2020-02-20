package com.jakeporter.WellbeingTrackerAPI.controllers;

import com.jakeporter.WellbeingTrackerAPI.entities.DayLog;
import com.jakeporter.WellbeingTrackerAPI.entities.LogHolder;
import com.jakeporter.WellbeingTrackerAPI.entities.MetricEntry;
import com.jakeporter.WellbeingTrackerAPI.entities.MetricType;
import com.jakeporter.WellbeingTrackerAPI.entities.NewEntryInfo;
import com.jakeporter.WellbeingTrackerAPI.entities.UpdatedEntryInfo;
import com.jakeporter.WellbeingTrackerAPI.entities.UserAccount;
import com.jakeporter.WellbeingTrackerAPI.exceptions.InvalidEmailException;
import com.jakeporter.WellbeingTrackerAPI.exceptions.InvalidEntryException;
import com.jakeporter.WellbeingTrackerAPI.exceptions.InvalidMetricTypeException;
import com.jakeporter.WellbeingTrackerAPI.exceptions.InvalidPasswordException;
import com.jakeporter.WellbeingTrackerAPI.exceptions.InvalidUsernameException;
import com.jakeporter.WellbeingTrackerAPI.service.AddService;
import com.jakeporter.WellbeingTrackerAPI.service.DeleteService;
import com.jakeporter.WellbeingTrackerAPI.service.LookupService;
import com.jakeporter.WellbeingTrackerAPI.service.UpdateService;
import com.jakeporter.WellbeingTrackerAPI.service.ValidateService;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
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
        
    @Autowired
    AddService addService;
    
    @Autowired
    LookupService lookupService;
    
    @Autowired
    UpdateService updateService;
    
    @Autowired
    DeleteService deleteService;
    
    @Autowired
    ValidateService validateService;
    

    // Creates a new user account
    @PostMapping("/createuser")
    public ResponseEntity<UserAccount> createAccount(@RequestBody UserAccount user) throws InvalidUsernameException, InvalidPasswordException, InvalidEmailException{
        // validate user
        validateService.validateNewAccountSettings(user);
        // add user to DB
        user = addService.createNewAccount(user);
        return new ResponseEntity(user, HttpStatus.CREATED);
    }
    
    @PostMapping("/addmetrictypes/{userId}")
    public ResponseEntity<List<MetricType>> createMetricSettings(@PathVariable int userId, @RequestBody MetricType[] metricTypes) throws InvalidMetricTypeException{
        // lookup the user using the ID from the PathVariable and assign to each MetricType
        updateService.populateMetricTypesWithUser(userId, metricTypes);
        // validate user's initial MetricType settings
        validateService.validateMetricTypes(userId, metricTypes);
        // add MetricTypes to DB, get them back with IDs
        List<MetricType> populatedTypeList = addService.addMetricTypes(metricTypes);
        return new ResponseEntity(populatedTypeList, HttpStatus.CREATED);
    }
    
    // get all DayLogs for a user
    @GetMapping("/daylogs/{userId}")
    public ResponseEntity<List<LocalDate>> getAllDayLogsForUser(@PathVariable int userId){
        return new ResponseEntity(lookupService.getDayLogsForUser(userId), HttpStatus.OK);
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
    
    // maybe could improve conditionals here, but it keeps breaking when I try to improve it so it's staying like this for now
    @PostMapping("/updateLog/{userId}")
    public ResponseEntity updateLogEntries(@PathVariable int userId, @RequestBody LogHolder holder) throws InvalidEntryException, InvalidMetricTypeException{
        LocalDate convertedDate = LocalDate.parse(holder.getDate(), DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        UpdatedEntryInfo[] updatedEntries = holder.getUpdatedEntries();
        NewEntryInfo[] newEntries = holder.getNewEntries();

        // Holds all the types for entries created/updated in this POST request (used for deletion below)
        List<MetricType> createdAndUpdatedTypes = new ArrayList();
        
        boolean onlyNewEntries = false;
        
        // if there are no updatedEntries, then there are only new entries
        if (updatedEntries.length == 0){
            onlyNewEntries = true;
        }
        if (updatedEntries.length == 0 && newEntries.length == 0){
            onlyNewEntries = false;
        }
        
        // DayLog reference- will either point to an existing DayLog or become a new one
        DayLog log = null;
        
        // check if DayLog has already been created
        log = lookupService.getDayLogByDateAndUser(userId, convertedDate);
                
        // if there are existing entries:
        if (!onlyNewEntries){
            // EDITING ENTRIES
            for (int i = 0; i < updatedEntries.length; i++){
                // get the original entry from DB
                MetricEntry originalEntry = lookupService.getMetricEntryById(updatedEntries[i].getEntryId());
                // DELETION
                if (updatedEntries[i].isValueIsEmpty()){
                    deleteService.deleteMetricEntry(updatedEntries[i].getEntryId());
                    continue;
                }
                originalEntry.setMetricValue(updatedEntries[i].getValue());
                validateService.validateMetricEntry(originalEntry);
                MetricEntry updatedEntry = updateService.updateMetricEntry(originalEntry);
                createdAndUpdatedTypes.add(updatedEntry.getMetricType());
            }
        }
        
        
        // if log is still null (meaning there are only new entries), create a new one
        if (log == null){
            log = new DayLog();
            log.setLogDate(convertedDate);
            log.setUser(lookupService.getUserAccountById(userId));
            log = addService.addDayLog(log);
        }
        
        // ADDING NEW ENTRIES
        for (int j = 0; j < newEntries.length; j++){
            MetricEntry newEntry = new MetricEntry();
            newEntry.setDayLog(log);
            newEntry.setMetricType(lookupService.getMetricTypeById(newEntries[j].getTypeId()));
            newEntry.setMetricValue(newEntries[j].getValue());
            newEntry.setEntryTime(Time.valueOf(LocalTime.now())); // TODO: change to user's time zone (shouldn't be that hard)
            validateService.validateMetricEntry(newEntry);
            // newEntry is added to DB, its type is also added to createdAndUpdatedEntries
            createdAndUpdatedTypes.add(addService.addMetricEntry(newEntry).getMetricType());
        }

        // If a DayLog has no entries, delete it
        List<DayLog> userLogs = lookupService.getDayLogsForUser(userId);
        for (DayLog dayLog : userLogs){    
            if (lookupService.getMetricEntriesByDate(userId, dayLog.getLogDate()).isEmpty()){
                deleteService.deleteDayLog(dayLog.getDayLogId());
            }
        }
        
        /*
        Add any missing DayLogs (dates with no entries) for user to fill in potential gaps.
        Does not contradict previous code block.
        */
        addService.fillDayLogGaps(userId);
        
        return new ResponseEntity(HttpStatus.OK);
    }
}
