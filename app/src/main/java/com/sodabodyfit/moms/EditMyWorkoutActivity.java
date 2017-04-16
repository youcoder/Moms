package com.sodabodyfit.moms;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gaurav.cdsrecyclerview.CdsItemTouchCallback;
import com.gaurav.cdsrecyclerview.CdsRecyclerView;
import com.sodabodyfit.moms.Adapter.EditMyWorkoutAdapter;
import com.sodabodyfit.moms.Common.DividerItemDecoration;
import com.sodabodyfit.moms.Models.Exercise;
import com.sodabodyfit.moms.Models.Workout;
import com.sodabodyfit.moms.Provider.DBEngine;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class EditMyWorkoutActivity extends AppCompatActivity {

    private ArrayList<Exercise> lstExercise = new ArrayList<Exercise>();
    private ArrayList<Exercise> lstTemp = new ArrayList<Exercise>();
    private CdsItemTouchCallback.ItemDragCompleteListener mItemDragCompleteListener;
    private int workoutId;
    private EditMyWorkoutAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_workout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        workoutId = intent.getIntExtra("workout_id", -1);

        DBEngine dbEngine = new DBEngine(this);
        Workout myWorkout = dbEngine.getWorkoutInfo(workoutId);

        TextView tvDeleteAll = (TextView)findViewById(R.id.txt_delete_all);
        tvDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAllExercise();
            }
        });
        EditText edtWorkoutTitle = (EditText)findViewById(R.id.edt_workout_title);
        edtWorkoutTitle.setText(myWorkout.title);

        TextView tvDate = (TextView)findViewById(R.id.tv_createdate);
        tvDate.setText(myWorkout.creationDate);

        initListeners();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        CdsRecyclerView recycler = (CdsRecyclerView) findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(layoutManager);

        if(!myWorkout.exercises.isEmpty()) {
            String[] exerciseIds = myWorkout.exercises.split(",");

            for (int i = 0; i < exerciseIds.length; i++) {
                Exercise exercise = dbEngine.getExerciseInfo(Integer.parseInt(exerciseIds[i]));
                lstExercise.add(exercise);
                lstTemp.add(exercise);
            }
        }

        adapter = new EditMyWorkoutAdapter(EditMyWorkoutActivity.this, workoutId, lstExercise);
        recycler.setAdapter(adapter);

        recycler.enableItemDrag();
        recycler.setItemDragCompleteListener(mItemDragCompleteListener);
    }

    private void initListeners() {

        mItemDragCompleteListener = new CdsItemTouchCallback.ItemDragCompleteListener() {
            @Override
            public void onItemDragComplete(int fromPosition, int toPosition) {

                Collections.swap(lstTemp, fromPosition, toPosition);

                Toast.makeText(EditMyWorkoutActivity.this, "Item dragged from " + fromPosition +
                        " to " + toPosition, Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void deleteAllExercise() {

        lstExercise.clear();
        lstTemp.clear();
        adapter.notifyDataSetChanged();

        Toast.makeText(EditMyWorkoutActivity.this, "All exercise deleted!", Toast.LENGTH_SHORT).show();
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
            SaveMyworkout();
            EditMyWorkoutActivity.this.finish();
        }else if (id == android.R.id.home) {
            EditMyWorkoutActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void SaveMyworkout() {

        EditText edtWorkoutTitle = (EditText)findViewById(R.id.edt_workout_title);
        String title = edtWorkoutTitle.getText().toString();

        DBEngine dbEngine = new DBEngine(this);
        if(!title.isEmpty()) {
            String newExercises = "";
            for (int i = 0; i < lstTemp.size(); i++)
                if(i == 0) newExercises = String.valueOf(lstTemp.get(0).exercise_id);
                else newExercises += "," + String.valueOf(lstTemp.get(i).exercise_id);

            dbEngine.updateWorkoutName(workoutId, title);
            dbEngine.updateWorkoutExerciseList(workoutId, newExercises);

            EditMyWorkoutActivity.this.finish();
        } else {
            Toast.makeText(EditMyWorkoutActivity.this, "The title is empty.", Toast.LENGTH_SHORT).show();
        }
    }
}
