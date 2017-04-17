package com.sodabodyfit.moms;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.sodabodyfit.moms.Adapter.WorkoutAdapter;
import com.sodabodyfit.moms.Common.DividerItemDecoration;
import com.sodabodyfit.moms.Models.Workout;
import com.sodabodyfit.moms.Provider.DBEngine;

import java.util.ArrayList;

public class ExpandWorkoutActivity extends AppCompatActivity {

    private ArrayList<Workout> lstWorkout = new ArrayList<Workout>();
    private int categoryId;
    private WorkoutAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand_workout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        categoryId = bundle.getInt("category_id");
        int resId = bundle.getInt("image");
        lstWorkout = bundle.getParcelableArrayList("workout");

        TextView tvTitle = (TextView)findViewById(R.id.txt_title);
        tvTitle.setText(getResources().getStringArray(R.array.title_category)[categoryId]);
        ImageView ivCategory = (ImageView) findViewById(R.id.img_category);
        ivCategory.setImageResource(resId);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(layoutManager);
        recycler.addItemDecoration(new DividerItemDecoration(this));

        adapter = new WorkoutAdapter(ExpandWorkoutActivity.this, lstWorkout);
        recycler.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        ActionBar actionBar = getSupportActionBar();
        getMenuInflater().inflate(R.menu.menu_expand, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_close) {
            ExpandWorkoutActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        DBEngine dbEngine = new DBEngine(this);
        lstWorkout = dbEngine.getWorkoutList(categoryId);
        adapter.notifyDataSetChanged();
    }
}
