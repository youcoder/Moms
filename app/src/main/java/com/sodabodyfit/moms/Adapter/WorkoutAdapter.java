package com.sodabodyfit.moms.Adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sodabodyfit.moms.Models.Workout;
import com.sodabodyfit.moms.Provider.DBEngine;
import com.sodabodyfit.moms.R;
import com.sodabodyfit.moms.ExerciseListActivity;

import java.util.ArrayList;

/**
 * Created by owner on 3/7/2017.
 */

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.ViewHolder> {

    Context context;
    ArrayList<Workout> listContent = new ArrayList<Workout>();

    public WorkoutAdapter(Context context, ArrayList<Workout> listContent) {
        this.context = context;
        this.listContent = listContent;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.list_workout, parent, false);
        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Workout item = listContent.get(position);
        holder.tvWorkout.setText(item.title);

        DBEngine dbEngine = new DBEngine(this.context);
        int nCount = dbEngine.getExerciseCount(String.valueOf(item.workout_id));

        holder.tvExerciseNum.setText(nCount + " EXERCISES");

        holder.llWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ExerciseListActivity.class);
                intent.putExtra("workout_title", item.title);
                intent.putExtra("workout_id", item.workout_id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listContent.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvWorkout, tvExerciseNum;
        LinearLayout llWorkout;

        public ViewHolder(View v) {
            super(v);
            llWorkout = (LinearLayout) v.findViewById(R.id.ll_workout);
            tvWorkout = (TextView) v.findViewById(R.id.txt_title);
            tvExerciseNum = (TextView) v.findViewById(R.id.txt_exersise_num);
        }
    }
}
