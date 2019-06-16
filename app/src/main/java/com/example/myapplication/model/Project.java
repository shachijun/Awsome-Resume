package com.example.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Project extends ID implements Parcelable {

    public String title;

    public String language;

    public List<String> description;

    public Project(){
        super();
    }

    protected Project(Parcel in) {
        id = in.readString();
        title = in.readString();
        language = in.readString();
        description = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(language);
        dest.writeStringList(description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };
}
