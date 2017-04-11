package com.sodabodyfit.moms.Models;

/**
 * Created by DEVMAN86 on 4/2/2017.
 */

public class Exercise {
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
    public Integer exercise_id;
    public Integer category_id;
    public Integer workout_id;
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
}
