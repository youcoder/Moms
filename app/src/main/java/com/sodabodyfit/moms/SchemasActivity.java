package com.sodabodyfit.moms;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sodabodyfit.moms.Adapter.SchemasAdapter;
import com.sodabodyfit.moms.Common.DividerItemDecoration;
import com.sodabodyfit.moms.Common.ImageLoader;
import com.sodabodyfit.moms.Models.Exercise;
import com.sodabodyfit.moms.Models.Workout;
import com.sodabodyfit.moms.Provider.DBEngine;

import java.util.ArrayList;

public class SchemasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schemas);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/Aleo-Bold.otf");
        
        Intent intent = getIntent();
        int workoutId = intent.getIntExtra("workout_id", -1);
        ArrayList<Exercise> lstExercise = intent.getParcelableArrayListExtra("exercise");

        DBEngine dbEngine = new DBEngine(this);
        Workout workout = dbEngine.getWorkoutInfo(workoutId);

        TextView tvTitle = (TextView)findViewById(R.id.txt_title);
        tvTitle.setText(workout.title);
        tvTitle.setTypeface(typeFace);

        ImageView imageView = (ImageView) findViewById(R.id.img_category);
        ImageLoader.LoadImage(this, imageView, workout.image);

        LinearLayout llSchemasInfo = (LinearLayout) findViewById(R.id.ll_schemas_info);
        TextView tvSchemasInfo = (TextView) findViewById(R.id.txt_schemas_info);
        if(workout.infoDisplayed) {
            tvSchemasInfo.setText(workout.info);
            llSchemasInfo.setVisibility(View.VISIBLE);
        }

        TextView tv_Schemas = (TextView)findViewById(R.id.txt_schemas_label);
        tv_Schemas.setTypeface(typeFace);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(layoutManager);
        recycler.addItemDecoration(new DividerItemDecoration(this));
        recycler.setFocusable(false);
        recycler.setNestedScrollingEnabled(false);

        SchemasAdapter adapter = new SchemasAdapter(SchemasActivity.this, lstExercise, typeFace);
        recycler.setAdapter(adapter);

        final NestedScrollView scroll = (NestedScrollView)findViewById(R.id.scroll);
        LinearLayout llUpArrow = (LinearLayout) findViewById(R.id.ll_top);
        llUpArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scroll.fullScroll(ScrollView.FOCUS_UP);
            }
        });
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
            SchemasActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
