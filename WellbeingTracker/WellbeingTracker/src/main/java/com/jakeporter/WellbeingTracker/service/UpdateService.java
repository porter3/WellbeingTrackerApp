package com.jakeporter.WellbeingTracker.service;

import com.jakeporter.WellbeingTracker.entities.DayLog;
import com.jakeporter.WellbeingTracker.entities.MetricEntry;
import com.jakeporter.WellbeingTracker.entities.MetricType;

/**
 *
 * @author jake
 */
public interface UpdateService {

    public void populateMetricTypesWithUser(int userId, MetricType... types);
    public MetricEntry updateMetricEntry(MetricEntry entry);
    public DayLog updateDayLog(DayLog log);
}
