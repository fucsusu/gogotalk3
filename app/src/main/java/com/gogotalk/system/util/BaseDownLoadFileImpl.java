package com.gogotalk.system.util;

import android.app.DownloadManager;

public abstract class BaseDownLoadFileImpl implements IDownLoadFile{
    IDownLoadingLisener downLoadingLisener;

    public void setDownLoadingLisener(IDownLoadingLisener downLoadingLisener) {
        this.downLoadingLisener = downLoadingLisener;
    }
    public interface IDownLoadingLisener{
        void onDownLoading(long downLoadId);
    }
    public abstract DownloadManager getDownloadManager();
}
