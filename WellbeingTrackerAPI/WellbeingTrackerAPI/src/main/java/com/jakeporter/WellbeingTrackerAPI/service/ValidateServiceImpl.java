package com.jakeporter.WellbeingTrackerAPI.service;

import com.jakeporter.WellbeingTrackerAPI.data.UserAccountDao;
import com.jakeporter.WellbeingTrackerAPI.entities.MetricType;
import com.jakeporter.WellbeingTrackerAPI.entities.UserAccount;
import com.jakeporter.WellbeingTrackerAPI.exceptions.InvalidEmailException;
import com.jakeporter.WellbeingTrackerAPI.exceptions.InvalidMetricTypeException;
import com.jakeporter.WellbeingTrackerAPI.exceptions.InvalidPasswordException;
import com.jakeporter.WellbeingTrackerAPI.exceptions.InvalidUsernameException;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author jake
 */

@Service
public class ValidateServiceImpl {
    
    @Autowired
    UserAccountDao userDao;

    public void validateNewAccountSettings(UserAccount user) throws InvalidUsernameException, InvalidPasswordException, InvalidEmailException{
        validateUsername(user.getUserName());
        validatePassword(user.getPassword());
        validateEmail(user.getPassword());
    }
    
    public void validateUsername(String username) throws InvalidUsernameException{
        // check for a length over 15
        if (username.length() > 15){
            throw new InvalidUsernameException("Username cannot be over 15 characters.");
        }
        validateUsernameDoesNotExist(username);
    }
    
    private void validateUsernameDoesNotExist(String username) throws InvalidUsernameException{
        Set<String> usernames = userDao.getAllUserAccounts()
                .stream().map(UserAccount::getUserName).collect(Collectors.toSet());
        if (usernames.contains(username)){
            throw new InvalidUsernameException("Username already exists.");
        }
    }
    
    public void validatePassword(String password) throws InvalidPasswordException{
        /* ^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,50}$ matches strings with at least 8 characters, 
        no more than 50 characters, and at least one lowercase letter, uppercase letter, and
        number each */
        
        // TODO: split this up so regex only checks for uppercase/lowercase/number inclusion, not casing
        if (!Pattern.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,50}$", password)){
            throw new InvalidPasswordException("Password must be between 8-50 characters, as well as "
                    + "contain at least 1 uppercase letter, 1 lowercase letter, and 1 number.");
        }
    }
    
    public void validateEmail(String email) throws InvalidEmailException{
        // TODO: fix email regex. Works in tester, doesn't work here. Probably character escape issue.
        
//        if (!Pattern.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", email)){
//            throw new InvalidEmailException("Email address is invalid.");
//        }
        validateEmailDoesNotExist(email);
    }
    
    private void validateEmailDoesNotExist(String email) throws InvalidEmailException{
        Set<String> emails = userDao.getAllUserAccounts()
                .stream().map(UserAccount::getEmail).collect(Collectors.toSet());
        if (emails.contains(email)){
            throw new InvalidEmailException("Email is already registered.");
        }
    }
    
    // compiler implicitly creates an array in the parameter list when this is called
    public void validateMetricTypes(MetricType... types) throws InvalidMetricTypeException{
        for (MetricType type : types){
            if (type.getMetricName() == null){
                throw new InvalidMetricTypeException("Metric type's name cannot be empty.");
            }
            if (type.getUser() == null){
                throw new InvalidMetricTypeException("Metric type must be associated with a user.");
            }
//            if ((type.getScale() == null) && (type.getUnit() == null)){
//                
//            }
        }
    }

}
