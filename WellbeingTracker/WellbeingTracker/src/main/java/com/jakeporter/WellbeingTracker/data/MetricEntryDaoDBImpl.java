package com.jakeporter.WellbeingTracker.data;

import com.jakeporter.WellbeingTracker.entities.MetricEntry;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jake
 */

@Repository
public class MetricEntryDaoDBImpl implements MetricEntryDao{
    
    @Autowired
    private JdbcTemplate jdbc;
    
    @Autowired
    DayLogDao logDao;
    
    @Autowired
    MetricTypeDao typeDao;

    @Override
    public MetricEntry addMetricEntry(MetricEntry entry) {
        final String INSERT_METRIC_ENTRY = "INSERT INTO MetricEntry"
                + "(daylogid, metrictypeid, metricvalue, entrytime) VALUES"
                + "(?, ?, ?, ?)";
        jdbc.update(INSERT_METRIC_ENTRY, entry.getDayLog().getDayLogId(), 
                entry.getMetricType().getMetricTypeId(), entry.getMetricValue(),
                entry.getEntryTime());
        // set ID
        entry.setMetricEntryId(jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class));
        return entry;
    }

    @Override
    public MetricEntry getMetricEntryById(int entryId) {
        final String SELECT_ENTRY_BY_ID = "SELECT * FROM MetricEntry WHERE metricentryid = ?";
        MetricEntry entry = new MetricEntry();
        try{
            entry = jdbc.queryForObject(SELECT_ENTRY_BY_ID, new MetricEntryMapper(), entryId);
        }
        catch(EmptyResultDataAccessException e){
            entry = null;
        }
        return entry;
    }
    
    @Override
    public List<MetricEntry> getAllMetricEntriesSorted(){
        final String SELECT_ALL_METRIC_ENTRIES = "SELECT * FROM MetricEntry INNER JOIN DayLog ON MetricEntry.daylogid = DayLog.DayLogId ORDER BY logdate, metricentryid";
        return jdbc.query(SELECT_ALL_METRIC_ENTRIES, new MetricEntryMapper());
    }

    @Override
    public MetricEntry editMetricEntry(MetricEntry updatedEntry) {
        final String UPDATE_ENTRY = "UPDATE MetricEntry SET daylogid = ?, metrictypeid = ?, metricvalue = ?, entrytime = ? WHERE metricentryid = ?";
        jdbc.update(UPDATE_ENTRY, updatedEntry.getDayLog().getDayLogId(), updatedEntry.getMetricType().getMetricTypeId(),
                updatedEntry.getMetricValue(), updatedEntry.getEntryTime(), updatedEntry.getMetricEntryId());
        return getMetricEntryById(updatedEntry.getMetricEntryId());
    }

    @Override
    public void deleteMetricEntry(int entryId) {
        final String DELETE_ENTRY = "DELETE FROM MetricEntry WHERE metricentryid = ?";
        jdbc.update(DELETE_ENTRY, entryId);
    }
    
        public final class MetricEntryMapper implements RowMapper<MetricEntry>{

        @Override
        public MetricEntry mapRow(ResultSet rs, int i) throws SQLException {
            MetricEntry entry = new MetricEntry();
            entry.setMetricEntryId(rs.getInt("metricentryid"));
            entry.setDayLog(logDao.getDayLogById(rs.getInt("daylogid")));
            entry.setMetricType(typeDao.getMetricTypeById(rs.getInt("metrictypeid")));
            entry.setMetricValue(rs.getFloat("metricvalue"));
            entry.setEntryTime(Time.valueOf(rs.getString("entrytime")));
            return entry;
        }
    }


}
