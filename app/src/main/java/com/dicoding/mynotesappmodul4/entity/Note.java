package com.dicoding.mynotesappmodul4.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {
    private int id;
    private String title, descripton, date;

    // constructor
    public Note() {
        // require empty constructor
    }

    // constructor
    public Note(int id, String title, String descripton, String date) {
        this.id = id;
        this.title = title;
        this.descripton = descripton;
        this.date = date;
    }

    // getter
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescripton() {
        return descripton;
    }

    public String getDate() {
        return date;
    }

    // setter
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescripton(String descripton) {
        this.descripton = descripton;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(descripton);
        dest.writeString(date);
    }

    private Note(Parcel in) {
        id = in.readInt();
        title = in.readString();
        descripton = in.readString();
        date = in.readString();
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
}
