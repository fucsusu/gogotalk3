package com.gogotalk.system.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;


import java.util.Map;

/**
 * Created by fucc
 * Date: 2019-08-07 10:29
 */
public class MyWebView extends WebView {
    public int height;
    public int width;

    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MyWebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public MyWebView(Context context, AttributeSet attributeSet, int i, boolean b) {
        super(context, attributeSet, i, b);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        height = MeasureSpec.getSize(heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(height * 16 / 9, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        offsetLeftAndRight(-(height / 9 * 16 - width) / 2);
    }
}
