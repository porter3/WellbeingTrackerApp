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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MetricEntry getMetricEntryById(int entryId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public List<MetricEntry> getAllMetricEntries(){
        final String SELECT_ALL_METRIC_ENTRIES = "SELECT * FROM metricentry";
        return jdbc.query(SELECT_ALL_METRIC_ENTRIES, new MetricEntryMapper());
    }

    @Override
    public MetricEntry editMetricEntry(MetricEntry updatedEntry) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteMetricEntry(int entryId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
        public final class MetricEntryMapper implements RowMapper<MetricEntry>{

        @Override
        public MetricEntry mapRow(ResultSet rs, int i) throws SQLException {
            MetricEntry entry = new MetricEntry();
            entry.setMetricEntryId(rs.getInt("metricentryid"));
            entry.setDayLog(logDao.getDayLogById(rs.getInt("daylogid")));
            entry.setMetricType(typeDao.getMetricTypeById(rs.getInt("metrictypeid")));
            entry.setMetricValue(rs.getInt("metricvalue"));
            System.out.println("METRIC ENTRY TIME: " + rs.getString("entrytime"));
            entry.setEntryTime(Time.valueOf(rs.getString("entrytime")));
            return entry;
        }
    }


}
