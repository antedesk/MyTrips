package it.antedesk.mytrips.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "activities",
        indices = { @Index(value = {"id"}, unique = true),
                    @Index(value = {"diary_id"}),
                    @Index(value = {"check_in_id"}) },
        foreignKeys = { @ForeignKey(entity = Diary.class,
                                    parentColumns = "id",
                                    childColumns = "diary_id",
                                    onDelete = CASCADE),
                        @ForeignKey(entity = CheckIn.class,
                                    parentColumns = "id",
                                    childColumns = "check_in_id")})
public class Activity implements Parcelable{

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "diary_id")
    private long diaryId;
    private String title;
    private String description;
    @ColumnInfo(name = "date_time")
    private Date dateTime;
    private String category;
    private double budget;
    private String currency;
    @ColumnInfo(name = "check_in_id")
    private long checkInId;

    @Ignore
    public Activity(String title, String description, Date dateTime, String category, double budget, String currency, long checkInId) {
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
        this.category = category;
        this.budget = budget;
        this.currency = currency;
        this.checkInId = checkInId;
    }

    public Activity(long id, long diaryId, String title, String description, Date dateTime, String category, double budget, String currency, long checkInId) {
        this.id = id;
        this.diaryId = diaryId;
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
        this.category = category;
        this.budget = budget;
        this.currency = currency;
        this.checkInId = checkInId;
    }

    @Ignore
    protected Activity(Parcel in) {
        id = in.readLong();
        diaryId = in.readLong();
        title = in.readString();
        description = in.readString();
        category = in.readString();
        budget = in.readDouble();
        currency = in.readString();
        checkInId = in.readLong();
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDiaryId() {
        return diaryId;
    }

    public void setDiaryId(long diaryId) {
        this.diaryId = diaryId;
    }

    public long getCheckInId() {
        return checkInId;
    }

    public void setCheckInId(long checkInId) {
        this.checkInId = checkInId;
    }

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
        dest.writeLong(id);
        dest.writeLong(diaryId);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(category);
        dest.writeDouble(budget);
        dest.writeString(currency);
        dest.writeLong(checkInId);
    }
}
