package com.sodabodyfit.moms;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.sodabodyfit.moms.Adapter.WorkoutAdapter;
import com.sodabodyfit.moms.Common.DividerItemDecoration;
import com.sodabodyfit.moms.Models.Workout;

import java.util.ArrayList;

public class ExpandWorkoutActivity extends AppCompatActivity {

    private ArrayList<Workout> lstWorkout = new ArrayList<Workout>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        int categoryId = bundle.getInt("category_id");
        int resId = bundle.getInt("image");
        lstWorkout = bundle.getParcelableArrayList("workout");

        TextView tvTitle = (TextView)findViewById(R.id.txt_title);
        tvTitle.setText(getResources().getStringArray(R.array.title_category)[categoryId]);
        ImageView ivCategory = (ImageView) findViewById(R.id.img_category);
        ivCategory.setImageResource(resId);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(layoutManager);
        recycler.addItemDecoration(new DividerItemDecoration(this));

        WorkoutAdapter adapter = new WorkoutAdapter(ExpandWorkoutActivity.this, lstWorkout);
        recycler.setAdapter(adapter);
    }

//    public void LoadImage(String imageId)
//    {
//        DBEngine dbEngine = new DBEngine(this);
//        Image imageInfo = dbEngine.getImageInfo(imageId);
//
//        if(imageInfo.isInAssets)
//        {
//            ImageView image = (ImageView) findViewById(R.id.iv_photo);
//            Bitmap bMap = BitmapFactory.decodeFile(getFilesDir() + File.separator + imageInfo.name + ".png");
//            image.setImageBitmap(bMap);
//        }
//        else
//        {
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl(Constants.BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//
//            ImageAPI service = retrofit.create(ImageAPI.class);
//
//            Call<ResponseBody> call = service.getImageRequest("Token token=Z6VXst4ia9f3ayUwrTDVgypT", imageInfo.name);
//
//            call.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    try {
//
//                        Log.d("onResponse", "Response came from server");
//
//                        String imageName = response.raw().request().url().queryParameterValue(0);
//                        boolean bSuccess = DownloadImage(response.body(), imageName);
//
//                        Log.d("onResponse", "Image is downloaded and saved ? " + bSuccess);
//
//                    } catch (Exception e) {
//                        Log.d("onResponse", "There is an error");
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    Log.d("onFailure", t.toString());
//                }
//            });
//        }
//    }
//
//    private boolean DownloadImage(ResponseBody body, String fileName) {
//
//        try {
//            Log.d("DownloadImage", "Reading and writing file");
//            InputStream in = null;
//            FileOutputStream out = null;
//
//            try {
//                in = body.byteStream();
//                out = new FileOutputStream(getFilesDir() + File.separator + fileName + ".png");
//                int c;
//
//                while ((c = in.read()) != -1) {
//                    out.write(c);
//                }
//            }
//            catch (IOException e) {
//                Log.d("DownloadImage",e.toString());
//                return false;
//            }
//            finally {
//                if (in != null) {
//                    in.close();
//                }
//                if (out != null) {
//                    out.close();
//                }
//            }
//
//            ImageView image = (ImageView) findViewById(R.id.iv_photo);
//            Bitmap bMap = BitmapFactory.decodeFile(getFilesDir() + File.separator + fileName + ".png");
//            image.setImageBitmap(bMap);
//
//            DBEngine dbEngine = new DBEngine(this);
//            dbEngine.updateImageInAssets(fileName);
//
//            return true;
//
//        } catch (IOException e) {
//            Log.d("DownloadImage",e.toString());
//            return false;
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        ActionBar actionBar = getSupportActionBar();
        getMenuInflater().inflate(R.menu.menu_expand, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_close) {
            ExpandWorkoutActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
