package com.sodabodyfit.moms.Adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.gaurav.cdsrecyclerview.CdsRecyclerViewAdapter;
import com.sodabodyfit.moms.Common.ImageLoader;
import com.sodabodyfit.moms.Models.Exercise;
import com.sodabodyfit.moms.Models.Workout;
import com.sodabodyfit.moms.Provider.DBEngine;
import com.sodabodyfit.moms.R;

import java.util.ArrayList;

/**
 * Created by owner on 3/7/2017.
 */

public class EditMyWorkoutAdapter extends CdsRecyclerViewAdapter<Exercise, EditMyWorkoutAdapter.ViewHolder> {

    Context context;
    int workoutId;
    ArrayList<Exercise> lstExercise = new ArrayList<Exercise>();

    public EditMyWorkoutAdapter(Context context, int workoutId, ArrayList<Exercise> listContent) {
        super(context, listContent);

        this.context = context;
        this.workoutId = workoutId;
        this.lstExercise = listContent;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.list_edit_my_workout, parent, false);
        return new ViewHolder(v);
    }

    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Exercise item = lstExercise.get(position);
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
    }

    private void deleteExcise(int exercise_id) {
        DBEngine dbEngine = new DBEngine(context);
        Workout myWorkout = dbEngine.getWorkoutInfo(workoutId);
        String[] exerciseIds = myWorkout.exercises.split(",");

        String newImages = "";
        for(int i = 0; i < exerciseIds.length; i++){
            if(Integer.parseInt(exerciseIds[i]) == exercise_id) continue;

            if(newImages == "") newImages = exerciseIds[i];
            else newImages += "," + exerciseIds[i];
        }

        dbEngine.updateWorkouts(workoutId, "", newImages);
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
