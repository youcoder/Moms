package com.sodabodyfit.moms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sodabodyfit.moms.Common.Constants;
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
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ExerciseListActivity extends Activity implements View.OnClickListener {

    public static final String WORKOUT_ID_KEY = "workout_id";

    private ListView m_PhotoList = null;
    private int m_nWorkoutId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);

        m_nWorkoutId = getIntent().getIntExtra(WORKOUT_ID_KEY, 1);

        init();
    }

    private void init()
    {
        ((ImageView)findViewById(R.id.btn_back)).setOnClickListener(this);
        ((ImageView)findViewById(R.id.btn_schema)).setOnClickListener(this);

        m_PhotoList = (ListView)findViewById(R.id.photo_list);

        DBEngine dbEngine = new DBEngine(this);
        Workout workout = dbEngine.getWorkoutInfo(m_nWorkoutId + 1);
        ((TextView)findViewById(R.id.tv_workout)).setText(workout.title);

        List<Exercise> exerciseList = dbEngine.getExerciseList(m_nWorkoutId + 1);

        m_PhotoList.setAdapter(new PhotoAdapter(this, exerciseList));

        m_PhotoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onMoveHelp(position + 11);  // test code
            }
        });
    }

    private void onMoveHelp(int position)
    {
        Intent intent = new Intent(this, ExerciseActivity.class)
                .putExtra(ExerciseActivity.EXERCISE_ID_KEY, position);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int nResId = v.getId();

        switch (nResId)
        {
            case R.id.btn_back:
                onBackPressed();
                finish();
                break;
            case R.id.btn_schema:
                onSchemaPressed();
                break;
        }
    }

    private void onSchemaPressed()
    {
        Intent intent = new Intent(this, SchemasActivity.class)
                .putExtra(SchemasActivity.WORKOUT_ID_KEY, m_nWorkoutId);
        startActivity(intent);
    }

    public class PhotoAdapter extends BaseAdapter
    {

        private List<Exercise> m_PhotoList = null;
        private LayoutInflater m_Inflater = null;
        private Context m_Context = null;

        public PhotoAdapter(Context conext, List<Exercise> exerciseList)
        {
            m_PhotoList = exerciseList;
            m_Inflater = LayoutInflater.from(conext);
            m_Context = conext;
        }

        @Override
        public int getCount() {
            return m_PhotoList.size();
        }

        @Override
        public Object getItem(int position) {
            return m_PhotoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null)
                convertView = m_Inflater.inflate(R.layout.photo_item, parent, false);

            Exercise curExercise = m_PhotoList.get(position);
            ((TextView)convertView.findViewById(R.id.tv_subject)).setText(curExercise.title);

            String[] imageIds = curExercise.images.split(",");

            if(imageIds.length > 0)
                LoadImage(convertView, imageIds[0]);

            return convertView;
        }

        // added by usc on 2017/04/10
        public void LoadImage(final View convertView, String imageId)
        {
            DBEngine dbEngine = new DBEngine(m_Context);
            Image imageInfo = dbEngine.getImageInfo(imageId);

            if(imageInfo.isInAssets)
            {
                ImageView image = (ImageView) convertView.findViewById(R.id.img_exercise);
                Bitmap bMap = BitmapFactory.decodeFile(getFilesDir() + File.separator + imageInfo.name + ".png");
                image.setImageBitmap(bMap);
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
                            boolean bSuccess = DownloadImage(response.body(), convertView, imageName);

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

        private boolean DownloadImage(ResponseBody body, View convertView, String fileName) {

            try {
                Log.d("DownloadImage", "Reading and writing file");
                InputStream in = null;
                FileOutputStream out = null;

                try {
                    in = body.byteStream();
                    out = new FileOutputStream(getFilesDir() + File.separator + fileName + ".png");
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

                ImageView image = (ImageView) convertView.findViewById(R.id.img_exercise);
                Bitmap bMap = BitmapFactory.decodeFile(getFilesDir() + File.separator + fileName + ".png");
                image.setImageBitmap(bMap);

                DBEngine dbEngine = new DBEngine(m_Context);
                dbEngine.updateImageInAssets(fileName);

                return true;

            } catch (IOException e) {
                Log.d("DownloadImage",e.toString());
                return false;
            }
        }
    }
}
