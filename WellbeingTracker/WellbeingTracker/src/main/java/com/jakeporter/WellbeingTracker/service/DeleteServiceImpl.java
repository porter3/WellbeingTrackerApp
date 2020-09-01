package com.jakeporter.WellbeingTracker.service;

import com.jakeporter.WellbeingTracker.data.DayLogDao;
import com.jakeporter.WellbeingTracker.data.MetricEntryDao;
import com.jakeporter.WellbeingTracker.data.MetricTypeDao;
import com.jakeporter.WellbeingTracker.entities.MetricEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author jake
 */

@Service
public class DeleteServiceImpl implements DeleteService{
    
    @Autowired
    MetricEntryDao entryDao;
    
    @Autowired
    DayLogDao logDao;
    
    @Autowired
    MetricTypeDao typeDao;
    
    @Autowired
    LookupService lookupService;

    public void deleteMetricEntry(int entryId) {
        entryDao.deleteMetricEntry(entryId);
    }

    public void deleteDayLog(int dayLogId){
        logDao.deleteDayLog(dayLogId);
    }
    
    public void deleteMetricType(int typeId){
        for (MetricEntry entry : lookupService.getMetricEntriesForType(typeId)){
            entryDao.deleteMetricEntry(entry.getMetricEntryId());
        }
        typeDao.deleteMetricType(typeId);
    }
}
