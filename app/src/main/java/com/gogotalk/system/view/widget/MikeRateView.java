package com.gogotalk.system.view.widget;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by fucc
 * Date: 2019-07-26 19:34
 */
public class MikeRateView extends View {
    public int width;
    public int height;
    public int penWidth = 20;
    Paint paint = new Paint();
    private int angle = 0;

    public MikeRateView(Context context) {
        super(context);
    }

    public MikeRateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MikeRateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CountDownTimer countDownTimer;

    {
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#FF6601"));
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(penWidth);
        paint.setAntiAlias(true);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawArc(penWidth / 2, penWidth / 2, width - penWidth / 2, width - penWidth / 2, -180, angle, false, paint);
    }


    public void start(int time) {
        countDownTimer = new CountDownTimer(time * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                angle = (int) (time - (millisUntilFinished / 1000)) * 180 / time;
                postInvalidate();
            }

            @Override
            public void onFinish() {
            }
        };
        countDownTimer.start();
    }
}
