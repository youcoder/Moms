package com.sodabodyfit.moms;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sodabodyfit.moms.Adapter.MyPagerAdapter;
import com.sodabodyfit.moms.Common.CardInfo;
import com.sodabodyfit.moms.Common.CarouselEffectTransformer;
import com.sodabodyfit.moms.Models.Workout;
import com.sodabodyfit.moms.Provider.DBEngine;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static MainActivity _instance = null;
    private DrawerLayout dlDrawer;
    private RadioButton rdoEnglish;
    private RadioButton rdoDutch;
    private ViewPager viewpager;

    private int m_nSelLangId = 1;   //default = english

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _instance = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dlDrawer = (DrawerLayout)findViewById(R.id.main_drawer);
        TextView tvAccount = (TextView)findViewById(R.id.tv_account);
        tvAccount.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Aleo-Bold.otf"));

        LinearLayout changePassword = (LinearLayout)findViewById(R.id.ll_change_password);
        changePassword.setOnClickListener(this);

        LinearLayout disclamier = (LinearLayout)findViewById(R.id.ll_disclaimer);
        disclamier.setOnClickListener(this);

        LinearLayout logout = (LinearLayout)findViewById(R.id.ll_logout);
        logout.setOnClickListener(this);

        rdoEnglish = (RadioButton)findViewById(R.id.rd_english);
        rdoEnglish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                DBEngine dbEngine = new DBEngine(v.getContext());
                dbEngine.setLanguage(1);    // english
            }
        });

        rdoDutch = (RadioButton)findViewById(R.id.rd_dutch);
        rdoDutch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                DBEngine dbEngine = new DBEngine(v.getContext());
                dbEngine.setLanguage(2);    // dutch
            }
        });

        viewpager = (ViewPager) findViewById(R.id.viewpager);

        viewpager.setClipChildren(false);
        viewpager.setPageMargin(5);
        viewpager.setOffscreenPageLimit(1);
        viewpager.setPageTransformer(false, new CarouselEffectTransformer(this)); // Set transformer

        ArrayList<CardInfo> cardInfos = new ArrayList<CardInfo>();

        int[] cardPhotoId = { R.drawable.mom_0, R.drawable.mom_1, R.drawable.mom_2,
                R.drawable.mom_3, R.drawable.mom_4, R.drawable.mom_5,
                R.drawable.mom_6, R.drawable.mom_7};

        int[] detailPhotoId = { R.drawable.mom_d0, R.drawable.mom_d1, R.drawable.mom_d2,
                R.drawable.mom_d3, R.drawable.mom_d4, R.drawable.mom_d5,
                R.drawable.mom_d6, R.drawable.mom_d7};

        for(int i=0; i<8; i++) {
            DBEngine dbEngine = new DBEngine(this);
            ArrayList<Workout> workouts = dbEngine.getWorkoutList(i);
            cardInfos.add(new CardInfo(cardPhotoId[i], detailPhotoId[i], workouts));
        }

        MyPagerAdapter adapter = new MyPagerAdapter(this, cardInfos);
        viewpager.setAdapter(adapter);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        DBEngine dbEngine = new DBEngine(this);
        int nSelLangType = dbEngine.getSelLanguageId();

        if(nSelLangType == 1)
            rdoEnglish.setChecked(true);
        else
            rdoDutch.setChecked(true);
    }

    @Override
    public void onClick(View v) {

        int nResId = v.getId();
        switch (nResId) {

            case R.id.ll_disclaimer:
                dlDrawer.closeDrawers();
                acceptDialog();
                break;
            case R.id.ll_change_password:
                dlDrawer.closeDrawers();
                changePassword();
                break;
            case R.id.ll_logout:
                dlDrawer.closeDrawers();
                logout();
                break;
        }
    }

    private void acceptDialog() {

        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .customView(R.layout.dialog_disclaimer, true)
                .cancelable(false)
                .build();

        Button btContinue = (Button)dialog.getCustomView().findViewById(R.id.btn_continue);
        btContinue.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.buttonEnable));
        btContinue.setEnabled(true);
        btContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        CheckBox cbAccept = (CheckBox)dialog.getCustomView().findViewById(R.id.chk_accept);
        cbAccept.setChecked(true);
        cbAccept.setEnabled(false);
        dialog.show();
    }

    private void logout() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void changePassword() {
        Intent intent = new Intent(MainActivity.this, ChangePasswordActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_menu) {
            if (dlDrawer.isDrawerOpen(Gravity.RIGHT)) {
                dlDrawer.closeDrawer(Gravity.RIGHT);
            } else {
                dlDrawer.openDrawer(Gravity.RIGHT);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
