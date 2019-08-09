package com.chivox.android;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder.AudioSource;
import android.util.Log;

/**
 * FIXME (441000, 1, 16) is the only format that is guaranteed to work on all
 * devices
 *
 * @author shun.zhang
 */
public class AIRecorder {

    private static String TAG = "AIRecorder";

    private static int CHANNELS = 1;
    private static int BITS = 16;
    private static int FREQUENCY = 16000; // sample rate
    private static int INTERVAL = 100; // callback interval

    private String latestPath = null; // latest wave file path

    private volatile boolean running = false;

    private ExecutorService workerThread;
    private Future<?> future = null;

    public static interface Callback {
        public void onStarted();

        public void onData(byte[] data, int size);

        public void onStopped();
    }

    public AIRecorder() {
        workerThread = Executors.newSingleThreadScheduledExecutor();
    }

    public boolean isRunning() {
        return running;
    }

    /**
     * 录音机开始录音
     */
    public int start(final Callback callback) {

        stop();
        Log.d(TAG, "starting");
        running = true;

        future = workerThread.submit(new Runnable() {

            @Override
            public void run() {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
                AudioRecord recorder = null;
                try {
                    Log.d(TAG, "#recorder new AudioRecord() 0");
                    int a = AudioRecord.getMinBufferSize(44100,AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT);//计算最小缓冲区

                    recorder = new AudioRecord(AudioSource.MIC, FREQUENCY, AudioFormat.CHANNEL_IN_MONO,
                            AudioFormat.ENCODING_PCM_16BIT, a); // 10s is enough
                    Log.d(TAG, "#recorder new AudioRecord() "+recorder.getState());

                    recorder.startRecording();

                    Log.d(TAG, "started"+recorder.getState());

                    /** 启动本次请求 */
                    callback.onStarted();

                    /* TODO started callback */
                    /* 丢弃用于修复瞬态的开始100 ms
                     * discard the beginning 100ms for fixing the transient
                     * noise bug shun.zhang, 2013-07-08
                     */
                    byte buffer[] = new byte[CHANNELS * FREQUENCY * BITS * INTERVAL / 1000 / 8];
                    int discardBytes = CHANNELS * FREQUENCY * BITS * 100 / 1000 / 8;
                    while (discardBytes > 0) {
                        int requestBytes = buffer.length < discardBytes ? buffer.length : discardBytes;
                        int readBytes = recorder.read(buffer, 0, requestBytes);
                        if (readBytes > 0) {
                            discardBytes -= readBytes;
                            Log.d(TAG, "discard: " + readBytes);
                        } else {
                            break;
                        }
                    }

                    while (running) {
                        Log.d(TAG, "#recorder.getRecordingState() "+recorder.getState());
                        if (recorder.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
                            Log.d("wuhongjie", "#recorder.getRecordingState() break:" + recorder.getRecordingState());
                            break;
                        }
                        Log.d("wuhongjie", "#recorder.getRecordingState() :" + recorder.getRecordingState());
                        Log.d(TAG, "#recorder.getRecordingState() 1");
                        Log.d(TAG, "#recorder.read() 0");

                        /** 从麦克风读取音频数据 */
                        int size = recorder.read(buffer, 0, buffer.length);
                        Log.d(TAG, "#recorder.read() 1 - " + size);
                        if (size > 0) {
                            if (callback != null) {
                                Log.d(TAG, "#recorder callback.run() 0");
                                callback.onData(buffer, size);
                                Log.d(TAG, "#recorder callback.run() 1");
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                } finally {
                    /** 在录音机关闭之前关闭评分引擎 */
                    callback.onStopped(); // invoke onStopped before recorder.stop()
                    running = false;
                    if (recorder != null) {
                        if (recorder.getRecordingState() != AudioRecord.RECORDSTATE_STOPPED) {
                            Log.d(TAG, "#recorder.stop() 0");
                            recorder.stop(); // FIXME elapse 400ms
                            Log.d(TAG, "#recorder.stop() 1");
                        }
                        recorder.release();
                    }

                    Log.d(TAG, "record stoped");
                }
            }
        });

        return 0;
    }

    public void writeRecorderData(byte[] bytes){

    }

    /**
     * 录音机停止录音
     */
    public int stop() {
        if (!running)
            return 0;

        Log.d(TAG, "stopping");
        running = false;
        if (future != null) {
            try {
                future.get();
            } catch (Exception e) {
                Log.e(TAG, "stop exception", e);
            } finally {
                future = null;
            }
        }

        return 0;
    }

}