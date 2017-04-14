package com.sodabodyfit.moms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sodabodyfit.moms.Common.ImageLoader;
import com.sodabodyfit.moms.Models.Exercise;
import com.sodabodyfit.moms.Models.Workout;
import com.sodabodyfit.moms.Provider.DBEngine;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddWorkoutActivity extends AppCompatActivity {

    private Exercise exercise;
    private EditText etWorkoutSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        exercise = intent.getParcelableExtra("exercise");

        etWorkoutSubject = (EditText)findViewById(R.id.edt_workout_title);

        TextView tvExerciseTitle = (TextView)findViewById(R.id.txt_subject);
        tvExerciseTitle.setText(exercise.title);

        TextView tvDate = (TextView)findViewById(R.id.tv_createdate);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String todayDate = sdf.format(new Date());
        tvDate.setText(todayDate);

        ImageView ivExercise = (ImageView)findViewById(R.id.img_exercise);
        String[] imageIds = exercise.images.split(",");
        if(imageIds.length > 0)
            ImageLoader.LoadImage(AddWorkoutActivity.this, ivExercise, imageIds[0]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getMenuInflater().inflate(R.menu.menu_edit_my_workout, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_ok) {
            CreateMyworkout();
            AddWorkoutActivity.this.finish();
        }else if (id == android.R.id.home) {
            AddWorkoutActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void CreateMyworkout() {

        try {
            String newWorkoutName = etWorkoutSubject.getText().toString();

            DBEngine dbEngine = new DBEngine(this);
            boolean bExist = dbEngine.CheckWorkoutName(newWorkoutName);

            if (!bExist) {
                Workout newMyWorkout = new Workout();
                newMyWorkout.category_id = 1;
                newMyWorkout.title = newWorkoutName;
                newMyWorkout.info = "";
                newMyWorkout.infoDisplayed = false;
                newMyWorkout.exercises = String.valueOf(exercise.exercise_id);
                String[] imageIds = exercise.images.split(",");
                if (imageIds.length > 0) newMyWorkout.image = imageIds[0];

                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                String todayDate = sdf.format(new Date());
                newMyWorkout.creationDate = todayDate;

                dbEngine.addWorkouts(newMyWorkout);

                Toast.makeText(AddWorkoutActivity.this, "created successfully!", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            Log.d("AddWorkout", e.toString());
        }
    }
}
