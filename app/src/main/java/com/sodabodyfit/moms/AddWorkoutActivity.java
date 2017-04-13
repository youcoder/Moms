package com.sodabodyfit.moms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sodabodyfit.moms.Common.ImageLoader;
import com.sodabodyfit.moms.Models.Exercise;
import com.sodabodyfit.moms.Models.Workout;
import com.sodabodyfit.moms.Provider.DBEngine;

import java.util.ArrayList;

public class AddWorkoutActivity extends AppCompatActivity implements View.OnClickListener {

    private Exercise m_Exercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_workout);

        init();
    }

    private void init()
    {
        Intent intent = getIntent();
        m_Exercise = intent.getParcelableExtra("exercise");

        TextView tvCancel = (TextView)findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(this);

        TextView tvOK = (TextView)findViewById(R.id.tv_ok);
        tvOK.setOnClickListener(this);

        LinearLayout llAdd = (LinearLayout)findViewById(R.id.add_workout);
        llAdd.setOnClickListener(this);

        ListView listView = (ListView)findViewById(R.id.exercise_list);

        ArrayList<Exercise> lstExercise = new ArrayList<Exercise>();
        lstExercise.add(m_Exercise);

        ExericiseListAdapter adapter = new ExericiseListAdapter(this, lstExercise);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        int nResId = v.getId();
        switch (nResId)
        {
            case R.id.tv_cancel:
                onClickCancel();
                break;
            case R.id.tv_ok:
                onClickOK();
                break;
        }
    }

    private void onClickOK()
    {
        try {
            EditText etOK = (EditText) findViewById(R.id.et_newtitle);
            String newWorkoutName = etOK.getText().toString();

            DBEngine dbEngine = new DBEngine(this);
            //boolean bExist = dbEngine.IsExistWorkoutName(newWorkoutName);

            //if (bExist) {

                Workout newMyWorkout = new Workout();
                newMyWorkout.category_id = 1;
                newMyWorkout.title = newWorkoutName;
                newMyWorkout.info = "";
                newMyWorkout.infoDisplayed = false;
                newMyWorkout.exercises = String.valueOf(m_Exercise.exercise_id);
                String[] imageIds = m_Exercise.images.split(",");
                if (imageIds.length > 0) newMyWorkout.image = imageIds[0];

                dbEngine.addWorkouts(newMyWorkout);
            //}
        }
        catch (Exception e){
            Log.d("AddWorkout", e.toString());
        }
    }

    private void onClickCancel()
    {
        onBackPressed();
    }

    public class ExericiseListAdapter extends BaseAdapter {

        private Context m_Context = null;
        private ArrayList<Exercise> m_ExerciseList = null;

        public ExericiseListAdapter(Context context, ArrayList<Exercise> exerciseList)
        {
            m_Context = context;
            m_ExerciseList = exerciseList;
        }

        @Override
        public int getCount() {
            return m_ExerciseList.size();
        }

        @Override
        public Object getItem(int position) {
            return m_ExerciseList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                convertView = LayoutInflater.from(m_Context).inflate(R.layout.workout_item, parent, false);
            }

            TextView workoutName = (TextView)convertView.findViewById(R.id.tv_workout_name);
            workoutName.setText(m_ExerciseList.get(position).title);
            ImageView workoutImage = (ImageView)convertView.findViewById(R.id.iv_workout_picture);

            String[] imageIds = m_ExerciseList.get(position).images.split(",");
            if(imageIds.length > 0)
                ImageLoader.LoadImage(m_Context, workoutImage, imageIds[0]);

            return convertView;
        }
    }
}
