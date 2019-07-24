package com.gogotalk.system.view.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.GridLayoutManager;

public class MyGridLayoutManager extends GridLayoutManager {

    private boolean canScroll = false;

    public MyGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MyGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public MyGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    /**
     * 垂直方向
     * @return
     */
    @Override
    public boolean canScrollVertically() {
        return canScroll && super.canScrollVertically();
    }

    /**
     * 水平方向
     * @return
     */
    @Override
    public boolean canScrollHorizontally() {
        return super.canScrollHorizontally();
    }

    /**
     * 设置是否可以滑动
     * @param canScroll
     */
    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }
}