package com.gogotalk.util;

import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import java.lang.ref.WeakReference;

public class AppUtils {
    /**
     *第一种：始终隐藏，触摸屏幕时也不出现
     *解决办法：同时设置以下两个参数
     *View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
     *View.SYSTEM_UI_FLAG_IMMERSIVE
     *在需要隐藏虚拟键Navigation Bar的Activity的onCreate方法中
     *添加如下代码：
     * 隐藏pad底部虚拟键
     */
    public static void hideVirtualKeyViewNoShow(Activity activity){
        WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);
        Window window = activityWeakReference.get().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE;
        window.setAttributes(params);
    }



    /**
     *不要直接copy啊，这是两种形式，都放在一起了。
     *第二种：隐藏了，但触摸屏幕时出现
     *解决办法：设置以下一个参数
     *View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
     *在需要隐藏虚拟键Navigation Bar的Activity的onCreate方法中
     *添加如下代码：
     *隐藏pad底部虚拟键
     */
    public static void hideVirtualKeyViewYesShow(Activity activity){
        WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);
        Window window = activityWeakReference.get().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        window.setAttributes(params);
    }

}
