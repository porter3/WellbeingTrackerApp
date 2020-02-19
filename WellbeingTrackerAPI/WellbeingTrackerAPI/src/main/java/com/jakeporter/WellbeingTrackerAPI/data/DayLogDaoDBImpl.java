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
@Profile("database")
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
        final String INSERT_DAYLOG = "INSERT INTO daylog(useraccountid, logdate, notes)"
                + " VALUES (?, ?, ?)";
        jdbc.update(INSERT_DAYLOG, dayLog.getUser().getUserAccountId(), dayLog.getLogDate(),
                dayLog.getNotes());
        
        // set ID
        dayLog.setDayLogId(jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class));
        return dayLog;
    }

    @Override
    public DayLog getDayLogById(int dayLogId) {
        final String SELECT_DAYLOG_BY_ID = "SELECT * FROM daylog WHERE daylogid = ?";
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
        final String SELECT_ALl_DAYLOGS = "SELECT * FROM daylog";
        return jdbc.query(SELECT_ALl_DAYLOGS, new DayLogMapper());
    }

    @Override
    public DayLog updateDayLog(DayLog updatedDayLog) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteDayLog(int dayLogId) {
        final String DELETE_DAYLOG = "DELETE FROM daylog WHERE daylogid = ?";
        jdbc.update(DELETE_DAYLOG, dayLogId);
    }
    

}
