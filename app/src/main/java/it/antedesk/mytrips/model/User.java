package it.antedesk.mytrips.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "users",
        indices = { @Index(value = {"home_check_in"}, unique = true) })
public class User implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String surname;
    private String bio;
    @ColumnInfo(name = "picture_url")
    private String pictureUrl;
    @ColumnInfo(name = "home_check_in")
    private long homeCheckIn;

    public User(long id, String name, String surname, String bio, String pictureUrl, long homeCheckIn) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.bio = bio;
        this.pictureUrl = pictureUrl;
        this.homeCheckIn = homeCheckIn;
    }

    @Ignore
    public User(String name, String surname, String bio, String pictureUrl, long homeCheckIn) {
        this.name = name;
        this.surname = surname;
        this.bio = bio;
        this.pictureUrl = pictureUrl;
        this.homeCheckIn = homeCheckIn;
    }

    @Ignore
    protected User(Parcel in) {
        id = in.readLong();
        name = in.readString();
        surname = in.readString();
        bio = in.readString();
        pictureUrl = in.readString();
        homeCheckIn = in.readLong();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public long getHomeCheckIn() {
        return homeCheckIn;
    }

    public void setHomeCheckIn(long homeCheckIn) {
        this.homeCheckIn = homeCheckIn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeString(bio);
        dest.writeString(pictureUrl);
        dest.writeLong(homeCheckIn);
    }
}
