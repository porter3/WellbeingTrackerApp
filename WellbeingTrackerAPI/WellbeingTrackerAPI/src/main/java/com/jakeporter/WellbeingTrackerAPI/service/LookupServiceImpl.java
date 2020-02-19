package com.jakeporter.WellbeingTrackerAPI.service;

import com.jakeporter.WellbeingTrackerAPI.data.DayLogDao;
import com.jakeporter.WellbeingTrackerAPI.data.MetricEntryDao;
import com.jakeporter.WellbeingTrackerAPI.data.MetricTypeDao;
import com.jakeporter.WellbeingTrackerAPI.data.UserAccountDao;
import com.jakeporter.WellbeingTrackerAPI.entities.DayLog;
import com.jakeporter.WellbeingTrackerAPI.entities.MetricEntry;
import com.jakeporter.WellbeingTrackerAPI.entities.MetricType;
import com.jakeporter.WellbeingTrackerAPI.entities.UserAccount;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author jake
 */

@Service
public class LookupServiceImpl implements LookupService{

    @Autowired
    DayLogDao logDao;
    
    @Autowired
    MetricEntryDao entryDao;
    
    @Autowired
    MetricTypeDao typeDao;
    
    @Autowired
    UserAccountDao userDao;
    
    // need to create a Comparator for DayLog that sorts by logDate if you want these sorted
    public List<DayLog> getDayLogsForUser(int userId){
        List<DayLog> dayLogs = logDao.getAllDayLogs().stream()
                .filter(log -> log.getUser().getUserAccountId() == userId)
                .collect(Collectors.toList());
        return dayLogs;
    }
    
    // sorted (natural order)
    public List<LocalDate> getDatesForUser(int userId){
        return getDayLogsForUser(userId).stream()
                .map(DayLog::getLogDate)
                .sorted()
                .collect(Collectors.toList());
    }
    
    public List<MetricEntry> getMetricEntriesForType(int typeId){
        return entryDao.getAllMetricEntriesSorted().stream()
                .filter(entry -> entry.getMetricType().getMetricTypeId() == typeId)
                .collect(Collectors.toList());
    }
    
    
    public List<MetricEntry> getMetricEntriesForUser(int userId){
        return entryDao.getAllMetricEntriesSorted().stream()
                .filter(entry -> entry.getDayLog().getUser().getUserAccountId() == userId)
                .collect(Collectors.toList());
    }
    
    public List<MetricEntry> getMetricEntriesByDate(int userId, LocalDate date){
        
        List<MetricEntry> entryList = getMetricEntriesForUser(userId).stream()
                .filter(entry -> entry.getDayLog().getLogDate().isEqual(date))
                .collect(Collectors.toList());
        return entryList;
    }
    
    public List<MetricType> getMetricTypesForUser(int userId){
        return typeDao.getAllMetricTypes().stream()
                .filter(type -> type.getUser().getUserAccountId() == userId)
                .collect(Collectors.toList());
    }

    public MetricEntry getMetricEntryById(int entryId) {
        return entryDao.getMetricEntryById(entryId);
    }
    
    public MetricType getMetricTypeById(int typeId){
        return typeDao.getMetricTypeById(typeId);
    }
    
    public UserAccount getUserAccountById(int userId){
        return userDao.getUserAccountById(userId);
    }

    public DayLog getDayLogByDateAndUser(int userId, LocalDate convertedDate) {
        return getDayLogsForUser(userId).stream()
                .filter(log -> log.getLogDate().isEqual(convertedDate))
                .findFirst().orElse(null);
    }
    
    public UserAccount getUserByUsername(String username){
        return userDao.getUserByUsername(username);
    }
}
