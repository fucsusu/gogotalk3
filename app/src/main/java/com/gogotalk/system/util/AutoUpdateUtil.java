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
    private DownloadManager downloadManager;
    private boolean isDownLoad = false;
    public long downloadId;
    public ProgressBar progressBar;
    public CommonDialog commonDialog;
    public int versionNumber;
    public DownloadCompleteReceiver completeReceiver;
    public Context activity;
    public TextView progressRate;


    private class DownloadCompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //在广播中取出下载任务的id
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            DownloadManager.Query query = new DownloadManager.Query();
            DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            query.setFilterById(id);
            Log.e("TAG", "onReceive: 下载完成通知");
            Cursor c = dm.query(query);
            if (c != null) {
                try {
                    if (c.moveToFirst()) {
                        String uri = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                        String filename = "";
                        if (uri != null) {
                            File file = new File(Uri.parse(uri).getPath());
                            filename = file.getName();
                        }
                        if (!filename.contains("gogotalk")) {
                            return;
                        }
                        int status = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            String path = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + filename;
                            installApk(context, path);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    c.close();
                }
            }
        }
    }

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
                                showUpdateMessage(data.getUpdateDescribe(), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //点下下载
                                        downLoadApk(data.getDownloadUrl());
                                    }
                                });
                            }
                        }
                    }
                });
    }

    //显示更新提示
    private void showUpdateMessage(String msg, final View.OnClickListener onClickListener) {
        CommonDialog.Builder builder = new CommonDialog.Builder(activity);
        builder.setMessage(msg);
        final CommonDialog twoButtonDialog = builder.createTwoButtonDialog();
        builder.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                twoButtonDialog.dismiss();
                onClickListener.onClick(view);
            }
        });
        builder.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                twoButtonDialog.dismiss();
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
        //创建request对象
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl))
                //设置什么网络情况下可以下载
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                //设置通知栏的标题
                .setTitle("下载")
                //设置通知栏的message
                .setDescription("GoGoTalk正在下载.....")
                //设置漫游状态下是否可以下载
                .setAllowedOverRoaming(false)
                //设置文件存放目录
                .setDestinationInExternalFilesDir(activity, Environment.DIRECTORY_DOWNLOADS, Constant.DOWNLOAD_APK + versionNumber + ".apk");
        //获取系统服务
        downloadManager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        //进行下载
        downloadId = downloadManager.enqueue(request);
        showDownLoadProgress();
        isDownLoad = true;
        progessThread.start();
        registerReceiver();
    }

    //注册下载完成广播
    private void registerReceiver() {
        completeReceiver = new DownloadCompleteReceiver();
        activity.registerReceiver(completeReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    //注销广播
    private void unregisterReceiver() {
        if (completeReceiver != null) {
            activity.unregisterReceiver(completeReceiver);
        }
    }


    //获取下载进度刷新界面
    private void getDownloadPercent() {
        Cursor c = downloadManager.query(new DownloadManager.Query().setFilterById(downloadId));
        if (c == null) {
            Log.e("TAG", "getDownloadPercent:下载失败 ");
            destory();
        } else { // 以下是从游标中进行信息提取
            if (!c.moveToFirst()) {
                Log.e("TAG", "getDownloadPercent:下载失败 ");
                if (!c.isClosed()) {
                    c.close();
                }
                destory();
                return;
            }
            int mDownload_so_far = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            int mDownload_all = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            progressBar.setProgress(mDownload_so_far);
            progressBar.setMax(mDownload_all);
            if (mDownload_so_far > 0 && mDownload_all > 0) {
                progressRate.setText("下载进度：" + mDownload_so_far / (mDownload_all / 100) + "%");
            }
            if (!c.isClosed()) {
                c.close();
            }
        }
    }

    //获取下载进度线程
    Thread progessThread = new Thread() {
        @Override
        public void run() {
            super.run();
            while (isDownLoad) {
                getDownloadPercent();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    //判断文件夹中是否存在apk
    private boolean isApkExistence() {
        File filesDir = activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File[] files = filesDir.listFiles();
        List<String> s = new ArrayList<>();
        String filename = Constant.DOWNLOAD_APK + versionNumber + ".apk";
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
        isDownLoad = false;
        if (commonDialog != null) {
            commonDialog.dismiss();
        }
        if (completeReceiver != null) {
            unregisterReceiver();
        }
    }
}
