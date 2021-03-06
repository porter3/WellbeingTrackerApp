package com.jakeporter.WellbeingTracker.data;

import com.jakeporter.WellbeingTracker.entities.MetricType;
import java.util.List;

/**
 *
 * @author jake
 */
public interface MetricTypeDao {

    public MetricType addMetricType(MetricType type);
    
    public MetricType getMetricTypeById(int typeId);
    
    public List<MetricType> getAllMetricTypes();
    
    public MetricType editMetricType(MetricType updatedType);
    
    public void deleteMetricType(int typeId);
}
