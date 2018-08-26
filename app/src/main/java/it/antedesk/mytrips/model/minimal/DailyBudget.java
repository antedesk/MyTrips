package it.antedesk.mytrips.model.minimal;

import android.arch.persistence.room.ColumnInfo;

import java.util.Date;

public class DailyBudget {

    @ColumnInfo(name = "date_time")
    private Date dateTime;
    private double budget;

    public DailyBudget(Date dateTime, double budget) {
        this.dateTime = dateTime;
        this.budget = budget;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }
}
