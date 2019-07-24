package com.gogotalk.system.app;

import android.app.Application;
import android.content.Context;

import androidx.annotation.Nullable;

import com.gogotalk.system.BuildConfig;
import com.gogotalk.system.di.components.DaggerNetComponent;
import com.gogotalk.system.di.components.NetComponent;
import com.gogotalk.system.di.modules.NetModule;
import com.gogotalk.system.util.AppUtils;
import com.gogotalk.system.util.SPUtils;
import com.gogotalk.system.zego.ZGBaseHelper;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.Logger;

public class AiRoomApplication extends Application {
    private static AiRoomApplication instance;

    public static AiRoomApplication get(Context context) {
        return (AiRoomApplication) context.getApplicationContext();
    }

    private NetComponent netComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initNet();
        initLogger();
        initJGSDK();
        SPUtils.initSpUtil(this, AppUtils.getAppName(this));
    }

    /**
     * 初始化logger
     */
    private void initLogger() {
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return BuildConfig.DEBUG;
            }
        });
        Logger.addLogAdapter(new DiskLogAdapter());
    }

    /**
     * 网络请求配置
     */
    private void initNet() {
        netComponent = DaggerNetComponent.builder()
                .netModule(new NetModule())
                .build();
    }

    public NetComponent getNetComponent() {
        return netComponent;
    }

    public static AiRoomApplication getInstance() {
        return instance;
    }

    private void initJGSDK() {
        ZGBaseHelper.sharedInstance().setSDKContextEx(null, null, 10 * 1024 * 1024, this);
    }
}
