package com.gogotalk.system.util;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.gogotalk.system.app.AiRoomApplication;
import com.gogotalk.system.model.api.ApiService;
import com.gogotalk.system.util.okHttpdownfile.DownloadProgressHandler;
import com.gogotalk.system.util.okHttpdownfile.ProgressHelper;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class OkHttpDownLoadFileImpl extends BaseDownLoadFileImpl{
    Disposable disposable;
    Context mContext;
    @Override
    public void downLoadFile(Context context, String fileUrl, String fileName) {
        mContext = context;
        ProgressHelper.setProgressHandler(new DownloadProgressHandler() {
            @Override
            protected void onProgress(long bytesRead, long contentLength, boolean done) {
                if(downLoadingLisener!=null){
                    downLoadingLisener.onDownLoadProgress((int) bytesRead,(int)contentLength);
                }
            }
        });
        ApiService apiService = AiRoomApplication.getInstance().getNetComponent().getApiService();
        Observable<ResponseBody> observable = apiService.downLoadClassFile(fileUrl);
        observable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        saveDataToFile(fileName,responseBody.byteStream(), responseBody.contentLength());
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(downLoadingLisener!=null){
                            ((Activity)mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    downLoadingLisener.onDownLoadFail();
                                }
                            });
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
    public void saveDataToFile(String fileName, InputStream stream,long totalLength){
        BufferedOutputStream writer = null;
        File file = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + fileName );
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            int index;
            byte[] bytes = new byte[1024];
            writer = new BufferedOutputStream(new FileOutputStream(file,false));
            while ((index = stream.read(bytes)) != -1) {
                writer.write(bytes, 0, index);
                writer.flush();
            }
            if(downLoadingLisener!=null) {
                ((Activity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        downLoadingLisener.onDownLoadFinsh();
                    }
                });

            }
        } catch (IOException e) {
            e.printStackTrace();
            if(downLoadingLisener!=null){
                ((Activity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        downLoadingLisener.onDownLoadFail();
                    }
                });

            }
        }finally {
            try {
                if(writer != null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
