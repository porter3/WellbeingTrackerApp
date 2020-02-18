package com.jakeporter.WellbeingTrackerAPI.service;

import com.jakeporter.WellbeingTrackerAPI.entities.DayLog;
import com.jakeporter.WellbeingTrackerAPI.entities.MetricEntry;
import com.jakeporter.WellbeingTrackerAPI.entities.MetricType;
import com.jakeporter.WellbeingTrackerAPI.entities.UserAccount;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author jake
 */
public interface LookupService {

    public List<DayLog> getDayLogsForUser(int userId);
    public List<LocalDate> getDatesForUser(int userId);
    public List<MetricEntry> getMetricEntriesForType(int typeId);
    public List<MetricEntry> getMetricEntriesForUser(int userId);
    public List<MetricEntry> getMetricEntriesByDate(int userId, LocalDate date);
    public List<MetricType> getMetricTypesForUser(int userId);
    public MetricEntry getMetricEntryById(int entryId);
    public MetricType getMetricTypeById(int typeId);
    public UserAccount getUserAccountById(int userId);
    public DayLog getDayLogByDateAndUser(int userId, LocalDate convertedDate);
    public UserAccount getUserByUsername(String username);
}
