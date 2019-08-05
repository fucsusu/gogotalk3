package com.gogotalk.system.util;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.gogotalk.system.R;
import com.gogotalk.system.app.AiRoomApplication;
import com.gogotalk.system.app.AppManager;
import com.gogotalk.system.model.entity.AppInfoDownLoadBean;
import com.gogotalk.system.model.entity.ResponseModel;
import com.gogotalk.system.model.util.Constant;
import com.gogotalk.system.model.util.RxUtil;
import com.gogotalk.system.view.widget.CommonDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by fucc
 * Date: 2019-07-10 17:09
 */
public class AutoUpdateUtil {
    private static AutoUpdateUtil autoUpdateUtil = new AutoUpdateUtil();
    public ProgressDialog dialog;
    public ProgressBar progressBar;
    public CommonDialog commonDialog;
    public int versionNumber;
    public Context activity;
    public TextView progressRate;
    public String filename;

    public static AutoUpdateUtil getInstance() {
        if (autoUpdateUtil != null) {
            return autoUpdateUtil;
        }
        synchronized (AutoUpdateUtil.class) {
            if (autoUpdateUtil == null) {
                autoUpdateUtil = new AutoUpdateUtil();
            }
            return autoUpdateUtil;
        }
    }

    private AutoUpdateUtil() {
    }

    public void checkForUpdates(final Activity activity) {
        this.activity = activity;
        AiRoomApplication.getInstance().getNetComponent().getApiService().getNewAppVersionInfo()
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new Consumer<ResponseModel<AppInfoDownLoadBean>>() {
                    @Override
                    public void accept(ResponseModel<AppInfoDownLoadBean> objectResponseModel) throws Exception {
                        AppInfoDownLoadBean data = objectResponseModel.getData();
                        if (data != null) {
                            versionNumber = data.getVersionNumber();
                            int versionCode = (int) activity.getPackageManager().getPackageInfo("com.gogotalk.system", 0).versionCode;
                            Log.e("TAG", "onResponse: " + versionNumber + "||||" + versionCode);
                            if (versionNumber > versionCode) {
                                showUpdateMessage(data.getUpdateDescribe(), data.getDownloadUrl());
                            }
                        }
                    }
                });
    }

    //显示更新提示
    private void showUpdateMessage(String msg, String fileUrl) {
        CommonDialog.Builder builder = new CommonDialog.Builder(activity);
        builder.setMessage(msg);
        final CommonDialog twoButtonDialog = builder.createTwoButtonDialog(false);
        builder.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                twoButtonDialog.dismiss();
                downLoadApk(fileUrl);
            }
        });
        builder.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                twoButtonDialog.dismiss();
                AppManager.getAppManager().finishAllActivity();
            }
        });
        twoButtonDialog.show();
    }

    //展示下载进度提示
    private void showDownLoadProgress() {
        LayoutInflater lInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflate = lInflater.inflate(R.layout.dialog_download_progress, null);
        progressBar = inflate.findViewById(R.id.progress_download);
        progressRate = inflate.findViewById(R.id.progress_rate);
        CommonDialog.Builder builder = new CommonDialog.Builder(activity, inflate);
        commonDialog = builder.create(false);
        commonDialog.show();
    }


    //下载最新apk
    private void downLoadApk(String fileUrl) {
        if (isApkExistence()) {
            return;
        }
        //开始下载
        AiRoomApplication.getInstance().getNetComponent().getDownLoadFileImpl()
                .setDownLoadingLisener(downLoadingLisener)
                .downLoadFile(activity, fileUrl, Constant.DOWNLOAD_APK + versionNumber + ".apk");
        showDownLoadProgress();
    }

    BaseDownLoadFileImpl.IDownLoadingLisener downLoadingLisener = new BaseDownLoadFileImpl.IDownLoadingLisener() {
        @Override
        public void onDownLoadFinsh() {
            installApk(activity, activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + filename);
        }

        @Override
        public void onDownLoadFail() {
            destory();
        }

        @Override
        public void onDownLoadProgress(int current, int toatal) {
            progressBar.setProgress(current);
            progressBar.setMax(toatal);
            progressRate.setText("下载进度：" + current / (toatal / 100) + "%");
        }
    };

    //判断文件夹中是否存在apk
    private boolean isApkExistence() {
        File filesDir = activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File[] files = filesDir.listFiles();
        List<String> s = new ArrayList<>();
        filename = Constant.DOWNLOAD_APK + versionNumber + ".apk";
        boolean isApkExit = false;
        for (int i = 0; i < files.length; i++) {
            if (filename.equals(files[i].getName())) {
                isApkExit = true;
                continue;
            } else if (files[i].getName().contains(Constant.DOWNLOAD_APK)) {
                files[i].delete();
            }
        }
        if (isApkExit) {
            installApk(activity, activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + filename);
        }
        return isApkExit;
    }

    //安装apk
    private void installApk(Context context, String path) {
        File apkFile = new File(path);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uriForFile = FileProvider.getUriForFile(context, "com.gogotalk.FileProvider", apkFile);//中间参数为 provider 中的 authorities
            intent.setDataAndType(uriForFile, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        destory();
        context.startActivity(intent);
    }


    //释放资源
    public void destory() {
        if (commonDialog != null) {
            commonDialog.dismiss();
        }
    }
}
