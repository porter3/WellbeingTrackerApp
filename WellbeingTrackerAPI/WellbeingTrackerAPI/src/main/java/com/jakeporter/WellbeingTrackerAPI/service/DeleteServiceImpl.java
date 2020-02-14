package com.jakeporter.WellbeingTrackerAPI.service;

import com.jakeporter.WellbeingTrackerAPI.data.DayLogDao;
import com.jakeporter.WellbeingTrackerAPI.data.MetricEntryDao;
import com.jakeporter.WellbeingTrackerAPI.entities.MetricEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author jake
 */

@Service
public class DeleteServiceImpl {
    
    @Autowired
    MetricEntryDao entryDao;
    
    @Autowired
    DayLogDao logDao;

    public void deleteMetricEntry(int entryId) {
        entryDao.deleteMetricEntry(entryId);
    }

    public void deleteDayLog(int dayLogId){
        logDao.deleteDayLog(dayLogId);
    }
}
