package com.jakeporter.WellbeingTracker.entities;

/**
 *
 * @author jake
 */

public class LogHolder {
    
    private String date;
    private UpdatedEntryInfo[] updatedEntries;
    private NewEntryInfo[] newEntries;
    private String notes;

    public String getDate() {
        return date;
    }

    public UpdatedEntryInfo[] getUpdatedEntries() {
        return updatedEntries;
    }

    public NewEntryInfo[] getNewEntries() {
        return newEntries;
    }

    public String getNotes() {
        return notes;
    }
}
