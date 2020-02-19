package com.jakeporter.WellbeingTrackerAPI.entities;

/**
 *
 * @author jake
 */
public class UpdatedEntryInfo {
    
    private int entryId;
    private float value;

    public int getEntryId() {
        return entryId;
    }

    public float getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "UpdatedEntryInfo{" + "entryId=" + entryId + ", value=" + value + '}';
    }
    
    

}
