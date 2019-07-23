package com.gogotalk.app;

import android.app.Application;
import android.content.Context;

import androidx.annotation.Nullable;

import com.gogotalk.BuildConfig;
import com.gogotalk.di.components.DaggerNetComponent;
import com.gogotalk.di.components.NetComponent;
import com.gogotalk.di.modules.NetModule;
import com.gogotalk.util.AppUtils;
import com.gogotalk.util.SPUtils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.Logger;

public class AiRoomApplication extends Application {
    private static AiRoomApplication instance;

    public static AiRoomApplication get(Context context){
        return (AiRoomApplication)context.getApplicationContext();
    }
    private NetComponent netComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initNet();
        initLogger();
        SPUtils.initSpUtil(this, AppUtils.getAppName(this));
    }

    /**
     * 初始化logger
     */
    private void initLogger(){
        Logger.addLogAdapter(new AndroidLogAdapter(){
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
    private void initNet(){
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
}
