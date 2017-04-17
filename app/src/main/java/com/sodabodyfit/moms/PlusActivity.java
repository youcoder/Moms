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
import android.view.View;
import android.widget.LinearLayout;

import com.sodabodyfit.moms.Adapter.MyWorkoutAdapter;
import com.sodabodyfit.moms.Common.DividerItemDecoration;
import com.sodabodyfit.moms.Models.Exercise;
import com.sodabodyfit.moms.Models.Workout;
import com.sodabodyfit.moms.Provider.DBEngine;

import java.util.ArrayList;

public class PlusActivity extends AppCompatActivity {

    private Exercise exercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        exercise = intent.getParcelableExtra("exercise");

        LinearLayout llPlus = (LinearLayout)findViewById(R.id.ll_add_workout);
        llPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlusActivity.this, AddWorkoutActivity.class);
                intent.putExtra("exercise", exercise);
                startActivity(intent);
                PlusActivity.this.finish();
            }
        });


        DBEngine dbEngine = new DBEngine(this);
        ArrayList<Workout> lstWorkout = dbEngine.getWorkoutList(1);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(layoutManager);
        recycler.addItemDecoration(new DividerItemDecoration(this));

        MyWorkoutAdapter adapter = new MyWorkoutAdapter(PlusActivity.this, lstWorkout, exercise.exercise_id);
        recycler.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            PlusActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
