package com.sodabodyfit.moms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sodabodyfit.moms.Common.DrawView;
import com.sodabodyfit.moms.Common.ImageLoader;
import com.sodabodyfit.moms.Models.Exercise;
import com.sodabodyfit.moms.Provider.DBEngine;

public class ExerciseActivity extends AppCompatActivity implements View.OnClickListener {

    private Exercise exercise = new Exercise();
    private ViewPager photoViewPager;
    private LinearLayout pageIndicator = null;
    private ViewPagerAdapter adapter = null;
    private ImageView[] pageIndicatorDots = null;
    private ImageView ivPlay = null;
    private ImageView ivExercise = null;
    private ImageView ivFavourite = null;
    private DrawView updatePanel = null;
    private int pageCount = 0;

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
    private boolean m_isRest = false;
    private boolean m_isFavourite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        exercise = getIntent().getParcelableExtra("exercise");
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/Aleo-Bold.otf");

        TextView tvSubject = (TextView)findViewById(R.id.txt_schema_content);
        tvSubject.setTypeface(typeFace);
        tvSubject.setText(exercise.title.toUpperCase());

        TextView tvSet = (TextView)findViewById(R.id.txt_set);
        tvSet.setText(exercise.sets);

        TextView tvReps = (TextView)findViewById(R.id.txt_reps);
        tvReps.setText(exercise.repetions);

        TextView tvTime = (TextView)findViewById(R.id.txt_time);
        tvTime.setText(exercise.times + " sec");

        TextView tvRest = (TextView)findViewById(R.id.txt_rest);
        tvRest.setText(exercise.rest + " sec");

        TextView tvKg = (TextView)findViewById(R.id.txt_kg);
        tvKg.setText(exercise.kg);

        TextView tvStartPosition = (TextView)findViewById(R.id.txt_starting_position);
        tvStartPosition.setTypeface(typeFace);
        TextView tvStartPositionContent = (TextView)findViewById(R.id.txt_starting_position_content);
        tvStartPositionContent.setText(exercise.initialPosition);

        TextView tvMovement = (TextView)findViewById(R.id.txt_movement);
        tvMovement.setTypeface(typeFace);
        TextView tvMovementContent = (TextView)findViewById(R.id.txt_movment_content);
        tvMovementContent.setText(exercise.movement);

        TextView tvPointRemember = (TextView)findViewById(R.id.txt_points_remember);
        tvPointRemember.setTypeface(typeFace);
        TextView tvPointRememberContent = (TextView)findViewById(R.id.txt_points_remember_content);
        tvPointRememberContent.setText(exercise.points);

        m_Set = Integer.parseInt(exercise.sets);

        m_ExerciseTime = 15;
        updatePanel = (DrawView)findViewById(R.id.update_panel);
        updatePanel.init(m_ExerciseTime);

        String[] restTimes = exercise.rest.split("/");
        if(restTimes.length > 0) m_RestTime = Integer.parseInt(restTimes[0]);

        ivPlay = (ImageView)findViewById(R.id.img_play);
        ivPlay.setOnClickListener(this);

        ivExercise = (ImageView)findViewById(R.id.img_exercise);
        ivExercise.setOnClickListener(this);
        
        ivFavourite = (ImageView)findViewById(R.id.img_favourite);
        ivFavourite.setOnClickListener(this);

        ImageView ivPlus = (ImageView)findViewById(R.id.img_plus);
        ivPlus.setOnClickListener(this);

        ImageView ivBack = (ImageView)findViewById(R.id.img_back);
        ivBack.setOnClickListener(this);

        DBEngine dbEngine = new DBEngine(this);
        m_isFavourite = dbEngine.isFavourite(exercise.exercise_id);

        if(m_isFavourite)
            ivFavourite.setImageResource(R.drawable.ic_vote);
        else
            ivFavourite.setImageResource(R.drawable.ic_unvote);

        photoViewPager = (ViewPager)findViewById(R.id.photo_pager);

        photoViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < pageCount; i++) {
                    pageIndicatorDots[i].setImageResource(R.drawable.nonselecteditem_dot);
                }

                pageIndicatorDots[position].setImageResource(R.drawable.selecteditem_dot);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        String[] imageIds = exercise.images.split(",");
        photoViewPager.setOffscreenPageLimit(imageIds.length - 1);

        adapter = new ViewPagerAdapter(this, imageIds);
        photoViewPager.setAdapter(adapter);
        pageIndicator = (LinearLayout)findViewById(R.id.viewPagerCountDots);
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

    private void startRest() {
        m_isRest = true;
        m_CurRestTime = m_RestTime;

        updatePanel.setTimeCount(m_CurRestTime, "REST");
        m_RestHandler.postDelayed(m_RestRunnable, REST_DELAY);

        stopPlay();
    }

    private void countDownRest() {
        m_CurRestTime--;
        updatePanel.setYPos(m_CurRestTime);
    }

    private void stopRest() {
        m_isRest = false;

        m_ExerciseHandler.removeCallbacks(m_ExerciseRunnable);
        m_RestHandler.removeCallbacks(m_RestRunnable);

        m_CurSet--;
        if (m_CurSet == 0) {
            stopPlay();
            updatePanel.ExitDraw();
            m_CurSet = m_Set;
        } else {
            //repeat
            startExercise();
        }
    }

    private void startExercise() {
        m_CurExerciseTime = m_ExerciseTime;
        updatePanel.setTimeCount(m_CurExerciseTime, "EXERCISE");
        updatePanel.reset();
        m_ExerciseHandler.postDelayed(m_ExerciseRunnable, EXERCISE_DELAY);

        startPlay();
    }

    private void countDownExercise() {
        m_CurExerciseTime--;
        updatePanel.setYPos(m_CurExerciseTime);
    }

    private void stopExercise() {
        m_ExerciseHandler.removeCallbacks(m_ExerciseRunnable);
        startRest();
    }

    private void updatePage() {
        photoViewPager.setCurrentItem(m_CurrentPageNum, false);
        m_CurrentPageNum++;
        if(m_CurrentPageNum > pageCount)
            m_CurrentPageNum = 0;
    }

    private void setUiPageViewController() {

        pageCount = adapter.getCount();
        pageIndicatorDots = new ImageView[pageCount];

        for (int i = 0; i < pageCount; i++) {
            pageIndicatorDots[i] = new ImageView(this);
            pageIndicatorDots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(8, 0, 8, 0);

            pageIndicator.addView(pageIndicatorDots[i], params);
        }

        pageIndicatorDots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    @Override
    public void onClick(View v) {

        int nResId  = v.getId();

        switch (nResId) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.img_play:
                onClickPlay();
                break;
            case R.id.img_exercise:
                onClickAlarm();
                break;
            case R.id.img_favourite:
                onClickFavourite();
                break;
            case R.id.img_plus:
                onClickPlus();
                break;
        }

    }

    private void onClickPlus() {
        Intent intent = new Intent(this, PlusActivity.class)
                .putExtra("exercise", exercise);
        startActivity(intent);
    }

    private void onClickFavourite() {
        if(m_isFavourite) {
            m_isFavourite = false;
            ivFavourite.setImageResource(R.drawable.ic_unvote);
        } else {
            m_isFavourite = true;
            ivFavourite.setImageResource(R.drawable.ic_vote);
        }

        DBEngine dbEngine = new DBEngine(this);
        dbEngine.updateFavourite(exercise.exercise_id, m_isFavourite);
    }

    private void onClickAlarm() {
        if(m_isAlarm) {
            // play
            m_isAlarm = false;
            ivExercise.setImageResource(R.drawable.ic_stop_exercise);
            updatePanel.setVisibility(View.GONE);
            stopRest();
        } else {
            // stop
            m_isAlarm = true;
            ivExercise.setImageResource(R.drawable.ic_start_exercise);
            updatePanel.setVisibility(View.VISIBLE);

            m_CurSet = m_Set;
            m_CurExerciseTime = m_ExerciseTime;
            m_CurRestTime = m_RestTime;

            updatePanel.init(m_ExerciseTime);
            updatePanel.reset();
            stopPlay();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (exercise.like != m_isFavourite) {
            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    private  void startPlay() {
        m_isPlay = true;
        m_PlayHandler.postDelayed(m_PlayRunnable, PLAY_DELAY);
        ivPlay.setImageResource(R.drawable.ic_pause);
    }
    private void stopPlay() {
        m_isPlay = false;
        m_PlayHandler.removeCallbacks(m_PlayRunnable);
        ivPlay.setImageResource(R.drawable.ic_play);
    }

    private void onClickPlay() {
        if(m_isPlay) {
            if(m_isAlarm)
                return;
            else
                stopPlay();
        } else {
            if(m_isRest) return;;

            if(m_isAlarm)
                startExercise();
            else
                startPlay();
        }
    }

    public class ViewPagerAdapter extends PagerAdapter {

        private Context mContext;
        private String[] mImageIds = null;

        public ViewPagerAdapter(Context context, String[] imageIds) {
            mContext = context;
            mImageIds = imageIds;
        }

        @Override
        public int getCount() {
            return mImageIds.length;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View itemView = LayoutInflater.from(mContext).inflate(R.layout.exercise_photo, container, false);
            ImageView imageView = (ImageView)itemView.findViewById(R.id.img_photo);

            if(mImageIds.length > position)
                ImageLoader.LoadImage(mContext, imageView, mImageIds[position]);

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
    }
}
