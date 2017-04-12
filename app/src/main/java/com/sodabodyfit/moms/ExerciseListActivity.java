package com.sodabodyfit.moms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.sodabodyfit.moms.Adapter.ExerciseListAdapter;
import com.sodabodyfit.moms.Models.Exercise;
import com.sodabodyfit.moms.Provider.DBEngine;

import java.util.ArrayList;

public class ExerciseListActivity extends AppCompatActivity {

    private ArrayList<Exercise> lstExercise = new ArrayList<Exercise>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String title = intent.getStringExtra("workout_title");
        int workoutId = intent.getIntExtra("workout_id", -1);

        TextView tvTitle = (TextView)findViewById(R.id.txt_title);
        tvTitle.setText(title);

        DBEngine dbEngine = new DBEngine(this);
        lstExercise = dbEngine.getExerciseList(workoutId);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(layoutManager);

        ExerciseListAdapter adapter = new ExerciseListAdapter(ExerciseListActivity.this, lstExercise);
        recycler.setAdapter(adapter);

    }

//    public class PhotoAdapter extends BaseAdapter {
//
//        private List<Exercise> m_PhotoList = null;
//        private LayoutInflater m_Inflater = null;
//        private Context m_Context = null;
//
//        public PhotoAdapter(Context conext, List<Exercise> exerciseList)
//        {
//            m_PhotoList = exerciseList;
//            m_Inflater = LayoutInflater.from(conext);
//            m_Context = conext;
//        }
//
//        @Override
//        public int getCount() {
//            return m_PhotoList.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return m_PhotoList.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//
//            if(convertView == null)
//                convertView = m_Inflater.inflate(R.layout.list_exercise, parent, false);
//
//            Exercise curExercise = m_PhotoList.get(position);
//            ((TextView)convertView.findViewById(R.id.tv_subject)).setText(curExercise.title);
//
//            String[] imageIds = curExercise.images.split(",");
//
//            if(imageIds.length > 0)
//                LoadImage(convertView, imageIds[0]);
//
//            return convertView;
//        }
//
//        // added by usc on 2017/04/10
//        public void LoadImage(final View convertView, String imageId)
//        {
//            DBEngine dbEngine = new DBEngine(m_Context);
//            Image imageInfo = dbEngine.getImageInfo(imageId);
//
//            if(imageInfo.isInAssets)
//            {
//                ImageView image = (ImageView) convertView.findViewById(R.id.img_exercise);
//                Bitmap bMap = BitmapFactory.decodeFile(getFilesDir() + File.separator + imageInfo.name + ".png");
//                image.setImageBitmap(bMap);
//            }
//            else
//            {
//                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl(Constants.BASE_URL)
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build();
//
//                ImageAPI service = retrofit.create(ImageAPI.class);
//
//                Call<ResponseBody> call = service.getImageRequest("Token token=Z6VXst4ia9f3ayUwrTDVgypT", imageInfo.name);
//
//                call.enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        try {
//
//                            Log.d("onResponse", "Response came from server");
//
//                            String imageName = response.raw().request().url().queryParameterValue(0);
//                            boolean bSuccess = DownloadImage(response.body(), convertView, imageName);
//
//                            Log.d("onResponse", "Image is downloaded and saved ? " + bSuccess);
//
//                        } catch (Exception e) {
//                            Log.d("onResponse", "There is an error");
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        Log.d("onFailure", t.toString());
//                    }
//                });
//            }
//        }
//
//        private boolean DownloadImage(ResponseBody body, View convertView, String fileName) {
//
//            try {
//                Log.d("DownloadImage", "Reading and writing file");
//                InputStream in = null;
//                FileOutputStream out = null;
//
//                try {
//                    in = body.byteStream();
//                    out = new FileOutputStream(getFilesDir() + File.separator + fileName + ".png");
//                    int c;
//
//                    while ((c = in.read()) != -1) {
//                        out.write(c);
//                    }
//                }
//                catch (IOException e) {
//                    Log.d("DownloadImage",e.toString());
//                    return false;
//                }
//                finally {
//                    if (in != null) {
//                        in.close();
//                    }
//                    if (out != null) {
//                        out.close();
//                    }
//                }
//
//                ImageView image = (ImageView) convertView.findViewById(R.id.img_exercise);
//                Bitmap bMap = BitmapFactory.decodeFile(getFilesDir() + File.separator + fileName + ".png");
//                image.setImageBitmap(bMap);
//
//                DBEngine dbEngine = new DBEngine(m_Context);
//                dbEngine.updateImageInAssets(fileName);
//
//                return true;
//
//            } catch (IOException e) {
//                Log.d("DownloadImage",e.toString());
//                return false;
//            }
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getMenuInflater().inflate(R.menu.menu_exercise_list, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_info) {
            Intent intent = new Intent(ExerciseListActivity.this, ExerciseActivity.class);
            startActivity(intent);
        }else if (id == android.R.id.home) {
            ExerciseListActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
