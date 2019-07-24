package com.gogotalk.system.view.widget;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Copyright (C)
 * <p>
 * FileName: SpaceItemDecoration
 * <p>
 * Author: 赵小钧
 * <p>
 * Date: 2019\6\12 0012 19:05
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int leftRight;
    private int topBottom;

    //leftRight为横向间的距离 topBottom为纵向间距离
    public SpaceItemDecoration(int leftRight, int topBottom) {
        this.leftRight = leftRight;
        this.topBottom = topBottom;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        //竖直方向的
        if (layoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
            //最后一项需要 bottom
            if (parent.getChildAdapterPosition(view) == layoutManager.getItemCount() - 1) {
                outRect.bottom = topBottom;
            }
            outRect.top = topBottom;
            outRect.left = leftRight;
            outRect.right = leftRight;
        } else {
            //最后一项需要right
            if (parent.getChildAdapterPosition(view) == layoutManager.getItemCount() - 1) {
                outRect.right = leftRight;
            }
            outRect.top = topBottom;
            if (parent.getChildAdapterPosition(view) != 0) {
                outRect.left = leftRight;
            }
            outRect.bottom = topBottom;
        }
    }
}
