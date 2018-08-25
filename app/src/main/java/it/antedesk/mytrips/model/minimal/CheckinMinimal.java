package it.antedesk.mytrips.model.minimal;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Ignore;
import android.os.Parcel;
import android.os.Parcelable;

public class CheckinMinimal implements Parcelable {
    private long id;
    private String title;
    private String category;
    @ColumnInfo(name = "category_id")
    private String categoryId;
    private double latitude;
    private double longitude;
    private String address;
    private String city;
    private String country;
    @ColumnInfo(name = "country_code")
    private String countryCode;

    public CheckinMinimal(long id, String title, String category, String categoryId, double latitude, double longitude, String address,
                          String city, String country, String countryCode) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.categoryId = categoryId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.city = city;
        this.country = country;
        this.countryCode = countryCode;
    }

    @Ignore
    public CheckinMinimal(){}

    @Ignore
    protected CheckinMinimal(Parcel in) {
        id = in.readLong();
        title = in.readString();
        category = in.readString();
        categoryId = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        address = in.readString();
        city = in.readString();
        country = in.readString();
        countryCode = in.readString();
    }

    public static final Creator<CheckinMinimal> CREATOR = new Creator<CheckinMinimal>() {
        @Override
        public CheckinMinimal createFromParcel(Parcel in) {
            return new CheckinMinimal(in);
        }

        @Override
        public CheckinMinimal[] newArray(int size) {
            return new CheckinMinimal[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
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
        dest.writeString(title);
        dest.writeString(category);
        dest.writeString(categoryId);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(country);
        dest.writeString(countryCode);
    }
}