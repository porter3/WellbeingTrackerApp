package com.jakeporter.WellbeingTracker.exceptions;

/**
 *
 * @author jake
 */
public class InvalidEntryException extends Throwable{

    public InvalidEntryException(String message){
        super(message);
    }
    
    public InvalidEntryException(String message, Throwable e){
        super(message, e);
    }
}
