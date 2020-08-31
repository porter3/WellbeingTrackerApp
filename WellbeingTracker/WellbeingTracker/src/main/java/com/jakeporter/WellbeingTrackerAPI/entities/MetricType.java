package com.jakeporter.WellbeingTrackerAPI.entities;

import java.util.Objects;

/**
 *
 * @author jake
 */
public class MetricType {
    private int metricTypeId;
    private UserAccount user;
    private String metricName;
    private int scale;
    private String unit;

    public int getMetricTypeId() {
        return metricTypeId;
    }

    public void setMetricTypeId(int metricTypeId) {
        this.metricTypeId = metricTypeId;
    }

    public UserAccount getUser() {
        return user;
    }

    public void setUser(UserAccount user) {
        this.user = user;
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + this.metricTypeId;
        hash = 79 * hash + Objects.hashCode(this.user);
        hash = 79 * hash + Objects.hashCode(this.metricName);
        hash = 79 * hash + this.scale;
        hash = 79 * hash + Objects.hashCode(this.unit);
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
        final MetricType other = (MetricType) obj;
        if (this.metricTypeId != other.metricTypeId) {
            return false;
        }
        if (this.scale != other.scale) {
            return false;
        }
        if (!Objects.equals(this.metricName, other.metricName)) {
            return false;
        }
        if (!Objects.equals(this.unit, other.unit)) {
            return false;
        }
        if (!Objects.equals(this.user, other.user)) {
            return false;
        }
        return true;
    }
}
