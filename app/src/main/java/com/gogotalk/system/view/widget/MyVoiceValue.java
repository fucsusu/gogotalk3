package com.gogotalk.system.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;

/**
 * Created by fucc
 * Date: 2019-08-08 15:11
 */
public class MyVoiceValue extends View {
    private Paint paint;
    private int radius;
    public int radius2;
    public int radius3;
    public int voiceNum = 0;

    public MyVoiceValue(Context context) {
        super(context);
    }

    public MyVoiceValue(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyVoiceValue(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(20);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
        radius = getHeight() / 4 + 10;
        radius2 = (getHeight() / 2 - 50) / 3 + radius;
        radius3 = radius2 + (getHeight() / 2 - 50) / 3;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (voiceNum) {
            case 3:
                canvas.drawArc(getWidth() / 2 - radius3, getHeight() / 4 * 3 - radius3, getWidth() / 2 + radius3, getHeight() / 4 * 3 + radius3, -40, -100, false, paint);
            case 2:
                canvas.drawArc(getWidth() / 2 - radius2, getHeight() / 4 * 3 - radius2, getWidth() / 2 + radius2, getHeight() / 4 * 3 + radius2, -40, -100, false, paint);
            case 1:
                canvas.drawArc(getWidth() / 2 - radius, getHeight() / 4 * 3 - radius, getWidth() / 2 + radius, getHeight() / 4 * 3 + radius, -40, -100, false, paint);
                break;
        }
    }

    public void setVoiceNum(int voiceNum) {
        Log.e("TAG", "setVoiceNum: " + voiceNum);
        if (voiceNum < 5) {
            this.voiceNum = 0;
        } else if (voiceNum < 15) {
            this.voiceNum = 1;
        } else if (voiceNum < 30) {
            this.voiceNum = 2;
        } else {
            this.voiceNum = 3;
        }
        postInvalidate();
    }
}
