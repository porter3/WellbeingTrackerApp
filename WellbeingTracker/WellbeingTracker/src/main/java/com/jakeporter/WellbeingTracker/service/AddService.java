package com.jakeporter.WellbeingTracker.service;

import com.jakeporter.WellbeingTracker.entities.DayLog;
import com.jakeporter.WellbeingTracker.entities.MetricEntry;
import com.jakeporter.WellbeingTracker.entities.MetricType;
import com.jakeporter.WellbeingTracker.entities.UserAccount;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author jake
 */
public interface AddService {

    public UserAccount createNewAccount(UserAccount user);
    public List<MetricType> addMetricTypes(MetricType... types);
    public MetricType addMetricType(MetricType type);
    public DayLog addDayLog(DayLog log);
    public MetricEntry addMetricEntry(MetricEntry entry);
    public void fillDayLogGaps(int userId);
    public UserAccount populateNewUserFromForm(HttpServletRequest request);
}
