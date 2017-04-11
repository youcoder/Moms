package com.sodabodyfit.moms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sodabodyfit.moms.Common.Constants;
import com.sodabodyfit.moms.Common.DrawView;
import com.sodabodyfit.moms.Interface.ImageAPI;
import com.sodabodyfit.moms.Models.Exercise;
import com.sodabodyfit.moms.Models.Image;
import com.sodabodyfit.moms.Provider.DBEngine;
import com.sodabodyfit.moms.R;

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


public class ExerciseActivity extends Activity implements View.OnClickListener {
    public static final String EXERCISE_ID_KEY = "exercise_id";

    private int m_nExerciseId = 0;

    private ViewPager m_PhotoPager = null;
    private LinearLayout m_PageIndicator = null;
    private ViewPagerAdapter m_PagerAdapter = null;
    private ImageView[] m_PageIndicatorDots = null;
    private ImageView m_Play = null;
    private ImageView m_AlarmPlay = null;
    private ImageView m_Favourite = null;
    private DrawView m_UpdatePanel = null;
    private int m_PageCount = 0;

    private int m_Set = 3;
    private int m_CurSet = 3;

    private Handler m_PlayHandler = null;
    private Runnable m_PlayRunnable = null;
    public static final int PLAY_DELAY = 1000;//0.5Sec

    private Handler m_ExerciseHandler = null;
    private Runnable m_ExerciseRunnable = null;
    public static final int EXERCISE_DELAY = 1000;//1Sec
    private int m_ExerciseTime = 15;//in sec
    private int m_CurExerciseTime = 15;

    private Handler m_RestHandler = null;
    private Runnable m_RestRunnable = null;
    public static final int REST_DELAY = 1000;//1Sec
    private int m_RestTime = 10;//in sec
    private int m_CurRestTime = 10;

    private int m_CurrentPageNum = 0;
    private boolean m_isPlay = false;
    private boolean m_isAlarm = false;
    private boolean m_isFavourite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_exercise);

        m_nExerciseId = getIntent().getIntExtra(EXERCISE_ID_KEY, 1);

        init();
    }

    private void init()
    {
        DBEngine dbEngine = new DBEngine(this);
        Exercise exercise = dbEngine.getExerciseInfo(m_nExerciseId);

        TextView tvTitle = (TextView)findViewById(R.id.tv_title);
        tvTitle.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Aleo-Bold.otf"));
        tvTitle.setText(exercise.title);

        TextView label_set = (TextView)findViewById(R.id.label_set);
        label_set.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Aleo-Bold.otf"));
        TextView tvSet = (TextView)findViewById(R.id.tv_set);
        tvSet.setText(exercise.sets);

        TextView label_reps = (TextView)findViewById(R.id.label_reps);
        label_reps.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Aleo-Bold.otf"));
        TextView tvReps = (TextView)findViewById(R.id.tv_reps);
        tvReps.setText(exercise.repetions);

        TextView label_time = (TextView)findViewById(R.id.label_tiem);
        label_time.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Aleo-Bold.otf"));
        TextView tvTime = (TextView)findViewById(R.id.tv_time);
        tvTime.setText(exercise.times + " sec");

        TextView label_rest = (TextView)findViewById(R.id.label_rest);
        label_rest.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Aleo-Bold.otf"));
        TextView tvRest = (TextView)findViewById(R.id.tv_rest);
        tvRest.setText(exercise.rest + " sec");

        TextView label_kg = (TextView)findViewById(R.id.label_kg);
        label_kg.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Aleo-Bold.otf"));
        TextView tvKg = (TextView)findViewById(R.id.tv_kg);
        tvKg.setText(exercise.kg);

        TextView label_starting_position = (TextView)findViewById(R.id.label_starting_position);
        label_starting_position.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Aleo-Bold.otf"));
        TextView tvStartPosition = (TextView)findViewById(R.id.tv_starting_position);
        tvStartPosition.setText(exercise.initialPosition);

        TextView label_movement = (TextView)findViewById(R.id.label_movement);
        label_movement.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Aleo-Bold.otf"));
        TextView tvMovement = (TextView)findViewById(R.id.tv_movment);
        tvMovement.setText(exercise.movement);

        TextView label_points_remember = (TextView)findViewById(R.id.label_points_remember);
        label_points_remember.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Aleo-Bold.otf"));
        TextView tvPointRemember = (TextView)findViewById(R.id.tv_points_remember);
        tvPointRemember.setText(exercise.points);

        TextView tvBack = (TextView)findViewById(R.id.tv_back);
        tvBack.setText(dbEngine.getWorkoutNameByExerciseId(m_nExerciseId));

        ImageView btn_back = (ImageView)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        ImageView btn_plus = (ImageView)findViewById(R.id.btn_plus);
        btn_plus.setOnClickListener(this);

        m_Set = Integer.parseInt(exercise.sets);

        m_ExerciseTime = 15;
        m_UpdatePanel = (DrawView)findViewById(R.id.update_panel);
        m_UpdatePanel.init(m_ExerciseTime);

        m_RestTime = Integer.parseInt(exercise.rest);

        m_Play = (ImageView)findViewById(R.id.btn_play);
        m_Play.setOnClickListener(this);

        m_AlarmPlay = (ImageView)findViewById(R.id.btn_alarm);
        m_AlarmPlay.setOnClickListener(this);
        m_Favourite = (ImageView) findViewById(R.id.btn_favourites);
        m_Favourite.setOnClickListener(this);

        m_isFavourite = dbEngine.isFavourite(m_nExerciseId);

        if(m_isFavourite)
            m_Favourite.setImageResource(R.drawable.favourites_icon_select);
        else
            m_Favourite.setImageResource(R.drawable.favourites_icon);

        m_PhotoPager = (ViewPager)findViewById(R.id.photo_pager);

        m_PhotoPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < m_PageCount; i++) {
                    m_PageIndicatorDots[i].setImageResource(R.drawable.nonselecteditem_dot);
                }

                m_PageIndicatorDots[position].setImageResource(R.drawable.selecteditem_dot);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        int[] photoList = {R.drawable.p_1_1_0, R.drawable.p_1_1_1, R.drawable.p_1_1_2};
        m_PagerAdapter = new ViewPagerAdapter(this, photoList);
        m_PhotoPager.setAdapter(m_PagerAdapter);
        m_PageIndicator = (LinearLayout)findViewById(R.id.viewPagerCountDots);
        setUiPageViewController();

        //ViewPager auto play animation
        m_PlayRunnable = new Runnable() {
            @Override
            public void run() {
                updatePage();
                m_PlayHandler.postDelayed(this, PLAY_DELAY);
            }
        };

        m_PlayHandler = new Handler();

        //Alarm exercise play animation
        m_ExerciseRunnable = new Runnable() {
            @Override
            public void run() {

                m_ExerciseHandler.postDelayed(this, EXERCISE_DELAY);

                if(m_CurExerciseTime >= 0)
                    countDownExercise();
                else
                    stopExercise();
            }
        };

        m_ExerciseHandler = new Handler();

        m_RestRunnable = new Runnable() {
            @Override
            public void run() {

                m_RestHandler.postDelayed(this, REST_DELAY);

                if(m_CurRestTime >= 0)
                    countDownRest();
                else
                    stopRest();
            }
        };

        m_RestHandler = new Handler();

    }


    private void startRest()
    {
        m_CurRestTime = m_RestTime;
        m_UpdatePanel.setTimeCount(m_CurRestTime, "REST");
        m_RestHandler.postDelayed(m_RestRunnable, REST_DELAY);
    }

    private void countDownRest()
    {
        m_CurRestTime--;
        m_UpdatePanel.setYPos(m_CurRestTime);
    }
    private void stopRest() {
        m_ExerciseHandler.removeCallbacks(m_ExerciseRunnable);
        m_RestHandler.removeCallbacks(m_RestRunnable);

        m_CurSet--;
        if (m_CurSet == 0) {
            stopPlay();
            m_UpdatePanel.ExitDraw();
            m_CurSet = m_Set;
        }
        else
        {
            //repeat
            startExercise();
        }
    }
    private void startExercise()
    {
        m_CurExerciseTime = m_ExerciseTime;
        m_UpdatePanel.setTimeCount(m_CurExerciseTime, "EXERCISE");
        m_UpdatePanel.reset();
        m_ExerciseHandler.postDelayed(m_ExerciseRunnable, EXERCISE_DELAY);
    }

    private void countDownExercise()
    {
        m_CurExerciseTime--;
        m_UpdatePanel.setYPos(m_CurExerciseTime);
    }

    private void stopExercise()
    {
        m_ExerciseHandler.removeCallbacks(m_ExerciseRunnable);
        startRest();
    }

    private void updatePage()
    {
        m_PhotoPager.setCurrentItem(m_CurrentPageNum, false);
        m_CurrentPageNum++;
        if(m_CurrentPageNum > m_PageCount)
            m_CurrentPageNum = 0;
    }

    private void setUiPageViewController() {

        m_PageCount = m_PagerAdapter.getCount();
        m_PageIndicatorDots = new ImageView[m_PageCount];

        for (int i = 0; i < m_PageCount; i++) {
            m_PageIndicatorDots[i] = new ImageView(this);
            m_PageIndicatorDots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(8, 0, 8, 0);

            m_PageIndicator.addView(m_PageIndicatorDots[i], params);
        }

        m_PageIndicatorDots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    @Override
    public void onClick(View v) {

        int nResId  = v.getId();

        switch (nResId)
        {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_play:
                onClickPlay();
                break;
            case R.id.btn_alarm:
                onClickAlarm();
                break;
            case R.id.btn_favourites:
                onClickFavourite();
                break;
            case R.id.btn_plus:
                onClickPlus();
                break;
        }

    }

    private void onClickPlus(){
        Intent intent = new Intent(this, PlusActivity.class);
        startActivity(intent);
    }

    private void onClickFavourite() {
        if(m_isFavourite)
        {
            m_isFavourite = false;
            m_Favourite.setImageResource(R.drawable.favourites_icon);
        }
        else
        {
            m_isFavourite = true;
            m_Favourite.setImageResource(R.drawable.favourites_icon_select);
        }

        DBEngine dbEngine = new DBEngine(this);
        dbEngine.updateFavourite(m_nExerciseId, m_isFavourite);
    }

    private void onClickAlarm() {
        if(m_isAlarm)
        {
            m_isAlarm = false;
            m_AlarmPlay.setImageResource(R.drawable.alarm_icon);
            m_UpdatePanel.setVisibility(View.GONE);
            stopRest();
        }
        else
        {
            m_isAlarm = true;
            m_AlarmPlay.setImageResource(R.drawable.alarm_icon_select);
            m_UpdatePanel.setVisibility(View.VISIBLE);
            m_CurSet = m_Set;
            m_UpdatePanel.init(15);
            m_UpdatePanel.reset();
            stopPlay();
        }
    }


    private void stopPlay(){
        if(m_isPlay)
        {
            m_isPlay = false;
            m_PlayHandler.removeCallbacks(m_PlayRunnable);
            m_Play.setImageResource(R.drawable.play_icon);
        }
    }

    private void onClickPlay() {
        if(m_isPlay)
        {
            m_isPlay = false;
            m_PlayHandler.removeCallbacks(m_PlayRunnable);
            m_Play.setImageResource(R.drawable.play_icon);
        }
        else
        {
            m_isPlay = true;
            m_PlayHandler.postDelayed(m_PlayRunnable, PLAY_DELAY);
            m_Play.setImageResource(R.drawable.pause_icon);
            if(m_isAlarm)
                startExercise();
        }
    }

    /**
     * Created by MokRan on 2017-02-11.
     */

    public class ViewPagerAdapter extends PagerAdapter {


        private Context mContext;
        private int[] mPaths = null;

        public ViewPagerAdapter(Context context, int[] paths)
        {
            mContext = context;
            mPaths = paths;
        }

        @Override
        public int getCount() {
            return mPaths.length;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View itemView = LayoutInflater.from(mContext).inflate(R.layout.help_photo, container, false);

            DBEngine dbEngine = new DBEngine(mContext);
            Exercise exercise = dbEngine.getExerciseInfo(m_nExerciseId);

            String[] imageIds = exercise.images.split(",");
            if(imageIds.length > 0)
                LoadImage(itemView, imageIds[0]);

            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout)object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout)object);
        }

        // added by usc on 2017/04/10
        public void LoadImage(final View convertView, String imageId)
        {
            DBEngine dbEngine = new DBEngine(mContext);
            Image imageInfo = dbEngine.getImageInfo(imageId);

            if(imageInfo.isInAssets)
            {
                ImageView image = (ImageView) convertView.findViewById(R.id.img_photo);
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

                ImageView image = (ImageView) convertView.findViewById(R.id.img_photo);
                Bitmap bMap = BitmapFactory.decodeFile(getFilesDir() + File.separator + fileName + ".png");
                image.setImageBitmap(bMap);

                DBEngine dbEngine = new DBEngine(mContext);
                dbEngine.updateImageInAssets(fileName);

                return true;

            } catch (IOException e) {
                Log.d("DownloadImage",e.toString());
                return false;
            }
        }
    }
}
