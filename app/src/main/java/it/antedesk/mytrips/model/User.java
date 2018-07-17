package it.antedesk.mytrips.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    public String name;
    public String surname;
    public String bio;
    public CheckIn home;

    public User(String name, String surname, String bio, CheckIn home) {
        this.name = name;
        this.surname = surname;
        this.bio = bio;
        this.home = home;
    }


    protected User(Parcel in) {
        name = in.readString();
        surname = in.readString();
        bio = in.readString();
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
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", bio='" + bio + '\'' +
                ", home=" + home +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(surname);
        parcel.writeString(bio);
        parcel.writeParcelable(home, i);
    }
}
