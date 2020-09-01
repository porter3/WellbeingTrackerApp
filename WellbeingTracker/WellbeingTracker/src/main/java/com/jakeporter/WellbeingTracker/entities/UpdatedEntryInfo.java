package com.jakeporter.WellbeingTracker.entities;

/**
 *
 * @author jake
 */
public class UpdatedEntryInfo {
    
    private int entryId;
    private float value;
    private boolean valueIsEmpty;

    public int getEntryId() {
        return entryId;
    }

    public float getValue() {
        return value;
    }

    public boolean isValueIsEmpty() {
        return valueIsEmpty;
    }
    

    @Override
    public String toString() {
        return "UpdatedEntryInfo{" + "entryId=" + entryId + ", value=" + value + '}';
    }
    
    

}
