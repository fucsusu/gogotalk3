package com.gogotalk.system.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.gogotalk.system.BuildConfig;
import com.gogotalk.system.di.components.DaggerNetComponent;
import com.gogotalk.system.di.components.NetComponent;
import com.gogotalk.system.di.modules.NetModule;
import com.gogotalk.system.util.AppUtils;
import com.gogotalk.system.util.SPUtils;
import com.gogotalk.system.util.SystemDownLoadFileImpl;
import com.gogotalk.system.zego.ZGBaseHelper;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.Logger;
import com.pgyersdk.Pgyer;
import com.pgyersdk.PgyerActivityManager;
import com.pgyersdk.crash.PgyCrashManager;
import com.pgyersdk.crash.PgyerCrashObservable;
import com.pgyersdk.crash.PgyerObserver;
import com.tencent.smtt.sdk.QbSdk;

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
        initPgyCrash();
        initNet();
        initLogger();
        initJGSDK();
        //initTenX5();
        SPUtils.initSpUtil(this, AppUtils.getAppName(this));
    }

    private void initTenX5() {
        //非wifi情况下，主动下载x5内核
        QbSdk.setDownloadWithoutWifi(true);
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.e("TAG", "onViewInitFinished: " + arg0);
            }

            @Override
            public void onCoreInitFinished() {

            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
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

    private void initPgyCrash() {
        PgyCrashManager.register();
        PgyerCrashObservable.get().attach(new PgyerObserver() {
            @Override
            public void receivedCrash(Thread thread, Throwable throwable) {

            }
        });
        PgyerActivityManager.set(this);
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        Pgyer.setAppId("9a55702201cdb5aafc2850204e7d4795");
    }
}
