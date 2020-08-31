package com.jakeporter.WellbeingTrackerAPI.data;

import com.jakeporter.WellbeingTrackerAPI.entities.MetricEntry;
import com.jakeporter.WellbeingTrackerAPI.entities.MetricType;
import com.jakeporter.WellbeingTrackerAPI.service.LookupServiceImpl;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class MetricTypeDaoDBImpl implements MetricTypeDao{

    @Autowired
    private JdbcTemplate jdbc;
    
    // must be static for use in MetricTypeMapper
    @Autowired
    UserAccountDao userDao;
    
    
    public final class MetricTypeMapper implements RowMapper<MetricType>{

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
        final String INSERT_METRIC_TYPE = "INSERT INTO MetricType(useraccountid, metricname, scale, unit)"
                + " VALUES(?,?,?,?)";
        jdbc.update(INSERT_METRIC_TYPE, type.getUser().getUserAccountId(), type.getMetricName(),
                type.getScale(), type.getUnit());
        
        // set MetricType's ID
        type.setMetricTypeId(jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class));
        return type;
    }

    @Override
    public MetricType getMetricTypeById(int typeId) {
        final String SELECT_METRICTYPE_BY_ID = "SELECT * FROM MetricType WHERE metrictypeid = ?";
        MetricType type = new MetricType();
        try{
            type = jdbc.queryForObject(SELECT_METRICTYPE_BY_ID, new MetricTypeMapper(), typeId);
        }
        catch(EmptyResultDataAccessException e){
            type = null;
        }
        return type;
    }

    @Override
    public List<MetricType> getAllMetricTypes() {
        final String SELECT_ALL_METRIC_TYPES = "SELECT * FROM MetricType";
        return jdbc.query(SELECT_ALL_METRIC_TYPES, new MetricTypeMapper());
    }

    @Override
    public MetricType editMetricType(MetricType updatedType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteMetricType(int typeId) {
        final String DELETE_TYPE = "DELETE FROM MetricType WHERE metrictypeid = ?";
        jdbc.update(DELETE_TYPE, typeId);
    }

}
