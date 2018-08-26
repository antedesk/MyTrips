package it.antedesk.mytrips.model.minimal;

import android.arch.persistence.room.ColumnInfo;

import java.util.Date;

public class DailyBudget {

    @ColumnInfo(name = "date_time")
    private Date dateTime;
    private double budget;
    private String currency;

    public DailyBudget(Date dateTime, double budget,String currency) {
        this.dateTime = dateTime;
        this.budget = budget;
        this.currency = currency;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "DailyBudget{" +
                "dateTime=" + dateTime +
                ", budget=" + budget +
                ", currency='" + currency + '\'' +
                '}';
    }
}
