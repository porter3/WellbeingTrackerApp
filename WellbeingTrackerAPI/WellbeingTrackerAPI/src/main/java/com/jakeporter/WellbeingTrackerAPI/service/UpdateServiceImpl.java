package com.jakeporter.WellbeingTrackerAPI.service;

import com.jakeporter.WellbeingTrackerAPI.data.DayLogDao;
import com.jakeporter.WellbeingTrackerAPI.data.MetricEntryDao;
import com.jakeporter.WellbeingTrackerAPI.data.UserAccountDao;
import com.jakeporter.WellbeingTrackerAPI.entities.DayLog;
import com.jakeporter.WellbeingTrackerAPI.entities.MetricEntry;
import com.jakeporter.WellbeingTrackerAPI.entities.MetricType;
import java.sql.SQLException;
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
