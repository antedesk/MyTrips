package it.antedesk.mytrips.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CheckIn implements Parcelable {
    private double latitude;
    private double longitude;
    private String address;
    private String city;
    private String country;

    public CheckIn(double latitude, double longitude, String address, String city, String country) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.city = city;
        this.country = country;
    }

    protected CheckIn(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
        address = in.readString();
        city = in.readString();
        country = in.readString();
    }

    public static final Creator<CheckIn> CREATOR = new Creator<CheckIn>() {
        @Override
        public CheckIn createFromParcel(Parcel in) {
            return new CheckIn(in);
        }

        @Override
        public CheckIn[] newArray(int size) {
            return new CheckIn[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(address);
        parcel.writeString(city);
        parcel.writeString(country);
    }

    @Override
    public String toString() {
        return "CheckIn{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
