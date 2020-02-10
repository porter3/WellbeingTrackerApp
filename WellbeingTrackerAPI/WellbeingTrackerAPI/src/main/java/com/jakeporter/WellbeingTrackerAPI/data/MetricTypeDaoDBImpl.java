package com.jakeporter.WellbeingTrackerAPI.data;

import com.jakeporter.WellbeingTrackerAPI.entities.MetricType;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class MetricTypeDaoDBImpl implements MetricTypeDao{

    @Autowired
    private JdbcTemplate jdbc;
    
    // must be static for use in MetricTypeMapper
    @Autowired
    private static UserAccountDao userDao;
    
    public static final class MetricTypeMapper implements RowMapper<MetricType>{

        @Override
        public MetricType mapRow(ResultSet rs, int i) throws SQLException {
            MetricType type = new MetricType();
            type.setMetricTypeId(rs.getInt("metrictypeid"));
            type.setUser(userDao.getUserAccountById(rs.getInt("useraccountid")));
            type.setMetricName(rs.getString("metricname"));
            type.setScale(rs.getInt("scale"));
            type.setUnit(rs.getString("unit"));
            return type;
        }
        
    }
    
    @Override
    public MetricType addMetricType(MetricType type) {
        final String INSERT_METRIC_TYPE = "INSERT INTO metrictype(useraccountid, metricname, scale, unit)"
                + " VALUES(?,?,?,?)";
        jdbc.update(INSERT_METRIC_TYPE, type.getUser().getUserAccountId(), type.getMetricName(),
                type.getScale(), type.getUnit());
        
        // set MetricType's ID
        type.setMetricTypeId(jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class));
        return type;
    }

    @Override
    public MetricType getMetricTypeById(int typeId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MetricType> getAllMetricTypes() {
        final String SELECT_ALL_METRIC_TYPES = "SELECT * FROM metrictype";
        return jdbc.query(SELECT_ALL_METRIC_TYPES, new MetricTypeMapper());
    }

    @Override
    public MetricType editMetricType(MetricType updatedType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteMetricType(int typeId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
