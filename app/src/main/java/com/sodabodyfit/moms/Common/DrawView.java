package com.sodabodyfit.moms.Common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.sodabodyfit.moms.R;

/**
 * Created by MokRan on 2017-02-12.
 */

public class DrawView extends View {

    private Paint paint = new Paint();
    private int m_YPos = 0;
    private int m_Height = 0;
    private int m_TimeCount = 0;
    private String m_strCountDisplay = "";
    private String m_strTypeDisplay = "";
    private int m_NumberTextSize = 300;
    private int m_TypeTextSize = 40;
    private Context m_Context = null;

    public DrawView(Context context) {
        super(context);
        m_Context = context;
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_Context = context;
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        m_Context = context;
    }

    public void init(int timeCount)
    {
        m_strCountDisplay = String.valueOf(timeCount);
        m_YPos = 1024;
        invalidate();
    }
    public void reset()
    {
        m_NumberTextSize = 300;
    }
    public void setTimeCount(int timeCount, String type)
    {
        m_TimeCount = timeCount;
        m_strTypeDisplay = type;
        m_YPos = 0;
        m_strCountDisplay = String.valueOf(timeCount);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), paint);

        paint.setStrokeWidth(0);
        paint.setColor(getResources().getColor(R.color.colorDotSelect));
        m_Height = this.getHeight();
        canvas.drawRect(0, m_YPos, this.getWidth(), this.getHeight(), paint);

        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(m_TypeTextSize);
        paint.setTypeface(Typeface.createFromAsset(m_Context.getAssets(), "fonts/Aleo-Bold.otf"));
        canvas.drawText(m_strTypeDisplay, 100, m_YPos + 60, paint);

        if(m_strCountDisplay != null)
        {
            paint.setColor(getResources().getColor(R.color.colorNumber));
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(m_NumberTextSize);
            paint.setTypeface(Typeface.createFromAsset(m_Context.getAssets(), "fonts/Aleo-Bold.otf"));
            canvas.drawText(m_strCountDisplay, this.getWidth()/2, m_Height/2, paint);
        }
    }

    public void setYPos(int number) {

        if(number < 0)
            return;
        int nStep = m_Height/m_TimeCount + 5;
        m_YPos += nStep;
        m_strCountDisplay = String.valueOf(number);
        invalidate();
    }

    public void ExitDraw() {
        m_YPos = 1024;
        m_strCountDisplay = "finished!";
        m_NumberTextSize = 150;
        invalidate();
    }



}
