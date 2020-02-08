package com.jakeporter.WellbeingTrackerAPI.service;

import com.jakeporter.WellbeingTrackerAPI.data.UserAccountDao;
import com.jakeporter.WellbeingTrackerAPI.entities.UserAccount;
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

    public UserAccount createNewAccount(UserAccount user){
        return userDao.addUserAccount(user);
    }
}
