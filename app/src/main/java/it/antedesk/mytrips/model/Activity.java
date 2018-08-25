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
                    @Index(value = {"diary_id"}) },
        foreignKeys = { @ForeignKey(entity = Diary.class,
                                    parentColumns = "id",
                                    childColumns = "diary_id",
                                    onDelete = CASCADE)})
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
    @ColumnInfo(name = "category_id")
    private String categoryId;
    private double budget;
    private String currency;
    private double latitude;
    private double longitude;
    private String address;
    private String city;
    private String country;
    @ColumnInfo(name = "country_code")
    private String countryCode;

    @Ignore
    public Activity(long diaryId, String title, String description, Date dateTime, String category,
                    String categoryId, double budget, String currency, double latitude, double longitude,
                    String address, String city, String country, String countryCode) {
        this.diaryId = diaryId;
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
        this.category = category;
        this.categoryId = categoryId;
        this.budget = budget;
        this.currency = currency;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.city = city;
        this.country = country;
        this.countryCode = countryCode;
    }

    public Activity(long id, long diaryId, String title, String description, Date dateTime, String category,
                    String categoryId, double budget, String currency, double latitude, double longitude,
                    String address, String city, String country, String countryCode) {
        this.id = id;
        this.diaryId = diaryId;
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
        this.category = category;
        this.categoryId = categoryId;
        this.budget = budget;
        this.currency = currency;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.city = city;
        this.country = country;
        this.countryCode = countryCode;
    }

    @Ignore
    public Activity() { }

    @Ignore
    protected Activity(Parcel in) {
        id = in.readLong();
        diaryId = in.readLong();
        title = in.readString();
        description = in.readString();
        category = in.readString();
        categoryId = in.readString();
        budget = in.readDouble();
        currency = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        address = in.readString();
        city = in.readString();
        country = in.readString();
        countryCode = in.readString();
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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
        dest.writeString(categoryId);
        dest.writeDouble(budget);
        dest.writeString(currency);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(country);
        dest.writeString(countryCode);
    }

}
