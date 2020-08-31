package com.jakeporter.WellbeingTrackerAPI.exceptions;

/**
 *
 * @author jake
 */
public class InvalidUsernameException extends Throwable{

    public InvalidUsernameException(String message){
        super(message);
    }
    
    public InvalidUsernameException(String message, Throwable e){
        super(message, e);
    }
}
