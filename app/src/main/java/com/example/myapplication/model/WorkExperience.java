package com.example.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.myapplication.util.DateUtils;

import java.util.Date;
import java.util.List;

public class WorkExperience extends ID implements Parcelable {
    //this is a test

    public String title;

    public Date startDate;

    public Date endDate;

    public List<String> description;

    public WorkExperience(){
        super();
    }

    protected WorkExperience(Parcel in) {
        id = in.readString();
        title = in.readString();
        startDate = DateUtils.stringToDate(in.readString());
        endDate = DateUtils.stringToDate(in.readString());
        description = in.createStringArrayList();
    }

    public static final Creator<WorkExperience> CREATOR = new Creator<WorkExperience>() {
        @Override
        public WorkExperience createFromParcel(Parcel in) {
            return new WorkExperience(in);
        }

        @Override
        public WorkExperience[] newArray(int size) {
            return new WorkExperience[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(DateUtils.dateToString(startDate));
        parcel.writeString(DateUtils.dateToString(endDate));
        parcel.writeStringList(description);
    }
}
