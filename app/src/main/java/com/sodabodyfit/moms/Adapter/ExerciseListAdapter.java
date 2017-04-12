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

import com.sodabodyfit.moms.ExerciseActivity;
import com.sodabodyfit.moms.ExerciseListActivity;
import com.sodabodyfit.moms.Models.Exercise;
import com.sodabodyfit.moms.Models.Workout;
import com.sodabodyfit.moms.Provider.DBEngine;
import com.sodabodyfit.moms.R;

import java.util.ArrayList;

/**
 * Created by owner on 3/7/2017.
 */

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.ViewHolder> {

    Context context;
    private ArrayList<Exercise> lstExercise = new ArrayList<Exercise>();

    public ExerciseListAdapter(Context context, ArrayList<Exercise> listContent) {
        this.context = context;
        this.lstExercise = listContent;
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
        holder.ivExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ExerciseActivity.class);
//                intent.putExtra("workout_title", item.title);
//                intent.putExtra("workout_id", item.workout_id);
                context.startActivity(intent);
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
