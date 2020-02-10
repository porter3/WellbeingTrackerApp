package com.jakeporter.WellbeingTrackerAPI.data;

import com.jakeporter.WellbeingTrackerAPI.entities.UserAccount;
import java.util.List;

/**
 *
 * @author jake
 */
public interface UserAccountDao {

    public UserAccount addUserAccount(UserAccount user);
    
    public UserAccount getUserAccountById(int userId);
    
    public List<UserAccount> getAllUserAccounts();
    
    public UserAccount editUserAccount(UserAccount updatedUser);
    
    public void deleteUserAccount (int userId);

    public UserAccount getUserByUsername(String username);
}
