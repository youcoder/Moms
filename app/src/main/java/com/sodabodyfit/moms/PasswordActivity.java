package com.sodabodyfit.moms;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class PasswordActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_password);
        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
    }

    private void init()
    {
        TextView btnDone = (TextView)findViewById(R.id.tv_done);
        btnDone.setOnClickListener(this);

        TextView btnSave = (TextView)findViewById(R.id.tv_save);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int nResId = v.getId();
        switch (nResId)
        {
            case R.id.tv_done:
                OnDone();
                break;
            case R.id.tv_save:
                OnSave();
                break;
        }
    }

    private void OnDone()
    {
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
    }

    private void OnSave()
    {
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
    }


}
