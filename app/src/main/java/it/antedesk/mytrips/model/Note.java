package it.antedesk.mytrips.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

@Entity(tableName = "notes",
        inheritSuperIndices = true)
public class Note extends Activity implements Parcelable {

    private String weather;
    private double temperature;

    @Ignore
    public Note(long diaryId, String title, String description, Date dateTime, String category, String categoryId, double budget, String currency, double latitude, double longitude, String address, String city, String country, String countryCode, String weather, double temperature) {
        super(diaryId, title, description, dateTime, category, categoryId, budget, currency, latitude, longitude, address, city, country, countryCode);
        this.weather = weather;
        this.temperature = temperature;
    }

    public Note(long id, long diaryId, String title, String description, Date dateTime, String category, String categoryId, double budget, String currency, double latitude, double longitude, String address, String city, String country, String countryCode, String weather, double temperature) {
        super(id, diaryId, title, description, dateTime, category, categoryId, budget, currency, latitude, longitude, address, city, country, countryCode);
        this.weather = weather;
        this.temperature = temperature;
    }

    @Ignore
    public Note(){ }

    @Ignore
    protected Note(Parcel in) {
        super(in);
        weather = in.readString();
        temperature = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(weather);
        dest.writeDouble(temperature);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
