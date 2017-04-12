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
            LoadImage(holder.ivExercise, imageIds[0]);

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

    public void LoadImage(final ImageView imageView, String imageId)
    {
        DBEngine dbEngine = new DBEngine(context);
        Image imageInfo = dbEngine.getImageInfo(imageId);

        if(imageInfo.isInAssets)
        {
            Bitmap bMap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + File.separator + imageInfo.name + ".png");
            imageView.setImageBitmap(bMap);
        }
        else
        {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ImageAPI service = retrofit.create(ImageAPI.class);

            Call<ResponseBody> call = service.getImageRequest("Token token=Z6VXst4ia9f3ayUwrTDVgypT", imageInfo.name);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {

                        Log.d("onResponse", "Response came from server");

                        String imageName = response.raw().request().url().queryParameterValue(0);
                        boolean bSuccess = DownloadImage(response.body(), imageView, imageName);

                        Log.d("onResponse", "Image is downloaded and saved ? " + bSuccess);

                    } catch (Exception e) {
                        Log.d("onResponse", "There is an error");
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("onFailure", t.toString());
                }
            });
        }
    }

    private boolean DownloadImage(ResponseBody body, ImageView imageView, String fileName) {

        try {
            Log.d("DownloadImage", "Reading and writing file");
            InputStream in = null;
            FileOutputStream out = null;

            try {
                in = body.byteStream();
                out = new FileOutputStream(Environment.getExternalStorageDirectory() + File.separator + fileName + ".png");
                int c;

                while ((c = in.read()) != -1) {
                    out.write(c);
                }
            }
            catch (IOException e) {
                Log.d("DownloadImage",e.toString());
                return false;
            }
            finally {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }

            Bitmap bMap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + File.separator + fileName + ".png");
            imageView.setImageBitmap(bMap);

            DBEngine dbEngine = new DBEngine(context);
            dbEngine.updateImageInAssets(fileName);

            return true;

        } catch (IOException e) {
            Log.d("DownloadImage",e.toString());
            return false;
        }
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
