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

    ListView m_ListView = null;
    ArrayList<WorkoutInfo> m_WorkoutInfoList = null;

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

        m_ListView = (ListView)findViewById(R.id.workout_list);
        //for debug...
        m_WorkoutInfoList = new ArrayList<WorkoutInfo>();
        m_WorkoutInfoList.add(new WorkoutInfo(R.drawable.boxing, "workout 1"));
        m_WorkoutInfoList.add(new WorkoutInfo(R.drawable.boxing, "workout 2"));
        m_WorkoutInfoList.add(new WorkoutInfo(R.drawable.boxing, "workout 3"));
        m_WorkoutInfoList.add(new WorkoutInfo(R.drawable.boxing, "workout 4"));
        m_WorkoutInfoList.add(new WorkoutInfo(R.drawable.boxing, "workout 5"));
        m_WorkoutInfoList.add(new WorkoutInfo(R.drawable.boxing, "workout 6"));
        m_WorkoutInfoList.add(new WorkoutInfo(R.drawable.boxing, "workout 7"));
        m_WorkoutInfoList.add(new WorkoutInfo(R.drawable.boxing, "workout 8"));
        m_WorkoutInfoList.add(new WorkoutInfo(R.drawable.boxing, "workout 9"));

        WorkoutListAdapter adapter = new WorkoutListAdapter(this, m_WorkoutInfoList);
        m_ListView.setAdapter(adapter);
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
        private ArrayList<WorkoutInfo> m_WorkoutInfoList = null;

        public WorkoutListAdapter(Context context, ArrayList<WorkoutInfo> workoutInfoList)
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

            if(convertView == null)
            {
                convertView = LayoutInflater.from(m_Context).inflate(R.layout.workout_item, parent, false);
            }

            TextView workoutName = (TextView)convertView.findViewById(R.id.tv_workout_name);
            workoutName.setText(m_WorkoutInfoList.get(position)._WorkoutName);
            ImageView workoutImage = (ImageView)convertView.findViewById(R.id.iv_workout_picture);
            workoutImage.setImageResource(m_WorkoutInfoList.get(position)._ImgResId);

            return convertView;
        }
    }

}
