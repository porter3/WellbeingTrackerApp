package com.jakeporter.WellbeingTrackerAPI.entities;

import java.time.LocalDate;
import java.util.Objects;

/**
 *
 * @author jake
 */
public class DayLog {
    private int dayLogId;
    private UserAccount user;
    private LocalDate logDate;

    public int getDayLogId() {
        return dayLogId;
    }

    public void setDayLogId(int dayLogId) {
        this.dayLogId = dayLogId;
    }

    public UserAccount getUser() {
        return user;
    }

    public void setUser(UserAccount user) {
        this.user = user;
    }

    public LocalDate getLogDate() {
        return logDate;
    }

    public void setLogDate(LocalDate logDate) {
        this.logDate = logDate;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.dayLogId;
        hash = 67 * hash + Objects.hashCode(this.user);
        hash = 67 * hash + Objects.hashCode(this.logDate);
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
        final DayLog other = (DayLog) obj;
        if (this.dayLogId != other.dayLogId) {
            return false;
        }
        if (!Objects.equals(this.user, other.user)) {
            return false;
        }
        if (!Objects.equals(this.logDate, other.logDate)) {
            return false;
        }
        return true;
    }
}
