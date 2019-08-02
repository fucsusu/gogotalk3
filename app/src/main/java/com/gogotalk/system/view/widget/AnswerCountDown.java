package com.gogotalk.system.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by fucc
 * Date: 2019-07-27 11:16
 */
public class AnswerCountDown extends View {
    //半圆半径
    private int radius;
    //进度条的宽度
    private int progressWith;
    private Paint mPaint;
    private TextPaint mTxtPaint;

    private int time = 11;
    private CountDownTimer countDownTimer;
    private AnswerCountDown answerCountDown = this;
    public Disposable mDisposable;

    public AnswerCountDown(Context context) {
        super(context);
    }

    public AnswerCountDown(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnswerCountDown(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        mPaint = new Paint();
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(Color.parseColor("#FFA100"));
        mTxtPaint = new TextPaint();
        mTxtPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int size = getHeight() / 5;
        radius = (size) * 3;
        progressWith = size * 2;
        mTxtPaint.setTextSize(progressWith * 3 / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画进度条背景
        mPaint.setStrokeWidth(progressWith);
        canvas.drawLine(progressWith / 2 + (10 - time) * getWidth() / 20, progressWith / 2, getWidth() - progressWith / 2 - (10 - time) * getWidth() / 20, progressWith / 2, mPaint);
        mPaint.setStrokeWidth(1);
        canvas.drawCircle(getWidth() / 2, progressWith, radius, mPaint);

        //画数字
        float textL = mTxtPaint.measureText(time + "");
        if (time <= 5) {
            mTxtPaint.setColor(Color.RED);
        } else {
            mTxtPaint.setColor(Color.WHITE);
        }
        canvas.drawText(time + "", getWidth() / 2 - textL / 2, getHeight() / 2 + progressWith - 10, mTxtPaint);
    }

    public void startCountDown() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
            mDisposable = null;
        }
        Observable.interval(0, 1, TimeUnit.SECONDS).
                take(11).observeOn(AndroidSchedulers.mainThread())//ui线程中进行控件更新
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mDisposable = disposable;
                    }
                })
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long num) {
                        time = (int) (10 - num);
                        postInvalidate();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        answerCountDown.setVisibility(GONE);
                    }
                });
        time = 10;
        answerCountDown.setVisibility(VISIBLE);
    }
}
