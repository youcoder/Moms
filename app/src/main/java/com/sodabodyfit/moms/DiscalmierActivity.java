package com.sodabodyfit.moms;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class DiscalmierActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_discalmier);
        init();
    }

    private void init()
    {
        Button btnContinue = (Button)findViewById(R.id.btn_continue);
        btnContinue.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Aleo-Bold.otf"));
        btnContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int nResId = v.getId();
        switch (nResId)
        {
            case R.id.btn_continue:
                onContinue();
                break;
        }
    }

    private void onContinue()
    {
        finish();
    }
}
