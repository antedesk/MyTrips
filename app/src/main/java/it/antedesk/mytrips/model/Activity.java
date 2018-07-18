package it.antedesk.mytrips.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

@Entity(tableName = "activities")
public class Activity implements Parcelable{

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    @ColumnInfo(name = "date_time")
    private Date dateTime;
    private String category;
    private double budget;
    private String currency;
    @ColumnInfo(name = "check_in")
    private CheckIn checkIn;

    @Ignore
    public Activity(String title, String description, Date dateTime, String category, double budget, String currency, CheckIn checkIn) {
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
        this.category = category;
        this.budget = budget;
        this.currency = currency;
        this.checkIn = checkIn;
    }

    public Activity(int id, String title, String description, Date dateTime, String category, double budget, String currency, CheckIn checkIn) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
        this.category = category;
        this.budget = budget;
        this.currency = currency;
        this.checkIn = checkIn;
    }

    @Ignore
    protected Activity(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
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

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(category);
        dest.writeDouble(budget);
        dest.writeString(currency);
        dest.writeParcelable(checkIn, flags);
    }
}
