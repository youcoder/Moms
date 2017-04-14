package com.sodabodyfit.moms.Provider;

/**
 * Created by DEVMAN86 on 4/2/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sodabodyfit.moms.Models.Exercise;
import com.sodabodyfit.moms.Models.Image;
import com.sodabodyfit.moms.Models.Language;
import com.sodabodyfit.moms.Models.Workout;

import java.util.ArrayList;
import java.util.List;

public class DBEngine {
    private DBHelper dbHelper;

    public DBEngine(Context context) {
        dbHelper = new DBHelper(context);
    }

    // Workout
    public ArrayList<Workout> getWorkoutList(int category_id) {
        ArrayList<Workout> workoutNameList = new ArrayList<Workout>();

        int nLanguage = getSelLanguageId();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            if ( nLanguage == 1) // english
            {
                String selectQuery = "SELECT  " +
                        Workout.KEY_ID + "," +
                        Workout.KEY_category + "," +
                        Workout.KEY_title + "," +
                        Workout.KEY_info + "," +
                        Workout.KEY_infoDisplayed + "," +
                        Workout.KEY_image + "," +
                        Workout.KEY_exercises +
                        " FROM " + Workout.TABLE +
                        " WHERE " + Workout.KEY_category + "=?";

                Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(category_id)});

                if (cursor.moveToFirst()) {
                    do {
                        Workout workout = new Workout();
                        workout.workout_id = cursor.getInt(cursor.getColumnIndex(Workout.KEY_ID));
                        workout.category_id = cursor.getInt(cursor.getColumnIndex(Workout.KEY_category));
                        workout.title = cursor.getString(cursor.getColumnIndex(Workout.KEY_title)).toUpperCase();
                        workout.info = cursor.getString(cursor.getColumnIndex(Workout.KEY_info));
                        int nInfoDisplayed = cursor.getInt(cursor.getColumnIndex(Workout.KEY_infoDisplayed));
                        if (nInfoDisplayed == 1) workout.infoDisplayed = true;
                        else workout.infoDisplayed = false;
                        workout.image = cursor.getString(cursor.getColumnIndex(Workout.KEY_image));
                        String temp = cursor.getString(cursor.getColumnIndex(Workout.KEY_exercises));
                        if (temp == null) workout.exercises = "";
                        else workout.exercises = temp;
                        workoutNameList.add(workout);
                    } while (cursor.moveToNext());
                }
                cursor.close();
            } else {
                String selectQuery = "SELECT  " +
                        Workout.KEY_ID + "," +
                        Workout.KEY_category + "," +
                        Workout.KEY_title_dut + "," +
                        Workout.KEY_info_dut + "," +
                        Workout.KEY_infoDisplayed + "," +
                        Workout.KEY_image + "," +
                        Workout.KEY_exercises +
                        " FROM " + Workout.TABLE +
                        " WHERE " + Workout.KEY_category + "=?";

                Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(category_id)});

                if (cursor.moveToFirst()) {
                    do {
                        Workout workout = new Workout();
                        workout.workout_id = cursor.getInt(cursor.getColumnIndex(Workout.KEY_ID));
                        workout.category_id = cursor.getInt(cursor.getColumnIndex(Workout.KEY_category));
                        workout.title = cursor.getString(cursor.getColumnIndex(Workout.KEY_title_dut)).toUpperCase();
                        workout.info = cursor.getString(cursor.getColumnIndex(Workout.KEY_info_dut));
                        int nInfoDisplayed = cursor.getInt(cursor.getColumnIndex(Workout.KEY_infoDisplayed));
                        if (nInfoDisplayed == 1) workout.infoDisplayed = true;
                        else workout.infoDisplayed = false;
                        workout.image = cursor.getString(cursor.getColumnIndex(Workout.KEY_image));
                        String temp = cursor.getString(cursor.getColumnIndex(Workout.KEY_exercises));
                        if (temp == null) workout.exercises = "";
                        else workout.exercises = temp;
                        workoutNameList.add(workout);
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        }
        catch (Exception e){
            Log.d("DBEngine", e.toString());
        }
        finally {
            db.close();
        }

        return workoutNameList;
    }

    public Workout getWorkoutInfo(int workout_id) {
        Workout workout = new Workout();

        int nLanguage = getSelLanguageId();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            if (nLanguage == 1) //english
            {
                String selectQuery = "SELECT  " +
                        Workout.KEY_title + "," +
                        Workout.KEY_info + "," +
                        Workout.KEY_infoDisplayed + "," +
                        Workout.KEY_image + "," +
                        Workout.KEY_exercises +
                        " FROM " + Workout.TABLE +
                        " WHERE " + Workout.KEY_ID + "=?";

                Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(workout_id)});

                if (cursor.moveToFirst()) {
                    workout.workout_id = workout_id;
                    workout.title = cursor.getString(cursor.getColumnIndex(Workout.KEY_title));
                    workout.info = cursor.getString(cursor.getColumnIndex(Workout.KEY_info));
                    int nFlag = cursor.getInt(cursor.getColumnIndex(Workout.KEY_infoDisplayed));
                    if (nFlag == 1)
                        workout.infoDisplayed = true;
                    else
                        workout.infoDisplayed = false;
                    workout.image = cursor.getString(cursor.getColumnIndex(Workout.KEY_image));
                    workout.exercises = cursor.getString(cursor.getColumnIndex(Workout.KEY_exercises));
                }
                cursor.close();
            } else {
                String selectQuery = "SELECT  " +
                        Workout.KEY_title_dut + "," +
                        Workout.KEY_info_dut + "," +
                        Workout.KEY_infoDisplayed + "," +
                        Workout.KEY_image + "," +
                        Workout.KEY_exercises +
                        " FROM " + Workout.TABLE +
                        " WHERE " + Workout.KEY_ID + "=?";

                Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(workout_id)});

                if (cursor.moveToFirst()) {
                    workout.workout_id = workout_id;
                    workout.title = cursor.getString(cursor.getColumnIndex(Workout.KEY_title_dut));
                    workout.info = cursor.getString(cursor.getColumnIndex(Workout.KEY_info_dut));
                    int nFlag = cursor.getInt(cursor.getColumnIndex(Workout.KEY_infoDisplayed));
                    if (nFlag == 1)
                        workout.infoDisplayed = true;
                    else
                        workout.infoDisplayed = false;
                    workout.image = cursor.getString(cursor.getColumnIndex(Workout.KEY_image));
                    workout.exercises = cursor.getString(cursor.getColumnIndex(Workout.KEY_exercises));
                }
                cursor.close();
            }
        }
        catch (Exception e){
            Log.d("DBEngine", e.toString());
        }
        finally {
            db.close();
        }

        return workout;
    }

    public Boolean CheckWorkoutName(String workoutName) {
        Boolean bExist = false;

        int nLanguage = getSelLanguageId();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "";
        try {
            if (nLanguage == 1) //english
            {
                selectQuery = "SELECT " +
                        "count(id) as WoCount " +
                        " FROM " + Workout.TABLE +
                        " WHERE UPPER(" + Workout.KEY_title + ")=?";
            } else {
                selectQuery = "SELECT " +
                        "count(id) as WoCount " +
                        " FROM " + Workout.TABLE +
                        " WHERE UPPER(" + Workout.KEY_title_dut + ")=?";
            }

            Cursor cursor = db.rawQuery(selectQuery, new String[]{workoutName.toUpperCase()});
            if (cursor.moveToFirst()) {
                int nCount = cursor.getInt(cursor.getColumnIndex("WoCount"));
                if (nCount > 0) bExist = true;
            }
            cursor.close();
        }
        catch (Exception e){
            Log.d("DBEngine", e.toString());
        }
        finally {
            db.close();
        }

        return bExist;
    }

    public boolean addWorkouts(Workout workout)
    {
        boolean bResult = false;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(Workout.KEY_category, workout.category_id);
            values.put(Workout.KEY_title, workout.title);
            values.put(Workout.KEY_title_dut, workout.title);
            values.put(Workout.KEY_info, workout.info);
            values.put(Workout.KEY_info_dut, workout.info);
            values.put(Workout.KEY_infoDisplayed, workout.infoDisplayed);
            values.put(Workout.KEY_image, workout.image);
            values.put(Workout.KEY_exercises, workout.exercises);
            db.insert(Workout.TABLE, null, values);

            bResult = true;
        }
        catch (Exception e){
            Log.d("DBEngine", e.toString());
        }
        finally {
            db.close();
        }

        return bResult;
    }

    public boolean updateWorkouts(int workoutId, String title, String exercises)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(Workout.KEY_title, title);
            values.put(Workout.KEY_exercises, exercises);
            db.update(Workout.TABLE, values, "id=?", new String[]{String.valueOf(workoutId)});
        }
        catch (Exception e){
            Log.d("DBEngine", e.toString());
        }
        finally {
            db.close();
        }

        return  true;
    }

    // Exercise
    public ArrayList<Exercise> getExerciseList(int workout_id) {
        ArrayList<Exercise> exerciseList = new ArrayList<Exercise>();

        int nLanguage = getSelLanguageId();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            if (nLanguage == 1) //english
            {
                String selectQuery = "";
                String[] whereParams = null;

                if (workout_id == 0) // Favourite
                {
                    selectQuery = "SELECT  " +
                            Exercise.KEY_ID + "," +
                            Exercise.KEY_title + "," +
                            Exercise.KEY_initialPosition + "," +
                            Exercise.KEY_movement + "," +
                            Exercise.KEY_points + "," +
                            Exercise.KEY_sets + "," +
                            Exercise.KEY_repetions + "," +
                            Exercise.KEY_times + "," +
                            Exercise.KEY_rest + "," +
                            Exercise.KEY_kg + "," +
                            Exercise.KEY_like + "," +
                            Exercise.KEY_images +
                            " FROM " + Exercise.TABLE +
                            " WHERE " + Exercise.KEY_like + "=1";

                    whereParams = null;
                } else {
                    selectQuery = "SELECT  " +
                            Exercise.KEY_ID + "," +
                            Exercise.KEY_title + "," +
                            Exercise.KEY_initialPosition + "," +
                            Exercise.KEY_movement + "," +
                            Exercise.KEY_points + "," +
                            Exercise.KEY_sets + "," +
                            Exercise.KEY_repetions + "," +
                            Exercise.KEY_times + "," +
                            Exercise.KEY_rest + "," +
                            Exercise.KEY_kg + "," +
                            Exercise.KEY_like + "," +
                            Exercise.KEY_images +
                            " FROM " + Exercise.TABLE +
                            " WHERE " + Exercise.KEY_workout + "=?";

                    whereParams = new String[]{String.valueOf(workout_id)};
                }

                Cursor cursor = db.rawQuery(selectQuery, whereParams);

                if (cursor.moveToFirst()) {
                    do {
                        Exercise exercise = new Exercise();
                        exercise.exercise_id = cursor.getInt(cursor.getColumnIndex(Exercise.KEY_ID));
                        exercise.workout_id = workout_id;
                        exercise.title = cursor.getString(cursor.getColumnIndex(Exercise.KEY_title));
                        exercise.initialPosition = cursor.getString(cursor.getColumnIndex(Exercise.KEY_initialPosition));
                        exercise.movement = cursor.getString(cursor.getColumnIndex(Exercise.KEY_movement));
                        exercise.points = cursor.getString(cursor.getColumnIndex(Exercise.KEY_points));
                        exercise.sets = cursor.getString(cursor.getColumnIndex(Exercise.KEY_sets));
                        exercise.repetions = cursor.getString(cursor.getColumnIndex(Exercise.KEY_repetions));
                        exercise.times = cursor.getString(cursor.getColumnIndex(Exercise.KEY_times));
                        exercise.rest = cursor.getString(cursor.getColumnIndex(Exercise.KEY_rest));
                        String temp = cursor.getString(cursor.getColumnIndex(Exercise.KEY_kg));
                        exercise.kg = temp == null ? "" : temp;
                        int nFavourite = cursor.getInt(cursor.getColumnIndex(Exercise.KEY_like));
                        if (nFavourite == 1) exercise.like = true;
                        else exercise.like = false;
                        exercise.images = cursor.getString(cursor.getColumnIndex(Exercise.KEY_images));
                        exerciseList.add(exercise);

                    } while (cursor.moveToNext());
                }
                cursor.close();
            } else {
                String selectQuery = "";
                String[] whereParams = null;

                if (workout_id == 0) // Favourite
                {
                    selectQuery = "SELECT  " +
                            Exercise.KEY_ID + "," +
                            Exercise.KEY_title_dut + "," +
                            Exercise.KEY_initialPosition_dut + "," +
                            Exercise.KEY_movement_dut + "," +
                            Exercise.KEY_points_dut + "," +
                            Exercise.KEY_sets + "," +
                            Exercise.KEY_repetions + "," +
                            Exercise.KEY_times + "," +
                            Exercise.KEY_rest + "," +
                            Exercise.KEY_kg + "," +
                            Exercise.KEY_like + "," +
                            Exercise.KEY_images +
                            " FROM " + Exercise.TABLE +
                            " WHERE " + Exercise.KEY_like + "=true";

                    whereParams = null;
                } else {
                    selectQuery = "SELECT  " +
                            Exercise.KEY_ID + "," +
                            Exercise.KEY_title_dut + "," +
                            Exercise.KEY_initialPosition_dut + "," +
                            Exercise.KEY_movement_dut + "," +
                            Exercise.KEY_points_dut + "," +
                            Exercise.KEY_sets + "," +
                            Exercise.KEY_repetions + "," +
                            Exercise.KEY_times + "," +
                            Exercise.KEY_rest + "," +
                            Exercise.KEY_kg + "," +
                            Exercise.KEY_like + "," +
                            Exercise.KEY_images +
                            " FROM " + Exercise.TABLE +
                            " WHERE " + Exercise.KEY_workout + "=?";

                    whereParams = new String[]{String.valueOf(workout_id)};
                }

                Cursor cursor = db.rawQuery(selectQuery, whereParams);

                if (cursor.moveToFirst()) {
                    do {
                        Exercise exercise = new Exercise();
                        exercise.exercise_id = cursor.getInt(cursor.getColumnIndex(Exercise.KEY_ID));
                        exercise.workout_id = workout_id;
                        exercise.title = cursor.getString(cursor.getColumnIndex(Exercise.KEY_title_dut));
                        exercise.initialPosition = cursor.getString(cursor.getColumnIndex(Exercise.KEY_initialPosition_dut));
                        exercise.movement = cursor.getString(cursor.getColumnIndex(Exercise.KEY_movement_dut));
                        exercise.points = cursor.getString(cursor.getColumnIndex(Exercise.KEY_points_dut));
                        exercise.sets = cursor.getString(cursor.getColumnIndex(Exercise.KEY_sets));
                        exercise.repetions = cursor.getString(cursor.getColumnIndex(Exercise.KEY_repetions));
                        exercise.times = cursor.getString(cursor.getColumnIndex(Exercise.KEY_times));
                        exercise.rest = cursor.getString(cursor.getColumnIndex(Exercise.KEY_rest));
                        String temp = cursor.getString(cursor.getColumnIndex(Exercise.KEY_kg));
                        exercise.kg = temp == null ? "" : temp;
                        int nFavourite = cursor.getInt(cursor.getColumnIndex(Exercise.KEY_like));
                        if (nFavourite == 1) exercise.like = true;
                        else exercise.like = false;
                        exercise.images = cursor.getString(cursor.getColumnIndex(Exercise.KEY_images));
                        exerciseList.add(exercise);

                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        }
        catch (Exception e){
            Log.d("DBEngine", e.toString());
        }
        finally {
            db.close();
        }

        return exerciseList;
    }

    public Exercise getExerciseInfo(int exercise_id) {
        Exercise exercise = new Exercise();

        int nLanguage = getSelLanguageId();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            if (nLanguage == 1) //english
            {

                String selectQuery = "SELECT  " +
                        Exercise.KEY_title + "," +
                        Exercise.KEY_initialPosition + "," +
                        Exercise.KEY_movement + "," +
                        Exercise.KEY_points + "," +
                        Exercise.KEY_sets + "," +
                        Exercise.KEY_repetions + "," +
                        Exercise.KEY_times + "," +
                        Exercise.KEY_rest + "," +
                        Exercise.KEY_kg + "," +
                        Exercise.KEY_like + "," +
                        Exercise.KEY_images +
                        " FROM " + Exercise.TABLE +
                        " WHERE " + Exercise.KEY_ID + "=?";

                Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(exercise_id)});

                if (cursor.moveToFirst()) {
                    exercise.exercise_id = exercise_id;
                    exercise.title = cursor.getString(cursor.getColumnIndex(Exercise.KEY_title));
                    exercise.initialPosition = cursor.getString(cursor.getColumnIndex(Exercise.KEY_initialPosition));
                    exercise.movement = cursor.getString(cursor.getColumnIndex(Exercise.KEY_movement));
                    exercise.points = cursor.getString(cursor.getColumnIndex(Exercise.KEY_points));
                    exercise.sets = cursor.getString(cursor.getColumnIndex(Exercise.KEY_sets));
                    exercise.repetions = cursor.getString(cursor.getColumnIndex(Exercise.KEY_repetions));
                    exercise.times = cursor.getString(cursor.getColumnIndex(Exercise.KEY_times));
                    exercise.rest = cursor.getString(cursor.getColumnIndex(Exercise.KEY_rest));
                    String temp = cursor.getString(cursor.getColumnIndex(Exercise.KEY_kg));
                    exercise.kg = temp == null ? "" : temp;
                    int nFavourite = cursor.getInt(cursor.getColumnIndex(Exercise.KEY_like));
                    if (nFavourite == 1) exercise.like = true;
                    else exercise.like = false;
                    exercise.images = cursor.getString(cursor.getColumnIndex(Exercise.KEY_images));
                }
                cursor.close();
            } else {
                String selectQuery = "SELECT  " +
                        Exercise.KEY_title_dut + "," +
                        Exercise.KEY_initialPosition_dut + "," +
                        Exercise.KEY_movement_dut + "," +
                        Exercise.KEY_points_dut + "," +
                        Exercise.KEY_sets + "," +
                        Exercise.KEY_repetions + "," +
                        Exercise.KEY_times + "," +
                        Exercise.KEY_rest + "," +
                        Exercise.KEY_kg + "," +
                        Exercise.KEY_like + "," +
                        Exercise.KEY_images +
                        " FROM " + Exercise.TABLE +
                        " WHERE " + Exercise.KEY_ID + "=?";

                Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(exercise_id)});

                if (cursor.moveToFirst()) {
                    exercise.exercise_id = exercise_id;
                    exercise.title = cursor.getString(cursor.getColumnIndex(Exercise.KEY_title_dut));
                    exercise.initialPosition = cursor.getString(cursor.getColumnIndex(Exercise.KEY_initialPosition_dut));
                    exercise.movement = cursor.getString(cursor.getColumnIndex(Exercise.KEY_movement_dut));
                    exercise.points = cursor.getString(cursor.getColumnIndex(Exercise.KEY_points_dut));
                    exercise.sets = cursor.getString(cursor.getColumnIndex(Exercise.KEY_sets));
                    exercise.repetions = cursor.getString(cursor.getColumnIndex(Exercise.KEY_repetions));
                    exercise.times = cursor.getString(cursor.getColumnIndex(Exercise.KEY_times));
                    exercise.rest = cursor.getString(cursor.getColumnIndex(Exercise.KEY_rest));
                    String temp = cursor.getString(cursor.getColumnIndex(Exercise.KEY_kg));
                    exercise.kg = temp == null ? "" : temp;
                    int nFavourite = cursor.getInt(cursor.getColumnIndex(Exercise.KEY_like));
                    if (nFavourite == 1) exercise.like = true;
                    else exercise.like = false;
                    exercise.images = cursor.getString(cursor.getColumnIndex(Exercise.KEY_images));
                }
                cursor.close();
            }
        }
        catch (Exception e){
            Log.d("DBEngine", e.toString());
        }
        finally {
            db.close();
        }

        return exercise;
    }

    public int getExerciseCount(int workout_id) {
        int nExCount = 0;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            String selectQuery = "";
            String[] whereParams = null;
            if (workout_id == 0) {
                selectQuery = "SELECT " +
                        "count(id) as ExCount " +
                        " FROM " + Exercise.TABLE +
                        " WHERE " + Exercise.KEY_like + "=1";

                whereParams = null;
            } else if (workout_id < 23) {
                selectQuery = "SELECT " +
                        "count(id) as ExCount " +
                        " FROM " + Exercise.TABLE +
                        " WHERE " + Exercise.KEY_workout + "=?";

                whereParams = new String[]{String.valueOf(workout_id)};
            } else {
                selectQuery = "SELECT " +
                        Workout.KEY_exercises +
                        " FROM " + Workout.TABLE +
                        " WHERE " + Workout.KEY_ID + "=?";

                whereParams = new String[]{String.valueOf(workout_id)};
            }

            Cursor cursor = db.rawQuery(selectQuery, whereParams);

            if (cursor.moveToFirst()) {
                if (workout_id < 23) {
                    nExCount = cursor.getInt(cursor.getColumnIndex("ExCount"));
                } else {
                    String exercises = cursor.getString(cursor.getColumnIndex(Workout.KEY_exercises));
                    String[] exerciseArray = exercises.split(",");
                    nExCount = exerciseArray.length;
                }
            }

            cursor.close();
        }
        catch (Exception e){
            Log.d("DBEngine", e.toString());
        }
        finally {
            db.close();
        }

        return nExCount;
    }

    // Images
    public Image getImageInfo(String imageId)
    {
        Image imageInfo = new Image();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            String selectQuery = "SELECT  " +
                    Image.KEY_name + "," +
                    Image.KEY_isInAssets + "," +
                    Image.KEY_path +
                    " FROM " + Image.TABLE +
                    " WHERE " + Image.KEY_ID + "=?";

            Cursor cursor = db.rawQuery(selectQuery, new String[]{imageId});

            if (cursor.moveToFirst()) {
                imageInfo.image_id = Integer.parseInt(imageId);
                imageInfo.name = cursor.getString(cursor.getColumnIndex(Image.KEY_name));
                int nInAssets = cursor.getInt(cursor.getColumnIndex(Image.KEY_isInAssets));
                if (nInAssets == 1) imageInfo.isInAssets = true;
                else imageInfo.isInAssets = false;

                int nLoading = cursor.getInt(cursor.getColumnIndex(Image.KEY_path));
                if (nLoading == 1) imageInfo.path = true;
                else imageInfo.path = false;
            }

            cursor.close();
        }
        catch (Exception e){
            Log.d("DBEngine", e.toString());
        }
        finally {
            db.close();
        }

        return imageInfo;
    }

    public boolean updateImageInAssets(String imageName)
    {
        boolean bResult = false;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(Image.KEY_isInAssets, true);
            db.update(Image.TABLE, values, Image.KEY_name + "=?", new String[]{imageName});

            bResult = true;
        }
        catch (Exception e){
            Log.d("DBEngine", e.toString());
        }
        finally {
            db.close();
        }

        return  bResult;
    }

    public boolean updateImagePath(String imageId, Boolean bLoading)
    {
        boolean bResult = false;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(Image.KEY_path, bLoading);
            db.update(Image.TABLE, values, Image.KEY_ID + "=?", new String[]{imageId});

            bResult = true;
        }
        catch (Exception e){
            Log.d("DBEngine", e.toString());
        }
        finally {
            db.close();
        }

        return bResult;
    }

    // Favourite
    public boolean isFavourite(int exercise_id)
    {
        boolean bFavourite = false;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            String selectQuery = "SELECT  " +
                    Exercise.KEY_like +
                    " FROM " + Exercise.TABLE +
                    " WHERE " + Exercise.KEY_ID + "=?";

            Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(exercise_id)});
            if (cursor.moveToFirst()) {
                int nLike = cursor.getInt(cursor.getColumnIndex(Exercise.KEY_like));
                if (nLike == 1) bFavourite = true;
            }
            cursor.close();
        }
        catch (Exception e){
            Log.d("DBEngine", e.toString());
        }
        finally {
            db.close();
        }

        return bFavourite;
    }

    public boolean updateFavourite(int exerciseId, Boolean bFavourite)
    {
        boolean bResult = false;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(Exercise.KEY_like, bFavourite);
            db.update(Exercise.TABLE, values, "id=?", new String[]{String.valueOf(exerciseId)});

            bResult = true;
        }
        catch (Exception e){
            Log.d("DBEngine", e.toString());
        }
        finally {
            db.close();
        }

        return bResult;
    }

    // Langugage
    public int getSelLanguageId()
    {
        int nLangId = 1;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            String selectQuery = "SELECT id from Language where selected = 1";

            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                nLangId = cursor.getInt(cursor.getColumnIndex(Language.KEY_ID));
            }
            cursor.close();
        }
        catch (Exception e){
            Log.d("DBEngine", e.toString());
        }
        finally {
            db.close();
        }

        return nLangId;
    }

    public boolean setLanguage(int langId)
    {
        boolean bResult = false;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            ContentValues values1 = new ContentValues();
            values1.put(Language.KEY_selected, false);
            db.update(Language.TABLE, values1, null, null);

            ContentValues values2 = new ContentValues();
            values2.put(Language.KEY_selected, true);
            db.update(Language.TABLE, values2, "id=?", new String[]{String.valueOf(langId)});

            bResult = true;
        }
        catch (Exception e){
            Log.d("DBEngine", e.toString());
        }
        finally {
            db.close();
        }

        return bResult;
    }
}
