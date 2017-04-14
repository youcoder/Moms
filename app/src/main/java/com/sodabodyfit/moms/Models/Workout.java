package com.sodabodyfit.moms.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Workout implements Parcelable {

    // table name
    public static final String TABLE = "Workout";

    // table columns names
    public static final String KEY_ID = "id";
    public static final String KEY_category = "category_id";
    public static final String KEY_title = "title";
    public static final String KEY_title_dut = "title_dut";
    public static final String KEY_info = "info";
    public static final String KEY_info_dut = "info_dut";
    public static final String KEY_infoDisplayed = "infoDisplayed";
    public static final String KEY_image = "image";
    public static final String KEY_exercises = "exercises";
    public static final String KEY_creationDate = "creationDate";

    // property
    public int workout_id;
    public int category_id;
    public String title;
    public String info;
    public Boolean infoDisplayed;
    public String image;
    public String exercises;
    public String creationDate;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.workout_id);
        dest.writeInt(this.category_id);
        dest.writeString(this.title);
        dest.writeString(this.info);
        dest.writeValue(this.infoDisplayed);
        dest.writeString(this.image);
        dest.writeString(this.exercises);
        dest.writeString(this.creationDate);
    }

    public Workout() {
    }

    protected Workout(Parcel in) {
        this.workout_id = in.readInt();
        this.category_id = in.readInt();
        this.title = in.readString();
        this.info = in.readString();
        this.infoDisplayed = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.image = in.readString();
        this.exercises = in.readString();
        this.creationDate = in.readString();
    }

    public static final Parcelable.Creator<Workout> CREATOR = new Parcelable.Creator<Workout>() {
        @Override
        public Workout createFromParcel(Parcel source) {
            return new Workout(source);
        }

        @Override
        public Workout[] newArray(int size) {
            return new Workout[size];
        }
    };
}
