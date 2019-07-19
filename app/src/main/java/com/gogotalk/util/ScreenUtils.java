package com.gogotalk.util;

import android.content.Context;

import com.orhanobut.logger.Logger;

/**
 * Copyright (C)
 * <p>
 * FileName: ScreenUtils
 * <p>
 * Author: 赵小钧
 * <p>
 * Date: 2019\6\13 0013 18:45
 */
public class ScreenUtils {
    /**
     * 获取屏幕高度(px)
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取屏幕宽度(px)
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        Logger.i(scale+"++++++++++++++++++++++++++++++++");
        return (int) (dpValue * scale + 0.5f);
    }

}
