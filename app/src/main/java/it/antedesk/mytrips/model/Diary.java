package it.antedesk.mytrips.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Diary implements Parcelable {

    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private double budget;
    private String currency;
    private String category;
    private boolean isPlan;
    private List<Activity> activities;


    protected Diary(Parcel in) {
        name = in.readString();
        description = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        budget = in.readDouble();
        currency = in.readString();
        category = in.readString();
        isPlan = in.readByte() != 0;
        activities = in.createTypedArrayList(Activity.CREATOR);
    }

    public static final Creator<Diary> CREATOR = new Creator<Diary>() {
        @Override
        public Diary createFromParcel(Parcel in) {
            return new Diary(in);
        }

        @Override
        public Diary[] newArray(int size) {
            return new Diary[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(startDate);
        parcel.writeString(endDate);
        parcel.writeDouble(budget);
        parcel.writeString(currency);
        parcel.writeString(category);
        parcel.writeByte((byte) (isPlan ? 1 : 0));
        parcel.writeTypedList(activities);
    }

    public Diary(String name, String description, String startDate, String endDate, double budget, String currency, String category, boolean isPlan) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
        this.currency = currency;
        this.category = category;
        this.isPlan = isPlan;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    public boolean isPlan() {
        return isPlan;
    }

    public void setPlan(boolean plan) {
        isPlan = plan;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    @Override
    public String toString() {
        return "Diary{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", budget=" + budget +
                ", currency='" + currency + '\'' +
                ", category='" + category + '\'' +
                ", isPlan=" + isPlan +
                '}';
    }
}
