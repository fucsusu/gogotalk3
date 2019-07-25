package com.gogotalk.system.util;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Toast;

import com.gogotalk.system.R;
import com.gogotalk.system.app.AiRoomApplication;

import java.io.File;

import javax.inject.Inject;

/**
 * Created by fucc
 * /storage/emulated/0/Android/data/com.gogotalk/files/Download 课件路径
 * Date: 2019-07-19 14:36
 */
public class CoursewareDownLoadUtil {
    private static CoursewareDownLoadUtil mUtil;
    public ProgressBar mProgress;
    public DownloadCompleteReceiver completeReceiver;
    public Context mContent;
    public DownloadManager downloadManager;
    public PopupWindow popupWindow;
    public long downloadId;
    private boolean isDowFinsh = false;
    private CoursewareDownFinsh mDownFinsh;
    @Inject
    BaseDownLoadFileImpl downLoadFile;

    Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (!isDowFinsh) {
                        getDownloadPercent();
                    }
                    break;
                case 2:
                    Log.e("TAG", "dispatchMessage: 解压成功");
                    File file = new File(mContent.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + fileMd5);
                    saveFile.renameTo(file);
                    destory();
                    if (mDownFinsh != null) {
                        mDownFinsh.finsh(file.getAbsolutePath());
                    }
                    break;
                case 3:
                    downLoadFail();
                    break;
            }
        }
    };
    public TextView mProgressTxt;
    //存储路径
    public String fileMd5;
    public File saveFile;
    public int oldIndex;
    public int pause;

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

    private class DownloadCompleteReceiver extends BroadcastReceiver {

        public File file;

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
                            file = new File(Uri.parse(uri).getPath());
                            filename = file.getName();
                        }
                        Log.e("TAG", "onReceive:下载完成 " + file.getAbsolutePath());

                        if (filename.contains("zip")) {
                            isDowFinsh = true;
                            mProgress.setMax(100);
                            mProgress.setProgress(100);
                            mProgressTxt.setText(mContent.getResources().getString(R.string.courseware_load_txt) + "100%");
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    mZipProcess(file);
                                }
                            }).start();
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
        downLoadFile = AiRoomApplication.getInstance().getNetComponent().getDownLoadFileImpl();
        downLoadFile.setDownLoadingLisener(new BaseDownLoadFileImpl.IDownLoadingLisener() {
            @Override
            public void onDownLoading(long downLoadId) {
                downloadId = downLoadId;
                showDownloadPopup(mContent, view);
                registerReceiver();
                handler.sendEmptyMessageDelayed(1, 500);
            }
        });
        downLoadFile.downLoadFile(mContent, fileUrl, fileMd5);
        downloadManager = downLoadFile.getDownloadManager();
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

    /**
     * Zip处理
     */
    public void mZipProcess(File zipFile) {
        ZipUtils.UnZipFolder(zipFile.getAbsolutePath(), saveFile.getAbsolutePath(), new ZipUtils.IProgress() {
            @Override
            public void onProgress(int progress) {
                Log.e("TAG", "onProgress: " + progress);
            }

            //解压失败
            @Override
            public void onError(String msg) {
                Toast.makeText(mContent, "解压失败！", Toast.LENGTH_SHORT).show();
                if (handler != null) {
                    handler.sendEmptyMessage(3);
                }
            }

            //成功
            @Override
            public void onDone() {
                //解压成功对文件夹做个标识
                if (handler != null) {
                    handler.sendEmptyMessage(2);
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

    //获取下载进度刷新界面
    private void getDownloadPercent() {
        Cursor c = downloadManager.query(new DownloadManager.Query().setFilterById(downloadId));
        if (c == null || !c.moveToFirst()) {
            if (c != null && !c.isClosed()) {
                c.close();
            }
            downLoadFail();
        } else { // 以下是从游标中进行信息提取
            int mDownload_so_far = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            int mDownload_all = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            if (!isDowFinsh) {
                if (mDownload_all > 0 && mDownload_so_far <= mDownload_all) {
                    mProgress.setMax(mDownload_all);
                    mProgress.setProgress(mDownload_so_far);
                    mProgressTxt.setText(mContent.getResources().getString(R.string.courseware_load_txt) + Math.abs(mDownload_so_far / (mDownload_all / 100)) + "%");
                    Log.e("TAG", "getDownloadPercent: " + mDownload_so_far + "||" + mDownload_all + "|||" + Math.abs(mDownload_so_far / (mDownload_all / 100)));
                }
                if (oldIndex ==mDownload_so_far) {
                    pause ++;
                    if (pause>20) {
                        downLoadFail();
                        return;
                    }
                }else {
                    oldIndex=mDownload_so_far;
                }
                handler.sendEmptyMessageDelayed(1, 500);
            } else {
                mProgressTxt.setText(mContent.getResources().getString(R.string.courseware_load_txt) + "100%");
            }
            if (!c.isClosed()) {
                c.close();
            }
        }
    }

    public interface CoursewareDownFinsh {
        void finsh(String filePath);
    }


    //注册下载完成广播
    private void registerReceiver() {
        completeReceiver = new DownloadCompleteReceiver();
        mContent.registerReceiver(completeReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    //注销广播
    private void unregisterReceiver() {
        if (completeReceiver != null) {
            mContent.unregisterReceiver(completeReceiver);
        }
    }

    //释放资源
    public void destory() {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
        if (completeReceiver != null) {
            unregisterReceiver();
        }
    }

    //下载失败
    private void downLoadFail() {
        if (downloadManager != null) {
            downloadManager.remove(downloadId);
        }
        if (saveFile != null) {
            if (saveFile.exists()) {
                DelectFileUtil.DeleteFolder(saveFile);
            }
        }
        File file = new File(mContent.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + fileMd5 + "zip");
        if (file.exists()) {
            file.delete();
        }
        destory();
        mDownFinsh.finsh("");
        Log.e("TAG", "getDownloadPercent:下载失败 ");
    }
}
