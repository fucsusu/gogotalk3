package com.gogotalk.system.util;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import javax.inject.Inject;

public class SystemDownLoadFileImpl extends BaseDownLoadFileImpl{
    public DownloadManager downloadManager;

    @Inject
    public SystemDownLoadFileImpl(){}



    @Override
    public void downLoadFile(Context context,String fileUrl,String fileName) {
        //创建request对象
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl))
                //设置什么网络情况下可以下载
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                //设置漫游状态下是否可以下载
                .setAllowedOverRoaming(false)
                //设置文件存放目录
                .setDestinationInExternalFilesDir(context.getApplicationContext(), Environment.DIRECTORY_DOWNLOADS, fileName + "zip");
        //获取系统服务
        downloadManager = (DownloadManager) context.getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        //进行下载
        long downLoadId = downloadManager.enqueue(request);
        if(downLoadingLisener!=null){
            downLoadingLisener.onDownLoading(downLoadId);
        }
    }

    @Override
    public DownloadManager getDownloadManager() {
        return downloadManager;
    }
}
