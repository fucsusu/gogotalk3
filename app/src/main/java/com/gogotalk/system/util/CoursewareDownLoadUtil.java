package com.gogotalk.system.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gogotalk.system.R;
import com.gogotalk.system.app.AiRoomApplication;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by fucc
 * /storage/emulated/0/Android/data/com.gogotalk/files/Download 课件路径
 * Date: 2019-07-19 14:36
 */
public class CoursewareDownLoadUtil {
    private static CoursewareDownLoadUtil mUtil;
    public ProgressBar mProgress;
    public Context mContent;
    public PopupWindow popupWindow;
    private CoursewareDownFinsh mDownFinsh;
    @Inject
    BaseDownLoadFileImpl downLoadFile;
    public TextView mProgressTxt;
    //存储路径
    public String fileMd5;
    public File saveFile;
    public File zipFile;

    public static CoursewareDownLoadUtil getCoursewareUtil() {
        if (mUtil == null) {
            synchronized (CoursewareDownLoadUtil.class) {
                if (mUtil != null) {
                    return mUtil;
                } else {
                    return new CoursewareDownLoadUtil();
                }
            }
        } else {
            return mUtil;
        }
    }

    //开始下载文件
    public void downloadCourseware(final Context mContent, String fileUrl, final View view, String fileMd5, CoursewareDownFinsh finsh) {
        this.mDownFinsh = finsh;
        this.mContent = mContent;
        this.fileMd5 = fileMd5;
        if (TextUtils.isEmpty(fileUrl) || TextUtils.isEmpty(fileMd5)) {
            mDownFinsh.finsh("");
            return;
        }
        if (isCoursewareExistence(fileMd5)) {
            mDownFinsh.finsh(mContent.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + fileMd5);
            return;
        }
        //创建文件夹
        saveFile = new File(mContent.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + fileMd5 + "_temp");
        if (!saveFile.exists()) {
            saveFile.mkdirs();
        }

        zipFile = new File(mContent.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + fileMd5 + "zip");
        if (zipFile.exists()) {
            DelectFileUtil.deleteDirectory(zipFile);
        }
        //开始下载
        AiRoomApplication.getInstance().getNetComponent().getDownLoadFileImpl()
                .setDownLoadingLisener(downLoadingLisener)
                .downLoadFile(mContent, fileUrl, fileMd5 + "zip");
        showDownloadPopup(mContent, view);
    }

    BaseDownLoadFileImpl.IDownLoadingLisener downLoadingLisener = new BaseDownLoadFileImpl.IDownLoadingLisener() {
        @Override
        public void onDownLoadFinsh() {
            mZipProcess();
        }

        @Override
        public void onDownLoadFail() {
            downLoadFail();
        }

        @Override
        public void onDownLoadProgress(int current, int toatal) {
            mProgress.setMax(toatal);
            mProgress.setProgress(current);
            mProgressTxt.setText(mContent.getResources().getString(R.string.courseware_load_txt) + Math.abs(current / (toatal / 100)) + "%");
            Log.e("TAG", "getDownloadPercent: " + current + "||" + toatal + "|||" + Math.abs(current / (toatal / 100)));
        }
    };

    /**
     * Zip处理
     */
    public void mZipProcess() {
        mProgressTxt.setText("加载中...");
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                ZipUtils.UnZipFolder(zipFile.getAbsolutePath(), saveFile.getAbsolutePath(), new ZipUtils.IProgress() {
                    @Override
                    public void onProgress(int progress) {
                        Log.e("TAG", "onProgress: " + progress);
                    }

                    //解压失败
                    @Override
                    public void onError(String msg) {
                        e.onNext(msg);
                    }

                    //成功
                    @Override
                    public void onDone() {
                        e.onNext("zip_success");
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                if ("zip_success".equals(s)) {
                    downLoadSucess();
                } else {
                    downLoadFail();
                    ToastUtils.showShortToast(mContent, "解压失败！" + s);
                }
            }
        });


    }

    //显示下载界面
    private void showDownloadPopup(Context context, View view) {
        popupWindow = new PopupWindow();
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View courseware_popup = LayoutInflater.from(context).inflate(R.layout.dialog_courseware_download, null);
        courseware_popup.setFocusable(true);
        popupWindow.setFocusable(false);
        popupWindow.setContentView(courseware_popup);
        mProgress = courseware_popup.findViewById(R.id.courseware_load_progress_popup);
        mProgressTxt = courseware_popup.findViewById(R.id.courseware_load_txt_popup);
        mProgressTxt.setText(mContent.getResources().getString(R.string.courseware_load_txt) + "0%");
        courseware_popup.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    public interface CoursewareDownFinsh {
        void finsh(String filePath);
    }

    //判断文件是否存在
    private boolean isCoursewareExistence(String fileMd5) {
        File filesDir = mContent.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        Log.e("TAG", "isCoursewareExistence: " + filesDir.getAbsolutePath());
        File[] files = filesDir.listFiles();
        boolean isFileExit = false;
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().equals(fileMd5)) {
                Log.e("TAG", "isCoursewareExistence: 已存在课件");
                isFileExit = true;
                break;
            }
        }
        return isFileExit;
    }


    private void downLoadSucess() {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
        //解压成功对文件夹做个标识
        File file = new File(mContent.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + fileMd5);
        saveFile.renameTo(file);
        if (mDownFinsh != null) {
            mDownFinsh.finsh(file.getAbsolutePath());
        }
    }

    //下载失败
    private void downLoadFail() {
        if (saveFile != null) {
            if (saveFile.exists()) {
                DelectFileUtil.DeleteFolder(saveFile);
            }
        }
        File file = new File(mContent.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + fileMd5 + "zip");
        if (file.exists()) {
            file.delete();
        }
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
        mDownFinsh.finsh("");
        ToastUtils.showLongToast(mContent, "下载失败，请查看网络环境是否正常！");
        Log.e("TAG", "getDownloadPercent:下载失败 ");
    }
}
