package it.antedesk.mytrips.model.minimal;

public class CategoryBudget {

    private double budget;
    private String currency;
    private String category;

    public CategoryBudget(double budget, String currency, String category) {
        this.budget = budget;
        this.currency = currency;
        this.category = category;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
