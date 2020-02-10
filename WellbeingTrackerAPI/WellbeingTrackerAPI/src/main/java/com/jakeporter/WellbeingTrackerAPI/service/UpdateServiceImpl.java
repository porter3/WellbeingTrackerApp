package com.jakeporter.WellbeingTrackerAPI.service;

import com.jakeporter.WellbeingTrackerAPI.data.UserAccountDao;
import com.jakeporter.WellbeingTrackerAPI.entities.MetricType;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author jake
 */

@Service
public class UpdateServiceImpl {
    
    @Autowired
    UserAccountDao userDao;

    public void populateMetricTypesWithUser(int userId, MetricType... types){
        for (MetricType type: types){
            type.setUser(userDao.getUserAccountById(userId));
        }
    }
}
