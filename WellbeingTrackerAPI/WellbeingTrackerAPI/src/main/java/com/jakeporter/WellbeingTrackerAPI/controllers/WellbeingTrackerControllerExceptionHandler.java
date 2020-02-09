package com.jakeporter.WellbeingTrackerAPI.controllers;

import com.jakeporter.WellbeingTrackerAPI.exceptions.InvalidEmailException;
import com.jakeporter.WellbeingTrackerAPI.exceptions.InvalidMetricTypeException;
import com.jakeporter.WellbeingTrackerAPI.exceptions.InvalidPasswordException;
import com.jakeporter.WellbeingTrackerAPI.exceptions.InvalidUsernameException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

/**
 *
 * @author jake
 */

@ControllerAdvice
@RestController
public class WellbeingTrackerControllerExceptionHandler {

    @ExceptionHandler(InvalidUsernameException.class)
    // WebRequest: generic interface for a web request
    public final ResponseEntity<Error> handleInvalidUsernameException(InvalidUsernameException e, WebRequest request){
        Error error = new Error();
        error.setMessage(e.getMessage());
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(InvalidPasswordException.class)
    public final ResponseEntity<Error> handleInvalidPasswordException(InvalidPasswordException e, WebRequest request){
        Error error = new Error();
        error.setMessage(e.getMessage());
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(InvalidEmailException.class)
    public final ResponseEntity<Error> handleInvalidEmailException(InvalidEmailException e, WebRequest request){
        Error error = new Error();
        error.setMessage(e.getMessage());
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(InvalidMetricTypeException.class)
    public final ResponseEntity<Error> handleInvalidMetricTypeException(InvalidMetricTypeException e, WebRequest request){
        Error error = new Error();
        error.setMessage(e.getMessage());
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }
}
