package com.jakeporter.WellbeingTrackerAPI.controllers;

import com.jakeporter.WellbeingTrackerAPI.entities.DayLog;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jake
 */

@RestController
@RequestMapping("/tracker/api")
public class WellbeingTrackerController {

    // Gets all DayLogs for a given UserAccount
    @GetMapping("/dayLogs/{userId}")
    public ResponseEntity<List<DayLog>>getDayLogsForUser(){
        String test = "test";
        return new ResponseEntity(test, HttpStatus.CREATED);
    }
}
