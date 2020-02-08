package com.jakeporter.WellbeingTrackerAPI.data;

import com.jakeporter.WellbeingTrackerAPI.entities.MetricEntry;
import java.util.List;

/**
 *
 * @author jake
 */
public interface MetricEntryDao {

    public MetricEntry addMetricEntry(MetricEntry entry);
    
    public MetricEntry getMetricEntryById(int entryId);
    
    public List<MetricEntry> getAllMetricEntries();
    
    public MetricEntry editMetricEntry(MetricEntry updatedEntry);
    
    public void deleteMetricEntry(int entryId);
}
