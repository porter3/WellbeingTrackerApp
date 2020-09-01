package com.jakeporter.WellbeingTrackerAPI.entities;

import java.time.LocalDate;
import java.util.Objects;

/**
 *
 * @author jake
 */
public class DayLog implements Comparable<DayLog>{
    private int dayLogId;
    private UserAccount user;
    private LocalDate logDate;
    private String notes;

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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.dayLogId;
        hash = 97 * hash + Objects.hashCode(this.user);
        hash = 97 * hash + Objects.hashCode(this.logDate);
        hash = 97 * hash + Objects.hashCode(this.notes);
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
        if (!Objects.equals(this.notes, other.notes)) {
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

    @Override
    public String toString() {
        return "DayLog{" + "dayLogId=" + dayLogId + ", user=" + user + ", logDate=" + logDate + ", notes=" + notes + '}';
    }

    @Override
    public int compareTo(DayLog otherDayLog) {
        return (this.getLogDate().compareTo(otherDayLog.getLogDate()));
    }
    
    
}
