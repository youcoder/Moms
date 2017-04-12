package com.sodabodyfit.moms.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DEVMAN86 on 4/2/2017.
 */

public class Exercise implements Parcelable {

    // table name
    public static final String TABLE = "Exercise";

    // table columns names
    public static final String KEY_ID = "id";
    public static final String KEY_category = "category_id";
    public static final String KEY_workout = "workout_id";
    public static final String KEY_title = "title";
    public static final String KEY_title_dut = "title_dut";
    public static final String KEY_initialPosition = "initialPosition";
    public static final String KEY_initialPosition_dut = "initialPosition_dut";
    public static final String KEY_movement = "movement";
    public static final String KEY_movement_dut = "movement_dut";
    public static final String KEY_points = "points";
    public static final String KEY_points_dut = "points_dut";
    public static final String KEY_sets = "sets";
    public static final String KEY_repetions = "repetions";
    public static final String KEY_times = "times";
    public static final String KEY_rest = "rest";
    public static final String KEY_kg = "kg";
    public static final String KEY_like = "like";
    public static final String KEY_images = "images";

    // property
    public int exercise_id;
    public int category_id;
    public int workout_id;
    public String title;
    public String initialPosition;
    public String movement;
    public String points;
    public String sets;
    public String repetions;
    public String times;
    public String rest;
    public String kg;
    public Boolean like;
    public String images;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.exercise_id);
        dest.writeInt(this.category_id);
        dest.writeInt(this.workout_id);
        dest.writeString(this.title);
        dest.writeString(this.initialPosition);
        dest.writeString(this.movement);
        dest.writeString(this.points);
        dest.writeString(this.sets);
        dest.writeString(this.repetions);
        dest.writeString(this.times);
        dest.writeString(this.rest);
        dest.writeString(this.kg);
        dest.writeValue(this.like);
        dest.writeString(this.images);
    }

    public Exercise() {
    }

    protected Exercise(Parcel in) {
        this.exercise_id = in.readInt();
        this.category_id = in.readInt();
        this.workout_id = in.readInt();
        this.title = in.readString();
        this.initialPosition = in.readString();
        this.movement = in.readString();
        this.points = in.readString();
        this.sets = in.readString();
        this.repetions = in.readString();
        this.times = in.readString();
        this.rest = in.readString();
        this.kg = in.readString();
        this.like = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.images = in.readString();
    }

    public static final Parcelable.Creator<Exercise> CREATOR = new Parcelable.Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel source) {
            return new Exercise(source);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };
}
