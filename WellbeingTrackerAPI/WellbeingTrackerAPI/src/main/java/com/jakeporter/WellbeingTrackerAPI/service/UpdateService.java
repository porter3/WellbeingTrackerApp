package com.jakeporter.WellbeingTrackerAPI.service;

import com.jakeporter.WellbeingTrackerAPI.entities.DayLog;
import com.jakeporter.WellbeingTrackerAPI.entities.MetricEntry;
import com.jakeporter.WellbeingTrackerAPI.entities.MetricType;

/**
 *
 * @author jake
 */
public interface UpdateService {

    public void populateMetricTypesWithUser(int userId, MetricType... types);
    public MetricEntry updateMetricEntry(MetricEntry entry);
    public DayLog updateDayLog(DayLog log);
}
