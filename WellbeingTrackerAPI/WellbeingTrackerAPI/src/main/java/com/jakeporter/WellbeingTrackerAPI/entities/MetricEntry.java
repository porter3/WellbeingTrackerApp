package com.jakeporter.WellbeingTrackerAPI.entities;

import java.sql.Time;
import java.util.Objects;

/**
 *
 * @author jake
 */
public class MetricEntry {
    private int metricEntryId;
    private DayLog dayLog;
    private MetricType metricType;
    private int metricValue;
    private Time entryTime;

    public int getMetricEntryId() {
        return metricEntryId;
    }

    public void setMetricEntryId(int metricEntryId) {
        this.metricEntryId = metricEntryId;
    }

    public DayLog getDayLog() {
        return dayLog;
    }

    public void setDayLog(DayLog dayLog) {
        this.dayLog = dayLog;
    }

    public MetricType getMetricType() {
        return metricType;
    }

    public void setMetricType(MetricType metricType) {
        this.metricType = metricType;
    }

    public int getMetricValue() {
        return metricValue;
    }

    public void setMetricValue(int metricValue) {
        this.metricValue = metricValue;
    }

    public Time getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Time entryTime) {
        this.entryTime = entryTime;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + this.metricEntryId;
        hash = 89 * hash + Objects.hashCode(this.dayLog);
        hash = 89 * hash + Objects.hashCode(this.metricType);
        hash = 89 * hash + this.metricValue;
        hash = 89 * hash + Objects.hashCode(this.entryTime);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MetricEntry other = (MetricEntry) obj;
        if (this.metricEntryId != other.metricEntryId) {
            return false;
        }
        if (this.metricValue != other.metricValue) {
            return false;
        }
        if (!Objects.equals(this.dayLog, other.dayLog)) {
            return false;
        }
        if (!Objects.equals(this.metricType, other.metricType)) {
            return false;
        }
        if (!Objects.equals(this.entryTime, other.entryTime)) {
            return false;
        }
        return true;
    }
}
