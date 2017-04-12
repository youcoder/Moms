package com.sodabodyfit.moms.Adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sodabodyfit.moms.Common.Constants;
import com.sodabodyfit.moms.Common.ImageLoader;
import com.sodabodyfit.moms.ExerciseActivity;
import com.sodabodyfit.moms.ExerciseListActivity;
import com.sodabodyfit.moms.Interface.ImageAPI;
import com.sodabodyfit.moms.Models.Exercise;
import com.sodabodyfit.moms.Models.Image;
import com.sodabodyfit.moms.Models.Workout;
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

        if(item.like) holder.ivFavourite.setVisibility(View.VISIBLE);
        else holder.ivFavourite.setVisibility(View.GONE);

        String[] imageIds = item.images.split(",");
        if(imageIds.length > 0)
            ImageLoader.LoadImage(context, holder.ivExercise, imageIds[0]);

        holder.ivExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ExerciseActivity.class);
                intent.putExtra("exercise", item);
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
