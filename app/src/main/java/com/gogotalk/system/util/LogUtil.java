package com.gogotalk.system.util;

import android.util.Log;

/**
 * Created by fucc
 * Date: 2019-08-11 16:36
 */
public class LogUtil {
    private static boolean isShow = true;

    public static void e(Object... ags) {
        if (isShow) {
            StringBuilder result = new StringBuilder();
            for (int i = 1; i < ags.length; i++) {
                result.append(ags[i] + "||");
            }
            Log.e(ags[0].toString(), "e: " + result);
        }
    }
}
