package com.jakeporter.WellbeingTracker.service;

import com.jakeporter.WellbeingTracker.data.DayLogDao;
import com.jakeporter.WellbeingTracker.data.MetricEntryDao;
import com.jakeporter.WellbeingTracker.data.UserAccountDao;
import com.jakeporter.WellbeingTracker.entities.DayLog;
import com.jakeporter.WellbeingTracker.entities.MetricEntry;
import com.jakeporter.WellbeingTracker.entities.MetricType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author jake
 */

@Service
public class UpdateServiceImpl implements UpdateService{
    
    @Autowired
    UserAccountDao userDao;
    
    @Autowired
    MetricEntryDao entryDao;
    
    @Autowired
    DayLogDao logDao;

    public void populateMetricTypesWithUser(int userId, MetricType... types){
        for (MetricType type: types){
            type.setUser(userDao.getUserAccountById(userId));
        }
    }
    
    public MetricEntry updateMetricEntry(MetricEntry entry){
        return entryDao.editMetricEntry(entry);
    }
    
    public DayLog updateDayLog(DayLog log){
        return logDao.updateDayLog(log);
    }
}
