package com.sodabodyfit.moms;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gaurav.cdsrecyclerview.CdsItemTouchCallback;
import com.gaurav.cdsrecyclerview.CdsRecyclerView;
import com.gaurav.cdsrecyclerview.CdsRecyclerViewAdapter;
import com.sodabodyfit.moms.Common.DividerItemDecoration;
import com.sodabodyfit.moms.Common.ImageLoader;
import com.sodabodyfit.moms.Models.Exercise;
import com.sodabodyfit.moms.Models.Workout;
import com.sodabodyfit.moms.Provider.DBEngine;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class EditMyWorkoutActivity extends AppCompatActivity {

    private ArrayList<Exercise> lstExercise = new ArrayList<Exercise>();
    private ArrayList<Exercise> lstExercise2 = new ArrayList<Exercise>();
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
                lstExercise2.add(exercise);
            }
        }

        adapter = new EditMyWorkoutAdapter(EditMyWorkoutActivity.this);
        recycler.setAdapter(adapter);

        recycler.enableItemDrag();
        recycler.setItemDragCompleteListener(mItemDragCompleteListener);
    }

    private void initListeners() {

        mItemDragCompleteListener = new CdsItemTouchCallback.ItemDragCompleteListener() {
            @Override
            public void onItemDragComplete(int fromPosition, int toPosition) {

                Collections.swap(lstExercise, fromPosition, toPosition);
                Collections.swap(lstExercise2, fromPosition, toPosition);

                Toast.makeText(EditMyWorkoutActivity.this, "Item dragged from " + fromPosition +
                        " to " + toPosition, Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void deleteAllExercise() {
        lstExercise.clear();
        lstExercise2.clear();
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
            for (int i = 0; i < lstExercise2.size(); i++)
                if(i == 0) newExercises = String.valueOf(lstExercise2.get(0).exercise_id);
                else newExercises += "," + String.valueOf(lstExercise2.get(i).exercise_id);

            dbEngine.updateWorkoutName(workoutId, title);
            dbEngine.updateWorkoutExerciseList(workoutId, newExercises);

            EditMyWorkoutActivity.this.finish();
        } else {
            Toast.makeText(EditMyWorkoutActivity.this, "The title is empty.", Toast.LENGTH_SHORT).show();
        }
    }

    public class EditMyWorkoutAdapter extends CdsRecyclerViewAdapter<Exercise, EditMyWorkoutAdapter.ViewHolder> {

        Context context;

        public EditMyWorkoutAdapter(Context context) {
            super(context, lstExercise);

            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(context).inflate(R.layout.list_edit_my_workout, parent, false);
            return new ViewHolder(v);
        }

        //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final Exercise item = lstExercise2.get(position);
            String[] imageIds = item.images.split(",");
            if(imageIds.length > 0) ImageLoader.LoadImage(context, ((ViewHolder) holder).ivExercise, imageIds[0]);
            ((ViewHolder) holder).tvSubject.setText(item.title);
            ((ViewHolder) holder).ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmDialog(item.exercise_id);
                }
            });
        }

        private void confirmDialog(final int exercise_id) {
            MaterialDialog dialog = new MaterialDialog.Builder(context)
                    .title("confirm")
                    .content("Are you sure you want to delete it?")
                    .positiveText("OK")
                    .negativeText("CANCEL")
                    .cancelable(false)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            deleteExcise(exercise_id);
                        }
                    }).build();
            dialog.show();
        }

        private void deleteExcise(int exercise_id) {

            this.notifyDataSetChanged();
            for (int i = 0; i < lstExercise.size(); i++) {
                if(lstExercise.get(i).exercise_id == exercise_id) {
                    lstExercise.remove(i);
                    this.notifyDataSetChanged();
                    break;
                }
            }

            for (int i = 0; i < lstExercise2.size(); i++) {
                if(lstExercise2.get(i).exercise_id == exercise_id) {
                    lstExercise2.remove(i);
                    break;
                }
            }
            Toast.makeText(context, "The selected exercise deleted!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public int getItemCount() {
            return lstExercise.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvSubject;
            ImageView ivDelete, ivExercise;

            public ViewHolder(View v) {
                super(v);
                tvSubject = (TextView) v.findViewById(R.id.txt_subject);
                ivDelete = (ImageView) v.findViewById(R.id.img_delete);
                ivExercise = (ImageView) v.findViewById(R.id.img_exercise);
            }
        }
    }
}
