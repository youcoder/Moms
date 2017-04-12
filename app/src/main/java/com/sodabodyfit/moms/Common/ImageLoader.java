package com.sodabodyfit.moms.Common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.sodabodyfit.moms.Interface.ImageAPI;
import com.sodabodyfit.moms.Models.Image;
import com.sodabodyfit.moms.Provider.DBEngine;

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
    public static void LoadImage(final Context context, final ImageView imageView, String imageId)
    {
        DBEngine dbEngine = new DBEngine(context);
        Image imageInfo = dbEngine.getImageInfo(imageId);

        if(imageInfo.isInAssets)
        {
            Bitmap bMap = BitmapFactory.decodeFile(context.getFilesDir() + File.separator + imageInfo.name + ".png");
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
                        boolean bSuccess = DownloadImage(response.body(), context, imageView, imageName);

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

            Bitmap bMap = BitmapFactory.decodeFile(context.getFilesDir() + File.separator + fileName + ".png");
            imageView.setImageBitmap(bMap);

            DBEngine dbEngine = new DBEngine(context);
            dbEngine.updateImageInAssets(fileName);

            return true;

        } catch (IOException e) {
            Log.d("DownloadImage",e.toString());
            return false;
        }
    }
}
