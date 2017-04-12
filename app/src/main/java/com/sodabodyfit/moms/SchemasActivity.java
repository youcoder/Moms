package com.sodabodyfit.moms;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sodabodyfit.moms.Adapter.SchemasAdapter;
import com.sodabodyfit.moms.Common.DividerItemDecoration;
import com.sodabodyfit.moms.Common.ImageLoader;
import com.sodabodyfit.moms.Models.Exercise;
import com.sodabodyfit.moms.Models.Workout;
import com.sodabodyfit.moms.Provider.DBEngine;

import java.util.ArrayList;

public class SchemasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schemas);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        Typeface m_TypeFace = Typeface.createFromAsset(getAssets(), "fonts/Aleo-Bold.otf");
        
        Intent intent = getIntent();
        int workoutId = intent.getIntExtra("workout_id", -1);
        ArrayList<Exercise> lstExercise = intent.getParcelableArrayListExtra("exercise");

        DBEngine dbEngine = new DBEngine(this);
        Workout workout = dbEngine.getWorkoutInfo(workoutId);

        ImageView imageView = (ImageView) findViewById(R.id.img_category);
        ImageLoader.LoadImage(this, imageView, workout.image);

        LinearLayout llSchemasInfo = (LinearLayout) findViewById(R.id.ll_schemas_info);
        TextView tvSchemasInfo = (TextView) findViewById(R.id.txt_schemas_info);
        if(workout.infoDisplayed) {
            tvSchemasInfo.setText(workout.info);
            llSchemasInfo.setVisibility(View.VISIBLE);
        }

        TextView tv_Schemas = (TextView)findViewById(R.id.txt_schemas_label);
        tv_Schemas.setTypeface(m_TypeFace);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(layoutManager);
        recycler.addItemDecoration(new DividerItemDecoration(this));

        SchemasAdapter adapter = new SchemasAdapter(SchemasActivity.this, lstExercise);
        recycler.setAdapter(adapter);
    }

//    public class SchemaAdapter extends BaseAdapter
//    {
//        Context m_context = null;
//        ArrayList<SchemasInfo> m_SchemaList = null;
//
//        public SchemaAdapter(Context context, ArrayList<SchemasInfo> schemaList)
//        {
//            m_context = context;
//            m_SchemaList = schemaList;
//        }
//
//        @Override
//        public int getCount() {
//            return m_SchemaList.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return m_SchemaList.get(position);
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
//            {
//                convertView = LayoutInflater.from(m_context).inflate(R.layout.list_schemas, parent, false);
//                TextView tv_set_label_schema = (TextView)convertView.findViewById(R.id.tv_set_label_schema);
//                tv_set_label_schema.setTypeface(m_TypeFace);
//
//                TextView tv_reps_label_schema = (TextView)convertView.findViewById(R.id.tv_reps_label_schema);
//                tv_reps_label_schema.setTypeface(m_TypeFace);
//
//                TextView tv_time_label_schema = (TextView)convertView.findViewById(R.id.tv_time_label_schema);
//                tv_time_label_schema.setTypeface(m_TypeFace);
//
//                TextView tv_rest_label_schema = (TextView)convertView.findViewById(R.id.tv_rest_label_schema);
//                tv_rest_label_schema.setTypeface(m_TypeFace);
//
//                TextView tv_kg_label_schema = (TextView)convertView.findViewById(R.id.tv_kg_label_schema);
//                tv_kg_label_schema.setTypeface(m_TypeFace);
//            }
//
//            TextView tv_Title = (TextView)convertView.findViewById(R.id.schema_content);
//            tv_Title.setText(m_SchemaList.get(position)._title);
//            tv_Title.setTypeface(m_TypeFace);
//            TextView tv_set = (TextView)convertView.findViewById(R.id.tv_set_schema);
//            tv_set.setText(String.valueOf(m_SchemaList.get(position)._Set));
//            tv_set.setTypeface(m_TypeFace);
//            TextView tv_reps = (TextView)convertView.findViewById(R.id.tv_reps_schema);
//            tv_reps.setText(String.valueOf(m_SchemaList.get(position)._Reps));
//            tv_reps.setTypeface(m_TypeFace);
//            TextView tv_time = (TextView)convertView.findViewById(R.id.tv_time_schema);
//            tv_time.setText(String.valueOf(m_SchemaList.get(position)._Time) + " sec");
//            tv_time.setTypeface(m_TypeFace);
//            TextView tv_rest = (TextView)convertView.findViewById(R.id.tv_rest_schema);
//            tv_rest.setText(String.valueOf(m_SchemaList.get(position)._Rest) + " sec");
//            tv_rest.setTypeface(m_TypeFace);
//            TextView tv_kg = (TextView)convertView.findViewById(R.id.tv_kg_schema);
//            tv_kg.setText(String.valueOf(m_SchemaList.get(position)._Kg));
//            tv_kg.setTypeface(m_TypeFace);
//
//            return convertView;
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
            SchemasActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
