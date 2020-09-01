package com.jakeporter.WellbeingTracker.service;

import com.jakeporter.WellbeingTracker.entities.MetricEntry;
import com.jakeporter.WellbeingTracker.entities.MetricType;
import com.jakeporter.WellbeingTracker.entities.UserAccount;
import com.jakeporter.WellbeingTracker.exceptions.InvalidEmailException;
import com.jakeporter.WellbeingTracker.exceptions.InvalidEntryException;
import com.jakeporter.WellbeingTracker.exceptions.InvalidMetricTypeException;
import com.jakeporter.WellbeingTracker.exceptions.InvalidPasswordException;
import com.jakeporter.WellbeingTracker.exceptions.InvalidUsernameException;
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
