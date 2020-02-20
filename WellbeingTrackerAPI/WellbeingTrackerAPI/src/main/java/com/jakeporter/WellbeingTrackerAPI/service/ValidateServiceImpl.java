package com.jakeporter.WellbeingTrackerAPI.service;

import com.jakeporter.WellbeingTrackerAPI.data.UserAccountDao;
import com.jakeporter.WellbeingTrackerAPI.entities.MetricEntry;
import com.jakeporter.WellbeingTrackerAPI.entities.MetricType;
import com.jakeporter.WellbeingTrackerAPI.entities.UserAccount;
import com.jakeporter.WellbeingTrackerAPI.exceptions.InvalidEmailException;
import com.jakeporter.WellbeingTrackerAPI.exceptions.InvalidEntryException;
import com.jakeporter.WellbeingTrackerAPI.exceptions.InvalidMetricTypeException;
import com.jakeporter.WellbeingTrackerAPI.exceptions.InvalidPasswordException;
import com.jakeporter.WellbeingTrackerAPI.exceptions.InvalidUsernameException;
import java.util.List;
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
public class ValidateServiceImpl implements ValidateService{
    
    @Autowired
    UserAccountDao userDao;
    
    @Autowired
    LookupService lookupService;

    public void validateNewAccountSettings(Set<String> violations, UserAccount user, String passwordConfirmationEntry){
        validateUsername(violations, user.getUsername());
        validatePassword(violations, user.getPassword(), passwordConfirmationEntry);
        validateEmail(violations, user.getPassword());
    }
    
    public void validateUsername(Set<String> violations, String username){
        // check for a length over 15
        if (username.length() > 15){
            violations.add("Username cannot be over 15 characters.");
        }
        validateUsernameDoesNotExist(violations, username);
    }
    
    private void validateUsernameDoesNotExist(Set<String> violations, String username){
        Set<String> usernames = userDao.getAllUserAccounts()
                .stream().map(UserAccount::getUsername).collect(Collectors.toSet());
        if (usernames.contains(username)){
            violations.add("Username already exists.");
        }
    }
    
    public void validatePassword(Set<String> violations, String password, String passwordConfirmationEntry){
        /* ^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,50}$ matches strings with at least 8 characters, 
        no more than 50 characters, and at least one lowercase letter, uppercase letter, and
        number each */
        
        // TODO: split this up so regex only checks for uppercase/lowercase/number inclusion, not casing
        if (!Pattern.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,50}$", password)){
            violations.add("Password must be between 8-50 characters, as well as "
                    + "contain at least 1 uppercase letter, 1 lowercase letter, and 1 number.");
        }
    }
    
    public void validateEmail(Set<String> violations, String email){
        // TODO: fix email regex. Works in tester, doesn't work here. Probably character escape issue.
        
//        if (!Pattern.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", email)){
//            throw new InvalidEmailException("Email address is invalid.");
//        }
        validateEmailDoesNotExist(violations, email);
    }
    
    private void validateEmailDoesNotExist(Set<String> violations, String email){
        Set<String> emails = userDao.getAllUserAccounts()
                .stream().map(UserAccount::getEmail).collect(Collectors.toSet());
        if (emails.contains(email)){
            violations.add("Email is already registered.");
        }
    }
    
    // compiler implicitly creates an array in the parameter list when this is called
    public void validateMetricTypes(int userId, MetricType... types) throws InvalidMetricTypeException{
        for (MetricType type : types){
            if (type.getMetricName() == null){
                throw new InvalidMetricTypeException("Metric type's name cannot be empty.");
            }
            if (type.getUser() == null){
                throw new InvalidMetricTypeException("Metric type must be associated with a user.");
            }
            
            if ((type.getScale() == 0) && (type.getUnit() == null)){
                throw new InvalidMetricTypeException("A metric type's scale and unit cannot both be empty.");
            }
            List<MetricType> typesForUser = lookupService.getMetricTypesForUser(userId);
            for (MetricType existingType : typesForUser){
                if (existingType.getMetricName().equalsIgnoreCase(type.getMetricName())){
                    throw new InvalidMetricTypeException("Metric type already exists for user.");
                }
            }
        }
    }

    public void validateMetricEntry(MetricEntry entry) throws InvalidEntryException, InvalidMetricTypeException {
        float value = entry.getMetricValue();
        MetricType type = entry.getMetricType();
        int scale = type.getScale();
        // check if entry has metricType
        if (type == null){
            throw new InvalidEntryException("Entry has no associated type.");
        }
        // check that type exists for the user
        if (type.getUser() == null){
            throw new InvalidMetricTypeException("Type of entry is not associated with user.");
        }
        // check that entry has a metricValue
        if (value <= 0){
            throw new InvalidEntryException("Entry must contain a positive value.");
        }
        // if entry has subjective type, check that its value is between 1 and the scale
        if (checkIfSubjective(entry)){
            if (!(value > 1 && value <= scale)){
                throw new InvalidEntryException("Entry value must be between 1 and " + scale);
            }
        }
    }
    
    private boolean checkIfSubjective(MetricEntry entry){
        // ints can't be null, default to 0 if not explicitly initialized
        if (entry.getMetricType().getScale() == 0){
            return false;
        }
        return true;
    }
    
    // VALIDATIONS THAT THROW EXCEPTIONS
    public void validateNewAccountSettings(UserAccount user) throws InvalidUsernameException, InvalidPasswordException, InvalidEmailException{
        validateUsername(user.getUsername());
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
                .stream().map(UserAccount::getUsername).collect(Collectors.toSet());
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
}
