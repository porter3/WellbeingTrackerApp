package com.jakeporter.WellbeingTrackerAPI.service;

import com.jakeporter.WellbeingTrackerAPI.data.MetricTypeDao;
import com.jakeporter.WellbeingTrackerAPI.entities.MetricType;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author jake
 */

@Service
public class LookupServiceImpl {

    @Autowired
    MetricTypeDao typeDao;
    
    public List<MetricType> getMetricTypesForUser(int userId){
        return typeDao.getAllMetricTypes().stream()
                .filter(type -> type.getUser().getUserAccountId() == userId)
                .collect(Collectors.toList());
    }
}
