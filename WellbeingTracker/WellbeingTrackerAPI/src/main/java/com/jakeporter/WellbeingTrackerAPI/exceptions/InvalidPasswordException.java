package com.jakeporter.WellbeingTrackerAPI.exceptions;

/**
 *
 * @author jake
 */
public class InvalidPasswordException extends Throwable{
    
    public InvalidPasswordException(String message){
        super(message);
    }
    
    public InvalidPasswordException(String message, Throwable e){
        super(message, e);
    }

}
