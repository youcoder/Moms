package com.sodabodyfit.moms.Adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sodabodyfit.moms.AddWorkoutActivity;
import com.sodabodyfit.moms.Common.ImageLoader;
import com.sodabodyfit.moms.Models.Workout;
import com.sodabodyfit.moms.Provider.DBEngine;
import com.sodabodyfit.moms.R;
import com.sodabodyfit.moms.ExerciseListActivity;

import java.util.ArrayList;

/**
 * Created by owner on 3/7/2017.
 */

public class MyWorkoutAdapter extends RecyclerView.Adapter<MyWorkoutAdapter.ViewHolder> {

    Context context;
    ArrayList<Workout> listContent = new ArrayList<Workout>();
    int exercise_id;

    public MyWorkoutAdapter(Context context, ArrayList<Workout> listContent, int exercise_id) {
        this.context = context;
        this.listContent = listContent;
        this.exercise_id = exercise_id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.list_my_workout, parent, false);
        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Workout item = listContent.get(position);
        holder.tvSubject.setText(item.title);
        ImageLoader.LoadImage(context, holder.ivExercise, item.image);

        holder.llMyWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddExerciseToWorkout(item.workout_id);
            }
        });

    }

    private Boolean AddExerciseToWorkout(int workout_id){
        DBEngine dbEngine = new DBEngine(context);
        Workout myWorkout = dbEngine.getWorkoutInfo(workout_id);
        String newImages = myWorkout.exercises;

        if(newImages.contains(String.valueOf(exercise_id))) {
            Toast.makeText(context, "The selected workout already contains the exercise", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(newImages.isEmpty()) newImages = String.valueOf(exercise_id);
        else newImages += "," + String.valueOf(exercise_id);


        boolean bResult = dbEngine.updateWorkouts(myWorkout.workout_id, "", newImages);

        if(bResult) Toast.makeText(context, "The exercise has added to the selected workout", Toast.LENGTH_SHORT).show();

        return bResult;
    }

    @Override
    public int getItemCount() {
        return listContent.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivExercise ;
        TextView tvSubject;
        LinearLayout llMyWorkout;

        public ViewHolder(View v) {
            super(v);
            ivExercise = (ImageView) v.findViewById(R.id.img_exercise);
            tvSubject = (TextView) v.findViewById(R.id.txt_subject);
            llMyWorkout = (LinearLayout) v.findViewById(R.id.ll_workout);
        }
    }
}
