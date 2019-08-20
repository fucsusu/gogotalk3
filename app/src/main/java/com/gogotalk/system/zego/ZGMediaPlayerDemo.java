package com.gogotalk.system.zego;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.zego.zegoavkit2.IZegoMediaPlayerCallback;
import com.zego.zegoavkit2.IZegoMediaPlayerVideoPlayCallback;
import com.zego.zegoavkit2.ZegoMediaPlayer;
import com.zego.zegoavkit2.ZegoVideoCaptureFactory;
import com.zego.zegoavkit2.ZegoVideoDataFormat;

import java.io.File;

/**
 * ZGMediaPlayerDemo
 * <p>
 * 你可以通过 {@link #startPlay} 来进行播放音视频资源。
 * 支持播放格式  MP3、MP4。
 * 并且可以把播放的内容通过zegoSDK外部采集的方式进行推流。
 * <p>
 * <p>
 * 一般是k歌房场景需要使用到该功能。把MV视频作为推流数据推给观众。
 * k歌房还能通过 {@link #setPlayerType} 来指定类型，
 * 传入 {@link ZegoMediaPlayer#PlayerTypeAux} 类型让播放器的声音
 * 也一起混音到推流中。
 * <p>
 * 基于zegoSDK {@link ZegoMediaPlayer} 播放器 与 zegoSDK {@link ZegoVideoCaptureFactory} 外部采集
 * 工厂实现。
 * <a href="https://doc.zego.im/CN/271.html"> zego外部采集 </a>
 * <a href="https://doc.zego.im/CN/283.html"> zego媒体播放器 </a>
 * 实现步骤如下
 * <p>
 * 1.开启zegoSDK外部采集功能
 * 2.设置外部采集工厂 !!!注意，设置工厂需要在
 * { @link ZegoLiveRoom#initSDK(long, byte[]) }
 * 之前。否则设置的工厂不生效
 * <p>
 * 3.创建初始化ZegoMediaPlayer播放器
 * 4.设置ZegoMediaPlayer播放器回调
 * 5.把ZegoMediaPlayer播放器回调的视频帧塞给外部采集工厂
 * <p>
 * <p>
 * 具体使用方法可以参考以下示例代码
 * public class MainActivity extends Activity {
 *
 * @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
 * <p>
 * // 视图设置 view
 * ZGMediaPlayerDemo.sharedInstance(this).setView(videoView);
 * <p>
 * // 音量设置 1-100
 * ZGMediaPlayerDemo.sharedInstance(this).setVolume(100);
 * <p>
 * // 是开启aux混音
 * ZGMediaPlayerDemo.sharedInstance(MediaPlayerDemoUI.this).setPlayerType(ZegoMediaPlayer.PlayerTypeAux);
 * <p>
 * // 播放视频资源
 * ZGMediaPlayerDemo.sharedInstance(this).startPlay(path, repeat);
 * <p>
 * }
 * }
 *
 * <strong>警告:</strong> 如果不用播放器，
 * 请使用 {@link ZGMediaPlayerDemo#unInit()} 释放掉播放器，避免浪费内存
 */
public class ZGMediaPlayerDemo implements IZegoMediaPlayerVideoPlayCallback {

    static private ZGMediaPlayerDemo zgMediaPlayerDemo;

//    public static ZGMediaPlayerDemo sharedInstance(int index) {
//        synchronized (ZGMediaPlayerDemo.class) {
//            if (zgMediaPlayerDemo == null) {
//                zgMediaPlayerDemo = new ZGMediaPlayerDemo(index);
//            }
//        }
//        return zgMediaPlayerDemo;
//    }

    public String TAG = "MediaPlayerDemo";


    public ZGMediaPlayerDemo(int index) {
        // 创建播放器对象
        zegoMediaPlayer = new ZegoMediaPlayer();
        // 初始化播放器
        zegoMediaPlayer.init(ZegoMediaPlayer.PlayerTypePlayer, index);
        // 设置播放器回调
        zegoMediaPlayer.setCallback(zgMediaPlayerCallback);
    }


    /* 媒体播放器 */
    private ZegoMediaPlayer zegoMediaPlayer;

    // --------------------------------对外接口-------------------------------- //

    /* 设置view */
    public void setView(View view) {
        if (zegoMediaPlayer != null) {
            zegoMediaPlayer.setView(view);
        }
    }

    /* 释放MediaPlayer 和一些相关操作 */
    public void unInit() {
        if (zegoMediaPlayer != null) {
            stopPlay();
            zegoMediaPlayer.setCallback(null);
            zegoMediaPlayer.setVideoPlayCallback(null, 0);
            zegoMediaPlayer.uninit();
            zegoMediaPlayer = null;
            zgMediaPlayerDemo = null;
        }
    }

    /* 停止播放 */
    public void stopPlay() {
        Log.e(TAG, "stopPlay");
        if (zegoMediaPlayer != null) {
            zegoMediaPlayer.stop();
        }
    }

    /**
     * 开始播放
     *
     * @param filePath file路径
     * @param repeat   是否重复播放
     */
    public static void startPlay(String filePath, boolean repeat, int index) {
        // 创建播放器对象
        final ZegoMediaPlayer zegoMediaPlayer = new ZegoMediaPlayer();
        // 初始化播放器
        zegoMediaPlayer.init(ZegoMediaPlayer.PlayerTypePlayer, index);
        // 设置播放器回调
        zegoMediaPlayer.setCallback(new IZegoMediaPlayerCallback() {
            @Override
            public void onPlayStart() {

            }

            @Override
            public void onPlayPause() {

            }

            @Override
            public void onPlayStop() {

            }

            @Override
            public void onPlayResume() {

            }

            @Override
            public void onPlayError(int i) {

            }

            @Override
            public void onVideoBegin() {

            }

            @Override
            public void onAudioBegin() {

            }

            @Override
            public void onPlayEnd() {

            }

            @Override
            public void onBufferBegin() {

            }

            @Override
            public void onBufferEnd() {

            }

            @Override
            public void onSeekComplete(int i, long l) {
                zegoMediaPlayer.stop();
                zegoMediaPlayer.setCallback(null);
                zegoMediaPlayer.setVideoPlayCallback(null, 0);
                zegoMediaPlayer.uninit();
                zgMediaPlayerDemo = null;
            }

            @Override
            public void onSnapshot(Bitmap bitmap) {

            }

            @Override
            public void onLoadComplete() {

            }

            @Override
            public void onProcessInterval(long l) {

            }
        });
        zegoMediaPlayer.setVolume(100);
        zegoMediaPlayer.start(filePath, repeat);
    }


    public static void startPlay(String filePath, String url, boolean repeat,int index) {
        Log.e("TAG", String.format("startPlay path: %s", filePath));
        File file = new File(filePath);
        if (!TextUtils.isEmpty(filePath)) {
            if (file.exists()) {
                startPlay(filePath, repeat, index);
            } else {
                startPlay(url, repeat, index);
            }
        }
    }

    /**
     * 暂停播放
     */
    public void pausePlay() {
        if (zegoMediaPlayer != null) {
            zegoMediaPlayer.pause();
        }
    }

    /**
     * 恢复播放
     */
    public void resume() {
        if (zegoMediaPlayer != null) {
            zegoMediaPlayer.resume();
        }
    }

    /**
     * 快进到指定进度
     *
     * @param millisecond 进度单位 毫秒
     */
    public void seekTo(long millisecond) {
        if (zegoMediaPlayer != null) {
            zegoMediaPlayer.seekTo(millisecond);
        }
    }

    /**
     * 获取该文件的播放时长
     *
     * @return
     */
    public long getDuration() {
        if (zegoMediaPlayer != null) {
            return zegoMediaPlayer.getDuration();
        }
        return -1;
    }

    /**
     * 设置音量
     *
     * @param volume
     */
    public void setVolume(int volume) {
        if (zegoMediaPlayer != null) {
            zegoMediaPlayer.setVolume(volume);
        }
    }

    /**
     * 设置音轨
     *
     * @param audioStream
     */
    public void setAudioStream(int audioStream) {
        if (zegoMediaPlayer != null) {
            zegoMediaPlayer.setAudioStream(audioStream);
        }
    }

    /**
     * 设置播放类型
     *
     * @param type
     */
    public void setPlayerType(int type) {
        if (zegoMediaPlayer != null) {
            zegoMediaPlayer.setPlayerType(type);
        }
    }

    /**
     * 获取音轨数量
     */
    public long getAudioStreamCount() {
        if (zegoMediaPlayer != null) {
            return zegoMediaPlayer.getAudioStreamCount();
        }
        return -1;
    }


    @Override
    public void onPlayVideoData(byte[] bytes, int i, ZegoVideoDataFormat f) {
    }

    IZegoMediaPlayerCallback zgMediaPlayerCallback = new IZegoMediaPlayerCallback() {

        @Override
        public void onPlayStart() {
            Log.v(TAG, "onPlayStart");
        }

        @Override
        public void onPlayPause() {
            Log.v(TAG, "onPlayPause");
        }

        @Override
        public void onPlayStop() {
            stopPlay();
        }

        @Override
        public void onPlayResume() {
            Log.v(TAG, "onPlayResume");
        }

        @Override
        public void onPlayError(int errorCode) {
            Log.e(TAG, String.format("onPlayError error: %d", errorCode));
        }

        @Override
        public void onVideoBegin() {
            Log.v(TAG, "onVideoBegin");
        }

        @Override
        public void onAudioBegin() {
            Log.v(TAG, "onAudioBegin");
        }

        @Override
        public void onPlayEnd() {
        }

        @Override
        public void onBufferBegin() {
            Log.v(TAG, "onBufferBegin");
        }

        @Override
        public void onBufferEnd() {
            Log.v(TAG, "onBufferEnd");
        }

        /**
         * 完成快进到指定时刻
         * @param code 大于等于0表示成功，其它表示失败
         * @param millisecond 实际快进的进度
         */

        @Override
        public void onSeekComplete(int code, long millisecond) {
            Log.v(TAG, "onSeekComplete");
            unInit();
        }

        @Override
        public void onSnapshot(Bitmap bitmap) {

        }

        @Override
        public void onLoadComplete() {

        }

        @Override
        public void onProcessInterval(long l) {

        }

    };


}