package it.antedesk.mytrips.model.minimal;

import android.arch.persistence.room.ColumnInfo;

public class BudgetInfo {

    @ColumnInfo(name = "total_budget")
    private double totalBudget;
    private String currency;

    public BudgetInfo( double totalBudget, String currency) {
        this.totalBudget = totalBudget;
        this.currency = currency;
    }

    public double getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(double totalBudget) {
        this.totalBudget = totalBudget;
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
                ", totalBudget=" + totalBudget +
                ", currency='" + currency + '\'' +
                '}';
    }
}
