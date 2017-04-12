package com.sodabodyfit.moms.Adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sodabodyfit.moms.Common.Constants;
import com.sodabodyfit.moms.ExerciseActivity;
import com.sodabodyfit.moms.Interface.ImageAPI;
import com.sodabodyfit.moms.Models.Exercise;
import com.sodabodyfit.moms.Models.Image;
import com.sodabodyfit.moms.Provider.DBEngine;
import com.sodabodyfit.moms.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by owner on 3/7/2017.
 */

public class SchemasAdapter extends RecyclerView.Adapter<SchemasAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Exercise> lstExercise = new ArrayList<Exercise>();
    private Typeface typeFace;

    public SchemasAdapter(Context context, ArrayList<Exercise> listContent, Typeface typeFace) {
        this.context = context;
        this.lstExercise = listContent;
        this.typeFace = typeFace;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.list_schemas, parent, false);
        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Exercise item = lstExercise.get(position);
        holder.tvSubject.setText(item.title);
        holder.tvSet.setText(item.sets);
        holder.tvReps.setText(item.repetions);
        holder.tvTime.setText(item.times + " sec");
        holder.tvRest.setText(item.rest + " sec");
        holder.tvKg.setText(item.kg);

    }

    @Override
    public int getItemCount() {
        return lstExercise.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubject, tvSet, tvReps, tvTime, tvRest, tvKg;

        public ViewHolder(View v) {
            super(v);
            tvSubject = (TextView) v.findViewById(R.id.txt_schema_content);
            tvSubject.setTypeface(typeFace);
            tvSet = (TextView) v.findViewById(R.id.txt_set_schema);
            tvSet.setTypeface(typeFace);
            tvReps = (TextView) v.findViewById(R.id.txt_reps_schema);
            tvReps.setTypeface(typeFace);
            tvTime = (TextView) v.findViewById(R.id.txt_time_schema);
            tvTime.setTypeface(typeFace);
            tvRest = (TextView) v.findViewById(R.id.txt_rest_schema);
            tvRest.setTypeface(typeFace);
            tvKg = (TextView) v.findViewById(R.id.txt_kg_schema);
            tvKg.setTypeface(typeFace);
        }
    }
}
