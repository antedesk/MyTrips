package it.antedesk.mytrips.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import static android.arch.persistence.room.ForeignKey.CASCADE;
import static android.arch.persistence.room.ForeignKey.NO_ACTION;

@Entity(tableName = "check_ins",
        indices = { @Index(value = {"id"}, unique = true) })
public class CheckIn implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private double latitude;
    private double longitude;
    private String address;
    private String city;
    private String country;

    @Ignore
    public CheckIn(double latitude, double longitude, String address, String city, String country) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.city = city;
        this.country = country;
    }

    public CheckIn(int id, double latitude, double longitude, String address, String city, String country) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.city = city;
        this.country = country;
    }

    @Ignore
    protected CheckIn(Parcel in) {
        id = in.readInt();
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(country);
    }
}
