package com.sodabodyfit.moms.Common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sodabodyfit.moms.Interface.ImageAPI;
import com.sodabodyfit.moms.Models.Image;
import com.sodabodyfit.moms.Provider.DBEngine;
import com.sodabodyfit.moms.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by DEVMAN86 on 4/12/2017.
 */

public class ImageLoader {
    public static void LoadImage(final Context context, final ImageView imageView, final String imageId)
    {
        try {
            DBEngine dbEngine = new DBEngine(context);
            Image imageInfo = dbEngine.getImageInfo(imageId);

            if (imageInfo.isInAssets) {
                String imagePath = context.getFilesDir() + File.separator + imageInfo.name + ".png";
                Glide.with(context).load(imagePath).placeholder(R.drawable.loading).into(imageView);
            } else {
                if(imageInfo.path) return;

                dbEngine.updateImagePath(imageId, true);    // state = downloading...

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ImageAPI service = retrofit.create(ImageAPI.class);
                Call<ResponseBody> call = service.getImageRequest("Token token=" + User.token, imageInfo.name);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {

                            Log.d("onResponse", "Response came from server");

                            if (response.body() != null) {
                                String imageName = response.raw().request().url().queryParameterValue(0);
                                boolean bSuccess = DownloadImage(response.body(), context, imageView, imageName);

                                Log.d("onResponse", imageName + ".png is downloaded and saved ? " + bSuccess);
                            } else
                                Log.d("onResponse", "Response data is null");

                        } catch (Exception e) {
                            Log.d("onResponse", "There is an error");
                            e.printStackTrace();
                        }
                        finally {
                            DBEngine dbEngine = new DBEngine(context);
                            dbEngine.updateImagePath(imageId, false);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("onFailure", t.toString());

                        DBEngine dbEngine = new DBEngine(context);
                        dbEngine.updateImagePath(imageId, false);
                    }
                });
            }
        }
        catch (Exception e){
            Log.d("LoadImage", e.toString());
        }
    }

    private static boolean DownloadImage(ResponseBody body, Context context, ImageView imageView, String fileName) {

        try {
            Log.d("DownloadImage", "Reading and writing file");
            InputStream in = null;
            FileOutputStream out = null;

            try {
                out = new FileOutputStream(context.getFilesDir() + File.separator + fileName + ".png");
                out.write(body.bytes());
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

            String imagePath = context.getFilesDir() + File.separator + fileName + ".png";
            Glide.with(context).load(imagePath).placeholder(R.drawable.loading).into(imageView);

            DBEngine dbEngine = new DBEngine(context);
            dbEngine.updateImageInAssets(fileName);

            return true;

        } catch (IOException e) {
            Log.d("DownloadImage",e.toString());
            return false;
        }
    }
}
