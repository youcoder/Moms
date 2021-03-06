package com.sodabodyfit.moms;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sodabodyfit.moms.Common.ImageLoader;
import com.sodabodyfit.moms.Models.Exercise;
import com.sodabodyfit.moms.Models.Workout;
import com.sodabodyfit.moms.Provider.DBEngine;

import java.util.ArrayList;

public class ExerciseListActivity extends AppCompatActivity {

    private static int CHANGE_FAVOURITE = 101;
    private ArrayList<Exercise> lstExercise = new ArrayList<Exercise>();
    private int workoutId;
    private DBEngine dbEngine;
    private ExerciseListAdapter adapter;
    private TextView tvTitle;
    private Menu menuOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        workoutId = intent.getIntExtra("workout_id", -1);
        String title = intent.getStringExtra("workout_title");

        tvTitle = (TextView)findViewById(R.id.txt_title);
        tvTitle.setText(title);

        dbEngine = new DBEngine(this);
        if(workoutId < 23) {    // common workouts
            lstExercise = dbEngine.getExerciseList(workoutId);
        }
        else{   // if MyWorkouts
            Workout myWorkout = dbEngine.getWorkoutInfo(workoutId);

            if(!myWorkout.exercises.isEmpty()) {
                String[] exerciseIds = myWorkout.exercises.split(",");

                for (int i = 0; i < exerciseIds.length; i++) {
                    lstExercise.add(dbEngine.getExerciseInfo(Integer.parseInt(exerciseIds[i])));
                }
            }
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(layoutManager);

        adapter = new ExerciseListAdapter(ExerciseListActivity.this);
        recycler.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuOption = menu;

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getMenuInflater().inflate(R.menu.menu_exercise_list, menu);

        DBEngine dbEngine = new DBEngine(this);
        Workout myWorkout = dbEngine.getWorkoutInfo(workoutId);

        if (workoutId < 23 || (myWorkout.exercises != null && myWorkout.exercises.isEmpty())) {
            MenuItem item = menu.findItem(R.id.action_edit);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_info) {
            Intent intent = new Intent(ExerciseListActivity.this, SchemasActivity.class);
            intent.putExtra("workout_id", workoutId);
            intent.putExtra("exercise", lstExercise);
            startActivity(intent);
        } else if(id == R.id.action_edit) {
            Intent intent = new Intent(ExerciseListActivity.this, EditMyWorkoutActivity.class);
            intent.putExtra("workout_id", workoutId);
            startActivityForResult(intent, CHANGE_FAVOURITE);
        } else if (id == android.R.id.home) {
            ExerciseListActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.ViewHolder> {

        Context context;

        public ExerciseListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(context).inflate(R.layout.list_exercise, parent, false);
            return new ViewHolder(v);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            final Exercise item = lstExercise.get(position);
            holder.tvSubject.setText(item.title);

            if(item.like) holder.ivFavourite.setVisibility(View.VISIBLE);
            else holder.ivFavourite.setVisibility(View.GONE);

            String[] imageIds = item.images.split(",");
            if(imageIds.length > 0)
                ImageLoader.LoadImage(context, holder.ivExercise, imageIds[0]);

            holder.ivExercise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showExercise(item);
                }
            });
        }

        @Override
        public int getItemCount() {
            return lstExercise.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView ivExercise, ivFavourite;
            TextView tvSubject;

            public ViewHolder(View v) {
                super(v);
                ivExercise = (ImageView) v.findViewById(R.id.img_exercise);
                ivFavourite = (ImageView) v.findViewById(R.id.img_favourite);
                tvSubject = (TextView) v.findViewById(R.id.txt_subject);
            }
        }
    }

    private void showExercise(Exercise item) {
        Intent intent = new Intent(ExerciseListActivity.this, ExerciseActivity.class);
        intent.putExtra("exercise", item);
        startActivityForResult(intent, CHANGE_FAVOURITE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHANGE_FAVOURITE) {
            //if (resultCode == RESULT_OK) {
                if(workoutId < 23) {
                    lstExercise = dbEngine.getExerciseList(workoutId);
                }
                else{
                    lstExercise.clear();

                    Workout myWorkouts = dbEngine.getWorkoutInfo(workoutId);
                    if(!myWorkouts.exercises.isEmpty()) {
                        String[] exerciseIds = myWorkouts.exercises.split(",");

                        for (int i = 0; i < exerciseIds.length; i++) {
                            lstExercise.add(dbEngine.getExerciseInfo(Integer.parseInt(exerciseIds[i])));
                        }
                    }
                }

                dbEngine = new DBEngine(this);
                Workout workout = dbEngine.getWorkoutInfo(workoutId);
                tvTitle.setText(workout.title.toUpperCase());

                if (workoutId < 23 || (workout.exercises != null && workout.exercises.isEmpty())) {
                    MenuItem item = menuOption.findItem(R.id.action_edit);
                    item.setVisible(false);
                }

                adapter.notifyDataSetChanged();
            //}
        }
    }
}
