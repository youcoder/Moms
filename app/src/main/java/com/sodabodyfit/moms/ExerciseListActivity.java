package com.sodabodyfit.moms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.sodabodyfit.moms.Adapter.ExerciseListAdapter;
import com.sodabodyfit.moms.Models.Exercise;
import com.sodabodyfit.moms.Provider.DBEngine;

import java.util.ArrayList;

public class ExerciseListActivity extends AppCompatActivity {

    private ArrayList<Exercise> lstExercise = new ArrayList<Exercise>();
    private int workoutId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String title = intent.getStringExtra("workout_title");
        workoutId = intent.getIntExtra("workout_id", -1);

        TextView tvTitle = (TextView)findViewById(R.id.txt_title);
        tvTitle.setText(title);

        DBEngine dbEngine = new DBEngine(this);
        lstExercise = dbEngine.getExerciseList(workoutId);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(layoutManager);

        ExerciseListAdapter adapter = new ExerciseListAdapter(ExerciseListActivity.this, lstExercise);
        recycler.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getMenuInflater().inflate(R.menu.menu_exercise_list, menu);

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
        }else if (id == android.R.id.home) {
            ExerciseListActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
