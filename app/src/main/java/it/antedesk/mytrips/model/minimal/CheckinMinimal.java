package it.antedesk.mytrips.model.minimal;


import android.arch.persistence.room.ColumnInfo;

public class CheckinMinimal {
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
}