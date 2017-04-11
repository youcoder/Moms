package com.sodabodyfit.moms;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SchemasActivity extends Activity implements View.OnClickListener {

    public static final String WORKOUT_ID_KEY = "workout_id";

    private Typeface m_TypeFace = null;
    private int m_nWorkoutId = 0;

    public class SchemasInfo
    {
        public String _title = "";
        public String _Set = "";
        public String _Reps = "";
        public String _Time = "";
        public String _Rest = "";
        public String _Kg = "";

        public SchemasInfo(String title, String set, String reps, String time, String rest, String kg)
        {
            _title = title;
            _Set = set;
            _Reps = reps;
            _Time = time;
            _Rest = rest;

            if(kg == null) _Kg = "";
            else _Kg = kg;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schemas);

        m_nWorkoutId = getIntent().getIntExtra(WORKOUT_ID_KEY, 1);

        init();
    }

    private void init()
    {
        m_TypeFace = Typeface.createFromAsset(getAssets(), "fonts/Aleo-Bold.otf");

        DBEngine dbEngine = new DBEngine(this);
        Workout workout = dbEngine.getWorkoutInfo(m_nWorkoutId + 1);

        TextView tv_Schemas = (TextView)findViewById(R.id.tv_schemas);
        tv_Schemas.setTypeface(m_TypeFace);
        tv_Schemas.setText(workout.title);

        TextView tv_Schemas_label = (TextView)findViewById(R.id.tv_schemas_label);
        tv_Schemas_label.setTypeface(m_TypeFace);

        TextView tv_SchemasInfo = (TextView) findViewById(R.id.tv_schemas_info);
        if(workout.infoDisplayed) {
            tv_SchemasInfo.setText(workout.info);
            tv_SchemasInfo.setVisibility(View.VISIBLE);
        }
        else
            tv_SchemasInfo.setVisibility(View.GONE);

        LoadImage(workout.image);

        ImageView btnClose = (ImageView) findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);

        List<Exercise> exerciseList = dbEngine.getExerciseList(m_nWorkoutId + 1);

        ArrayList<SchemasInfo> schemaInfoList = new ArrayList<SchemasInfo>();
        for(int i = 0; i < exerciseList.size(); i++)
        {
            Exercise exercise = exerciseList.get(i);
            schemaInfoList.add(new SchemasInfo(exercise.title, exercise.sets, exercise.repetions, exercise.times, exercise.rest, exercise.kg));
        }

        ListView listView = (ListView)findViewById(R.id.schema_list);
        listView.setAdapter(new SchemaAdapter(this, schemaInfoList));
    }

    public void LoadImage(String imageId)
    {
        DBEngine dbEngine = new DBEngine(this);
        Image imageInfo = dbEngine.getImageInfo(imageId);

        if(imageInfo.isInAssets)
        {
            ImageView image = (ImageView) findViewById(R.id.schema_pic);
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
                        boolean bSuccess = DownloadImage(response.body(), imageName);

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

    private boolean DownloadImage(ResponseBody body, String fileName) {

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

            ImageView image = (ImageView) findViewById(R.id.schema_pic);
            Bitmap bMap = BitmapFactory.decodeFile(getFilesDir() + File.separator + fileName + ".png");
            image.setImageBitmap(bMap);

            DBEngine dbEngine = new DBEngine(this);
            dbEngine.updateImageInAssets(fileName);

            return true;

        } catch (IOException e) {
            Log.d("DownloadImage",e.toString());
            return false;
        }
    }

    @Override
    public void onClick(View v) {

        int nResId = v.getId();
        switch (nResId)
        {
            case R.id.btn_close:
                onClose();
                break;
        }
    }

    private void onClose()
    {
        onBackPressed();
    }

    public class SchemaAdapter extends BaseAdapter
    {
        Context m_context = null;
        ArrayList<SchemasInfo> m_SchemaList = null;

        public SchemaAdapter(Context context, ArrayList<SchemasInfo> schemaList)
        {
            m_context = context;
            m_SchemaList = schemaList;
        }

        @Override
        public int getCount() {
            return m_SchemaList.size();
        }

        @Override
        public Object getItem(int position) {
            return m_SchemaList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null)
            {
                convertView = LayoutInflater.from(m_context).inflate(R.layout.schemas_item, parent, false);
                TextView tv_set_label_schema = (TextView)convertView.findViewById(R.id.tv_set_label_schema);
                tv_set_label_schema.setTypeface(m_TypeFace);

                TextView tv_reps_label_schema = (TextView)convertView.findViewById(R.id.tv_reps_label_schema);
                tv_reps_label_schema.setTypeface(m_TypeFace);

                TextView tv_time_label_schema = (TextView)convertView.findViewById(R.id.tv_time_label_schema);
                tv_time_label_schema.setTypeface(m_TypeFace);

                TextView tv_rest_label_schema = (TextView)convertView.findViewById(R.id.tv_rest_label_schema);
                tv_rest_label_schema.setTypeface(m_TypeFace);

                TextView tv_kg_label_schema = (TextView)convertView.findViewById(R.id.tv_kg_label_schema);
                tv_kg_label_schema.setTypeface(m_TypeFace);
            }

            TextView tv_Title = (TextView)convertView.findViewById(R.id.schema_content);
            tv_Title.setText(m_SchemaList.get(position)._title);
            tv_Title.setTypeface(m_TypeFace);
            TextView tv_set = (TextView)convertView.findViewById(R.id.tv_set_schema);
            tv_set.setText(String.valueOf(m_SchemaList.get(position)._Set));
            tv_set.setTypeface(m_TypeFace);
            TextView tv_reps = (TextView)convertView.findViewById(R.id.tv_reps_schema);
            tv_reps.setText(String.valueOf(m_SchemaList.get(position)._Reps));
            tv_reps.setTypeface(m_TypeFace);
            TextView tv_time = (TextView)convertView.findViewById(R.id.tv_time_schema);
            tv_time.setText(String.valueOf(m_SchemaList.get(position)._Time) + " sec");
            tv_time.setTypeface(m_TypeFace);
            TextView tv_rest = (TextView)convertView.findViewById(R.id.tv_rest_schema);
            tv_rest.setText(String.valueOf(m_SchemaList.get(position)._Rest) + " sec");
            tv_rest.setTypeface(m_TypeFace);
            TextView tv_kg = (TextView)convertView.findViewById(R.id.tv_kg_schema);
            tv_kg.setText(String.valueOf(m_SchemaList.get(position)._Kg));
            tv_kg.setTypeface(m_TypeFace);

            return convertView;
        }
    }
}
