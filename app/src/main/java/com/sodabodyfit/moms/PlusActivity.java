package com.sodabodyfit.moms;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sodabodyfit.moms.Common.ImageLoader;
import com.sodabodyfit.moms.Models.Workout;
import com.sodabodyfit.moms.Provider.DBEngine;

import java.util.ArrayList;

public class PlusActivity extends Activity implements View.OnClickListener {

    public class WorkoutInfo{
        public int _ImgResId = 0;
        public String _WorkoutName = "";

        public WorkoutInfo(int resId, String workoutName){
            _ImgResId = resId;
            _WorkoutName = workoutName;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_plus);

        init();
    }

    private void init()
    {
        TextView tvCancel = (TextView)findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(this);

        LinearLayout llPlus = (LinearLayout)findViewById(R.id.new_workout);
        llPlus.setOnClickListener(this);

        ListView listView = (ListView)findViewById(R.id.workout_list);

        DBEngine dbEngine = new DBEngine(this);
        ArrayList<Workout> lstWorkout = dbEngine.getWorkoutList(1);

        WorkoutListAdapter adapter = new WorkoutListAdapter(this, lstWorkout);
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
            case R.id.new_workout:
                onClickPlus();
                break;
        }
    }

    private void onClickPlus()
    {

    }

    private void onClickCancel()
    {
        onBackPressed();
    }

    public class WorkoutListAdapter extends BaseAdapter{

        private Context m_Context = null;
        private ArrayList<Workout> m_WorkoutInfoList = null;

        public WorkoutListAdapter(Context context, ArrayList<Workout> workoutInfoList)
        {
            m_Context = context;
            m_WorkoutInfoList = workoutInfoList;
        }

        @Override
        public int getCount() {
            return m_WorkoutInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return m_WorkoutInfoList.get(position);
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
            workoutName.setText(m_WorkoutInfoList.get(position).title);
            ImageView workoutImage = (ImageView)convertView.findViewById(R.id.iv_workout_picture);
            ImageLoader.LoadImage(m_Context, workoutImage, m_WorkoutInfoList.get(position).image);

            return convertView;
        }
    }

}
