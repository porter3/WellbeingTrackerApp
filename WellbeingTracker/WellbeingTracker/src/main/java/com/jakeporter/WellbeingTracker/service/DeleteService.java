package com.jakeporter.WellbeingTrackerAPI.service;

/**
 *
 * @author jake
 */
public interface DeleteService {

    public void deleteMetricEntry(int entryId);
    public void deleteDayLog(int dayLogId);
    public void deleteMetricType(int typeId);
}
