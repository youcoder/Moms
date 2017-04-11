package com.sodabodyfit.moms;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.sodabodyfit.moms.Common.DrawView;

public class TestActivity extends Activity implements View.OnClickListener{

    DrawView drawView;
    Handler m_RedrawHandler = null;
    Runnable m_RedrawRunnable = null;
    int m_Time = 10;
    public static final int DELAY = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        init();
    }

    private void init()
    {
        drawView = (DrawView)findViewById(R.id.draw_panel);
        ImageView btnRedraw = (ImageView)findViewById(R.id.btn_redraw);
        btnRedraw.setOnClickListener(this);


        m_RedrawRunnable = new Runnable() {
            @Override
            public void run() {
                m_RedrawHandler.postDelayed(this, DELAY);
                drawView.setYPos(m_Time);
                m_Time--;
                if(m_Time < 0)
                    stopDraw();
            }
        };

        m_RedrawHandler = new Handler();
    }

    @Override
    public void  onClick(View view) {

        int nStep = 60;
        m_RedrawHandler.postDelayed(m_RedrawRunnable, DELAY);
        drawView.setYPos(m_Time);
        m_Time--;
    }

    private void stopDraw()
    {
        //drawView.initDraw();
        m_RedrawHandler.removeCallbacks(m_RedrawRunnable);
    }
}
