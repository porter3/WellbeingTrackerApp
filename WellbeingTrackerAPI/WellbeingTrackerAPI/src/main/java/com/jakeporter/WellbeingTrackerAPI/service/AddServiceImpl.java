package com.jakeporter.WellbeingTrackerAPI.service;

import com.jakeporter.WellbeingTrackerAPI.data.MetricTypeDao;
import com.jakeporter.WellbeingTrackerAPI.data.UserAccountDao;
import com.jakeporter.WellbeingTrackerAPI.entities.MetricType;
import com.jakeporter.WellbeingTrackerAPI.entities.UserAccount;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author jake
 */

@Service
public class AddServiceImpl {
    
    @Autowired
    UserAccountDao userDao;
    
    @Autowired
    MetricTypeDao typeDao;

    public UserAccount createNewAccount(UserAccount user){
        return userDao.addUserAccount(user);
    }
    
    public List<MetricType> addMetricTypes(MetricType... types){
        // list that will hold MetricTypes after having IDs assigned
        List<MetricType> populatedTypeList = new ArrayList();
        for (MetricType type : types){
            MetricType populatedType = typeDao.addMetricType(type);
            populatedTypeList.add(populatedType);
        }
        return populatedTypeList;
    }
}
