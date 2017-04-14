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
import android.widget.TextView;
import android.widget.Toast;

import com.gaurav.cdsrecyclerview.CdsItemTouchCallback;
import com.gaurav.cdsrecyclerview.CdsRecyclerView;
import com.sodabodyfit.moms.Adapter.EditMyWorkoutAdapter;
import com.sodabodyfit.moms.Common.DividerItemDecoration;
import com.sodabodyfit.moms.Models.Exercise;

import java.util.ArrayList;

public class EditMyWorkoutActivity extends AppCompatActivity {

    private ArrayList<Exercise> lstExercise = new ArrayList<Exercise>();
    private CdsItemTouchCallback.ItemDragCompleteListener mItemDragCompleteListener;
    private int workoutId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_workout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String title = intent.getStringExtra("workout_title");
        workoutId = intent.getIntExtra("workout_id", -1);

        TextView tvDeleteAll = (TextView)findViewById(R.id.txt_delete_all);
        tvDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAllExercise();
            }
        });
        initListeners();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        CdsRecyclerView recycler = (CdsRecyclerView) findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(layoutManager);
        recycler.addItemDecoration(new DividerItemDecoration(this));

        EditMyWorkoutAdapter adapter = new EditMyWorkoutAdapter(EditMyWorkoutActivity.this, lstExercise);
        recycler.setAdapter(adapter);

        recycler.enableItemDrag();
        recycler.setItemDragCompleteListener(mItemDragCompleteListener);
    }

    private void initListeners() {
//        mItemClickListener = new CdsRecyclerView.ItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                Toast.makeText(MainActivity.this, "Item Clicked:" +
//                        mRecyclerViewAdapter.getItem(position), Toast.LENGTH_SHORT).show();
//            }
//        };

        mItemDragCompleteListener = new CdsItemTouchCallback.ItemDragCompleteListener() {
            @Override
            public void onItemDragComplete(int fromPosition, int toPosition) {
                Toast.makeText(EditMyWorkoutActivity.this, "Item dragged from " + fromPosition +
                        " to " + toPosition, Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void deleteAllExercise() {

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

    }
}
