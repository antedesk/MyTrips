package it.antedesk.mytrips.model.minimal;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Ignore;
import android.os.Parcel;
import android.os.Parcelable;

public class CheckinMinimal implements Parcelable {
    private String category;
    private double latitude;
    private double longitude;
    private String address;
    private String city;
    private String country;
    @ColumnInfo(name = "country_code")
    private String countryCode;

    public CheckinMinimal(String category, double latitude, double longitude, String address,
                          String city, String country, String countryCode) {
        this.category = category;
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
        category = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(category);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(country);
        dest.writeString(countryCode);
    }
}