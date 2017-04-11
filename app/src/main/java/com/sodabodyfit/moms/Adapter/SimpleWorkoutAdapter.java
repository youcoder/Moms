package com.sodabodyfit.moms.Adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sodabodyfit.moms.Models.Workout;
import com.sodabodyfit.moms.R;

import java.util.ArrayList;

/**
 * Created by owner on 3/7/2017.
 */

public class SimpleWorkoutAdapter extends RecyclerView.Adapter<SimpleWorkoutAdapter.ViewHolder> {

    Context context;
    ArrayList<Workout> listContent;

    public SimpleWorkoutAdapter(Context context, ArrayList<Workout> listContent) {
        this.context = context;
        this.listContent = listContent;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.list_simple_workout, parent, false);
        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Workout item = listContent.get(position);
        holder.tvWorkout.setText(item.title);

        holder.tvWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, ContentsActivity.class);
//                intent.putExtra("tab", 3);
//                intent.putExtra("position", position);
//                context.startActivity(intent);
                Toast.makeText(context, String.valueOf(item.workout_id), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listContent.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvWorkout;

        public ViewHolder(View v) {
            super(v);
            tvWorkout = (TextView) v.findViewById(R.id.txt_content);
        }
    }
}
