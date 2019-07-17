package com.gogotalk.util;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import java.lang.ref.WeakReference;

public class AppUtils {
    /**
     * 隐藏虚拟按键，并且全屏
     */
    public static void hideVirtualKeyView(Activity activity) {
        WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = activityWeakReference.get().getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = activityWeakReference.get().getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
