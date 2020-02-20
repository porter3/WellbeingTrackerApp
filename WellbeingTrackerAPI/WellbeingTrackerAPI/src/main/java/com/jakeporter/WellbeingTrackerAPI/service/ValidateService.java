package com.jakeporter.WellbeingTrackerAPI.service;

import com.jakeporter.WellbeingTrackerAPI.entities.MetricEntry;
import com.jakeporter.WellbeingTrackerAPI.entities.MetricType;
import com.jakeporter.WellbeingTrackerAPI.entities.UserAccount;
import com.jakeporter.WellbeingTrackerAPI.exceptions.InvalidEmailException;
import com.jakeporter.WellbeingTrackerAPI.exceptions.InvalidEntryException;
import com.jakeporter.WellbeingTrackerAPI.exceptions.InvalidMetricTypeException;
import com.jakeporter.WellbeingTrackerAPI.exceptions.InvalidPasswordException;
import com.jakeporter.WellbeingTrackerAPI.exceptions.InvalidUsernameException;
import java.util.Set;

/**
 *
 * @author jake
 */
public interface ValidateService {

    public void validateNewAccountSettings(Set<String> violations, UserAccount user, String passwordConfirmationEntry);
    public void validateNewAccountSettings(UserAccount user) throws InvalidUsernameException, InvalidPasswordException, InvalidEmailException;
    public void validateUsername(Set<String> violations, String username);
    public void validateUsername(String username) throws InvalidUsernameException;
    public void validatePassword(Set<String> violations, String password, String passwordConfirmationEntry);
    public void validatePassword(String password) throws InvalidPasswordException;
    public void validateEmail(Set<String> violations, String email);
    public void validateEmail(String email) throws InvalidEmailException;
    public void validateMetricTypes(int userId, MetricType... types) throws InvalidMetricTypeException;
    public void validateMetricEntry(MetricEntry entry) throws InvalidEntryException, InvalidMetricTypeException;
    
}
