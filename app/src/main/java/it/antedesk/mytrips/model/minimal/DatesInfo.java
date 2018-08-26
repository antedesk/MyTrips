package it.antedesk.mytrips.model.minimal;

import android.arch.persistence.room.ColumnInfo;

public class DatesInfo {
    @ColumnInfo(name = "current_days")
    private int currentDays;
    @ColumnInfo(name = "total_days")
    private int totalDays;

    public DatesInfo(int currentDays, int totalDays) {
        this.currentDays = currentDays;
        this.totalDays = totalDays;
    }

    public int getCurrentDays() {
        return currentDays;
    }

    public void setCurrentDays(int currentDays) {
        this.currentDays = currentDays;
    }

    public int getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }
}
