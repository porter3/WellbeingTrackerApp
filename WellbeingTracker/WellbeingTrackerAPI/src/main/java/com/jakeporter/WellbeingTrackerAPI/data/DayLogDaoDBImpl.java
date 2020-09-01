package com.jakeporter.WellbeingTrackerAPI.data;

import com.jakeporter.WellbeingTrackerAPI.entities.DayLog;
import com.jakeporter.WellbeingTrackerAPI.entities.UserAccount;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jake
 */

@Repository
public class DayLogDaoDBImpl implements DayLogDao{
    
    @Autowired
    JdbcTemplate jdbc;
    
    @Autowired
    UserAccountDao userDao;
    
    public final class DayLogMapper implements RowMapper<DayLog>{

        @Override
        public DayLog mapRow(ResultSet rs, int i) throws SQLException {
            DayLog log = new DayLog();
            log.setDayLogId(rs.getInt("daylogid"));
            log.setUser(userDao.getUserAccountById(rs.getInt("useraccountid")));
            log.setLogDate(LocalDate.parse(rs.getString("logdate"), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            log.setNotes(rs.getString("notes"));
            return log;
        }
    }

    @Override
    public DayLog addDayLog(DayLog dayLog) {
        final String INSERT_DAYLOG = "INSERT INTO DayLog(useraccountid, logdate, notes)"
                + " VALUES (?, ?, ?)";
        jdbc.update(INSERT_DAYLOG, dayLog.getUser().getUserAccountId(), dayLog.getLogDate(),
                dayLog.getNotes());
        
        // set ID
        dayLog.setDayLogId(jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class));
        return dayLog;
    }

    @Override
    public DayLog getDayLogById(int dayLogId) {
        final String SELECT_DAYLOG_BY_ID = "SELECT * FROM DayLog WHERE daylogid = ?";
        DayLog log = new DayLog();
        try{
            log = jdbc.queryForObject(SELECT_DAYLOG_BY_ID, new DayLogMapper(), dayLogId);
        }
        catch(EmptyResultDataAccessException e){
            log = null;
        }
        return log;
    }

    @Override
    public List<DayLog> getAllDayLogs() {
        final String SELECT_ALL_DAYLOGS = "SELECT * FROM DayLog";
        return jdbc.query(SELECT_ALL_DAYLOGS, new DayLogMapper());
    }

    @Override
    public DayLog updateDayLog(DayLog updatedDayLog) {
        final String UPDATE_DAYLOG = "UPDATE DayLog SET useraccountid = ?, logdate = ?, notes = ? WHERE daylogid = ?";
        jdbc.update(UPDATE_DAYLOG, updatedDayLog.getUser().getUserAccountId(), updatedDayLog.getLogDate(), 
                updatedDayLog.getNotes(), updatedDayLog.getDayLogId());
        return getDayLogById(updatedDayLog.getDayLogId());
    }

    @Override
    public void deleteDayLog(int dayLogId) {
        final String DELETE_DAYLOG = "DELETE FROM DayLog WHERE daylogid = ?";
        jdbc.update(DELETE_DAYLOG, dayLogId);
    }
    

}
