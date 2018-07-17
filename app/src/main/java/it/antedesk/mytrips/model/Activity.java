package it.antedesk.mytrips.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Activity implements Parcelable {

    private String title;
    private String description;
    private String dateTime;
    private String category;
    private double budget;
    private String currency;
    private CheckIn checkIn;

    public Activity(String title, String description, String dateTime, String category, double budget, String currency, CheckIn checkIn) {
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
        this.category = category;
        this.budget = budget;
        this.currency = currency;
        this.checkIn = checkIn;
    }

    protected Activity(Parcel in) {
        title = in.readString();
        description = in.readString();
        dateTime = in.readString();
        category = in.readString();
        budget = in.readDouble();
        currency = in.readString();
        checkIn = in.readParcelable(CheckIn.class.getClassLoader());
    }

    public static final Creator<Activity> CREATOR = new Creator<Activity>() {
        @Override
        public Activity createFromParcel(Parcel in) {
            return new Activity(in);
        }

        @Override
        public Activity[] newArray(int size) {
            return new Activity[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
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

    @Override
    public String toString() {
        return "Activity{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", category='" + category + '\'' +
                ", budget=" + budget +
                ", currency='" + currency + '\'' +
                ", checkIn=" + checkIn +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(dateTime);
        parcel.writeString(category);
        parcel.writeDouble(budget);
        parcel.writeString(currency);
        parcel.writeParcelable(checkIn, i);
    }
}
