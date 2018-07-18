package it.antedesk.mytrips.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "users")
public class User implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String surname;
    private String bio;
    @ColumnInfo(name = "picture_url")
    public String pictureUrl;
    private CheckIn home;

    @Ignore
    public User(String name, String surname, String bio, String pictureUrl, CheckIn home) {
        this.name = name;
        this.surname = surname;
        this.bio = bio;
        this.pictureUrl = pictureUrl;
        this.home = home;
    }

    public User(int id, String name, String surname, String bio, String pictureUrl, CheckIn home) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.bio = bio;
        this.pictureUrl = pictureUrl;
        this.home = home;
    }

    @Ignore
    protected User(Parcel in) {
        id = in.readInt();
        name = in.readString();
        surname = in.readString();
        bio = in.readString();
        pictureUrl = in.readString();
        home = in.readParcelable(CheckIn.class.getClassLoader());
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

    public CheckIn getHome() {
        return home;
    }

    public void setHome(CheckIn home) {
        this.home = home;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeString(bio);
        dest.writeString(pictureUrl);
        dest.writeParcelable(home, flags);
    }
}
