package it.antedesk.mytrips.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

@Entity(tableName = "diaries")
public class Diary implements Parcelable{

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String description;
    @ColumnInfo(name = "start_date")
    private Date startDate;
    @ColumnInfo(name = "end_date")
    private Date endDate;
    private double budget;
    private String currency;
    private String category;
    @ColumnInfo(name = "is_plan")
    private boolean isPlan;
    private List<Activity> activities;

    //ignore annotation is used to avoid that room use this constructor
    @Ignore
    public Diary(String name, String description, Date startDate, Date endDate, double budget, String currency, String category, boolean isPlan) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
        this.currency = currency;
        this.category = category;
        this.isPlan = isPlan;
    }

    public Diary(int id, String name, String description, Date startDate, Date endDate, double budget, String currency, String category, boolean isPlan) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
        this.currency = currency;
        this.category = category;
        this.isPlan = isPlan;
    }

    protected Diary(Parcel in) {
        name = in.readString();
        description = in.readString();
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

    //TODO add start/end data
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeDouble(budget);
        dest.writeString(currency);
        dest.writeString(category);
        dest.writeByte((byte) (isPlan ? 1 : 0));
        dest.writeTypedList(activities);
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
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
}