package com.gogotalk.system.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by fucc
 * Date: 2019-07-26 19:34
 */
public class MikeRateView extends View {
    private Paint mPaint;
    private int mStrokeWidth = 20;
    private Path mPath;
    private int width;
    private int height;
    private PathMeasure pathMeasure;
    private Path dstPath;
    private float detPathMeasure;
    private CountDownTimer countDownTimer;
    private int speed;

    {
        mStrokeWidth = 8 * (getContext().getResources().getDisplayMetrics().densityDpi / 160);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.parseColor("#FF6601"));
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);

        pathMeasure = new PathMeasure();
        mPath = new Path();

        dstPath = new Path();
    }

    public MikeRateView(Context context) {
        super(context);
    }

    public MikeRateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MikeRateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = right - left;
        height = bottom - top;
        mPath.moveTo(mStrokeWidth / 2, height / 2 - mStrokeWidth);
        mPath.cubicTo(mStrokeWidth / 2 + 5, -mStrokeWidth / 4 * 2, width - mStrokeWidth / 2 - 5, -mStrokeWidth / 4 * 2, width - mStrokeWidth / 2, height / 2 - mStrokeWidth);
        pathMeasure.setPath(mPath, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        dstPath.reset();
        pathMeasure.getSegment(0, detPathMeasure * speed, dstPath, true);
        canvas.drawPath(dstPath, mPaint);
    }

    public void start(final int times) {
        detPathMeasure = pathMeasure.getLength() / times;
        speed = 0;
        postInvalidate();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        countDownTimer = new CountDownTimer((times + 1) * 1000, 990) {
            @Override
            public void onTick(long millisUntilFinished) {
                speed++;
                if (speed > times) {
                    speed = times;
                }
                postInvalidate();
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }
}
