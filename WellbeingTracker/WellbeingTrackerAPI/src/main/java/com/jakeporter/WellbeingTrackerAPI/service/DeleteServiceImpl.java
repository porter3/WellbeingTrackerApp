package com.jakeporter.WellbeingTrackerAPI.service;

import com.jakeporter.WellbeingTrackerAPI.data.DayLogDao;
import com.jakeporter.WellbeingTrackerAPI.data.MetricEntryDao;
import com.jakeporter.WellbeingTrackerAPI.data.MetricTypeDao;
import com.jakeporter.WellbeingTrackerAPI.entities.MetricEntry;
import java.util.List;
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
