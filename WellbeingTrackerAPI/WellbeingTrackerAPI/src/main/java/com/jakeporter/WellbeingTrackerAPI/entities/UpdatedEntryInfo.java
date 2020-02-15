package com.jakeporter.WellbeingTrackerAPI.entities;

/**
 *
 * @author jake
 */
public class UpdatedEntryInfo {
    
    private int entryId;
    private int value;

    public int getEntryId() {
        return entryId;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "UpdatedEntryInfo{" + "entryId=" + entryId + ", value=" + value + '}';
    }
    
    

}
