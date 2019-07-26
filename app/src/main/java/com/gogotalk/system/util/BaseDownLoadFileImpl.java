package com.gogotalk.system.util;

import java.io.File;

public abstract class BaseDownLoadFileImpl implements IDownLoadFile {
    IDownLoadingLisener downLoadingLisener;

    public BaseDownLoadFileImpl setDownLoadingLisener(IDownLoadingLisener downLoadingLisener) {
        this.downLoadingLisener = downLoadingLisener;
        return this;
    }

    public interface IDownLoadingLisener {
        void onDownLoadFinsh();

        void onDownLoadFail();

        void onDownLoadProgress(int current, int toatal);
    }
}
