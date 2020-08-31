package com.jakeporter.WellbeingTrackerAPI.exceptions;

/**
 *
 * @author jake
 */
public class InvalidMetricTypeException extends Throwable{

    public InvalidMetricTypeException(String message){
        super(message);
    }
    
    public InvalidMetricTypeException(String message, Throwable e){
        super(message, e);
    }
}
