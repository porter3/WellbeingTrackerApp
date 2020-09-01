package com.jakeporter.WellbeingTracker.data;

import com.jakeporter.WellbeingTracker.entities.MetricEntry;
import java.util.List;

/**
 *
 * @author jake
 */
public interface MetricEntryDao {

    public MetricEntry addMetricEntry(MetricEntry entry);
    
    public MetricEntry getMetricEntryById(int entryId);
    
    public List<MetricEntry> getAllMetricEntriesSorted();
    
    public MetricEntry editMetricEntry(MetricEntry updatedEntry);
    
    public void deleteMetricEntry(int entryId);
}
