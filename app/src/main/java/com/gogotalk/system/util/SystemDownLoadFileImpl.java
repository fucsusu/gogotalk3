package com.gogotalk.system.util;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.gogotalk.system.R;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

import static android.app.DownloadManager.Request.VISIBILITY_HIDDEN;

public class SystemDownLoadFileImpl extends BaseDownLoadFileImpl {
    public DownloadManager downloadManager;
    public long downLoadId;
    public Disposable mDisposable;
    private int oldCurrent = 0;
    private int stopTimes = 0;

    @Inject
    public SystemDownLoadFileImpl() {
    }

    @Override
    public void downLoadFile(Context context, String fileUrl, String fileName) {
        //创建request对象
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl))
                //设置什么网络情况下可以下载
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setNotificationVisibility(VISIBILITY_HIDDEN)
                //设置漫游状态下是否可以下载
                .setAllowedOverRoaming(false)
                //设置文件存放目录
                .setDestinationInExternalFilesDir(context.getApplicationContext(), Environment.DIRECTORY_DOWNLOADS, fileName);
        //获取系统服务
        downloadManager = (DownloadManager) context.getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        //进行下载
        downLoadId = downloadManager.enqueue(request);
        Log.e("TAG", "downLoadFile: "+downLoadId );

        Observable.interval(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        mDisposable = disposable;
                    }

                    @Override
                    public void onNext(@NonNull Long number) {
                        if (queryStatus()) {
                            downLoadingLisener.onDownLoadProgress(100, 100);
                            downLoadingLisener.onDownLoadFinsh();
                            cancle();
                        } else {
                            getDownloadPercent();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        downLoadingLisener.onDownLoadFail();
                        cancle();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    //获取下载进度刷新界面
    private void getDownloadPercent() {
        Cursor c = downloadManager.query(new DownloadManager.Query().setFilterById(downLoadId));
        if (c == null || !c.moveToFirst()) {
            if (c != null && !c.isClosed()) {
                c.close();
            }
            downLoadingLisener.onDownLoadFail();
        } else { // 以下是从游标中进行信息提取
            int mDownload_so_far = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            int mDownload_all = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            if (mDownload_so_far == oldCurrent) {
                stopTimes++;
                if (stopTimes > 20) {
                    downLoadingLisener.onDownLoadFail();
                    cancle();
                    return;
                }
            } else {
                oldCurrent = mDownload_so_far;
                stopTimes = 0;
            }
            if (mDownload_all > 0) {
                downLoadingLisener.onDownLoadProgress(mDownload_so_far, mDownload_all);
                Log.e("TAG", "getDownloadPercent: " + downLoadId);
            }
            if (!c.isClosed()) {
                c.close();
            }
        }
    }

    // 根据DownloadManager下载的Id，查询DownloadManager某个Id的下载任务状态。
    private boolean queryStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downLoadId);
        Cursor cursor = downloadManager.query(query);
        boolean isFinsh = false;
        if (cursor.moveToFirst()) {
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                case DownloadManager.STATUS_SUCCESSFUL:
                    isFinsh = true;
                    break;
                default:
                    isFinsh = false;
                    break;
            }
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return isFinsh;
    }

    private void cancle() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        downloadManager = null;
    }
}
