package com.jakeporter.WellbeingTrackerAPI.data;

import com.jakeporter.WellbeingTrackerAPI.entities.MetricEntry;
import com.jakeporter.WellbeingTrackerAPI.entities.UserAccount;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jake
 */

@Repository
@Profile("database")
public class MetricEntryDaoDBImpl implements MetricEntryDao{
    
    @Autowired
    private JdbcTemplate jdbc;
    
    @Autowired
    DayLogDao logDao;
    
    @Autowired
    MetricTypeDao typeDao;

    @Override
    public MetricEntry addMetricEntry(MetricEntry entry) {
        final String INSERT_METRIC_ENTRY = "INSERT INTO metricentry"
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
        final String SELECT_ENTRY_BY_ID = "SELECT * FROM metricentry WHERE metricentryid = ?";
        return jdbc.queryForObject(SELECT_ENTRY_BY_ID, new MetricEntryMapper(), entryId);
    }
    
    @Override
    public List<MetricEntry> getAllMetricEntries(){
        final String SELECT_ALL_METRIC_ENTRIES = "SELECT * FROM metricentry";
        return jdbc.query(SELECT_ALL_METRIC_ENTRIES, new MetricEntryMapper());
    }

    @Override
    public MetricEntry editMetricEntry(MetricEntry updatedEntry) {
        final String UPDATE_ENTRY = "UPDATE metricentry SET daylogid = ?, metrictypeid = ?, metricvalue = ?, entrytime = ? WHERE metricentryid = ?";
        jdbc.update(UPDATE_ENTRY, updatedEntry.getDayLog().getDayLogId(), updatedEntry.getMetricType().getMetricTypeId(),
                updatedEntry.getMetricValue(), updatedEntry.getEntryTime(), updatedEntry.getMetricEntryId());
        return getMetricEntryById(updatedEntry.getMetricEntryId());
    }

    @Override
    public void deleteMetricEntry(int entryId) {
        final String DELETE_ENTRY = "DELETE FROM metricentry WHERE metricentryid = ?";
        jdbc.update(DELETE_ENTRY, entryId);
    }
    
        public final class MetricEntryMapper implements RowMapper<MetricEntry>{

        @Override
        public MetricEntry mapRow(ResultSet rs, int i) throws SQLException {
            MetricEntry entry = new MetricEntry();
            entry.setMetricEntryId(rs.getInt("metricentryid"));
            entry.setDayLog(logDao.getDayLogById(rs.getInt("daylogid")));
            entry.setMetricType(typeDao.getMetricTypeById(rs.getInt("metrictypeid")));
            entry.setMetricValue(rs.getInt("metricvalue"));
            entry.setEntryTime(Time.valueOf(rs.getString("entrytime")));
            return entry;
        }
    }


}
