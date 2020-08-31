package com.jakeporter.WellbeingTrackerAPI.data;

import com.jakeporter.WellbeingTrackerAPI.entities.DayLog;
import java.util.List;

/**
 *
 * @author jake
 */
public interface DayLogDao {

    public DayLog addDayLog(DayLog dayLog);
    
    public DayLog getDayLogById(int dayLogId);
    
    public List<DayLog> getAllDayLogs();
    
    public DayLog updateDayLog(DayLog updatedDayLog);
    
    public void deleteDayLog(int dayLogId);
}
