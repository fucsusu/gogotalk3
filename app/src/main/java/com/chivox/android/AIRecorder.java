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
 * 
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

    /** 录音机开始录音 */
    public int start(final String path, final Callback callback) {

        stop();
        Log.d(TAG, "starting");
        running = true;

        future = workerThread.submit(new Runnable() {

            @Override
            public void run() {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
                RandomAccessFile file = null;
                AudioRecord recorder = null;
                try {
                    if (path != null) {
                        file = fopen(path);
                    }

                    /*
                    int bufferSize = 320000; // 10s is enough
                    int minBufferSize = AudioRecord.getMinBufferSize(FREQUENCY, AudioFormat.CHANNEL_IN_MONO,
                            AudioFormat.ENCODING_PCM_16BIT);

                    if (minBufferSize > bufferSize) {
                        bufferSize = minBufferSize;
                    }
                    */
                    Log.d(TAG, "#recorder new AudioRecord() 0");
                    recorder = new AudioRecord(AudioSource.DEFAULT, FREQUENCY, AudioFormat.CHANNEL_IN_MONO,
                            AudioFormat.ENCODING_PCM_16BIT, 320000); // 10s is enough
                    Log.d(TAG, "#recorder new AudioRecord() 1");
                    Log.d(TAG, "#recorder.startRecording() 0");

                    recorder.startRecording();

                    Log.d(TAG, "#recorder.startRecording() 1");
                    Log.d(TAG, "started");

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
                        Log.d(TAG, "#recorder.getRecordingState() 0");
                        if (recorder.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
                            Log.d("wuhongjie", "#recorder.getRecordingState() break:"+recorder.getRecordingState());
                            break;
                        }
                        Log.d("wuhongjie", "#recorder.getRecordingState() :"+recorder.getRecordingState());
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
                            if (file != null) {
                                Log.d(TAG, "#recorder fwrite() 0");
                                /**把音频文件存入本地.wav文件*/
                                fwrite(file, buffer, 0, size);
                                Log.d(TAG, "#recorder fwrite() 1");
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

                    if (file != null) {
                        try {
                            fclose(file);
                            latestPath = path;
                        } catch (IOException e) {
                            // ignore
                        }
                    }
                }
            }
        });

        return 0;
    }

    /** 录音机停止录音 */
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

    /** 录音机回放方法 */
    public int playback() {

        stop();

        if (this.latestPath == null) {
            return -1;
        }

        Log.d(TAG, "playback starting");

        running = true;
        future = workerThread.submit(new Runnable() {

            @Override
            public void run() {

                RandomAccessFile file = null;

                int bufferSize = CHANNELS * FREQUENCY * BITS * INTERVAL / 1000 / 8;
                int minBufferSize = AudioTrack.getMinBufferSize(FREQUENCY, AudioFormat.CHANNEL_OUT_MONO,
                        AudioFormat.ENCODING_PCM_16BIT);

                if (minBufferSize > bufferSize) {
                    bufferSize = minBufferSize;
                }

                byte buffer[] = new byte[bufferSize];

                AudioTrack player = null;

                try {
                    
                    player = new AudioTrack(AudioManager.STREAM_MUSIC, FREQUENCY, AudioFormat.CHANNEL_OUT_MONO,
                            AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);
                    /** 回放的是本地的音频，从音频位置44开始读 */
                    file = new RandomAccessFile(latestPath, "r");
                    file.seek(44);

                    player.play();

                    Log.d(TAG, "playback started");

                    while (running) {

                        int size = file.read(buffer, 0, buffer.length);
                        if (size == -1) {
                            break;
                        }
                        player.write(buffer, 0, size);
                    }
                    player.flush();

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                } finally {
                    try {
                        running = false;

                        if (player != null) {
                            if (player.getPlayState() != AudioTrack.PLAYSTATE_STOPPED) {
                                player.stop();
                            }
                            player.release();
                        }

                        Log.d(TAG, "playback stoped");

                        if (file != null) {
                            file.close();
                        }
                    } catch (IOException e) {
                        // ignore
                    }
                }

            }
        });

        return 0;
    }




    private RandomAccessFile fopen(String path) throws IOException {
        File f = new File(path);

		if (f.exists()) {
			f.delete();
		} else {
			File parentDir = f.getParentFile();
			if (!parentDir.exists()) {
				parentDir.mkdirs();
			}
		}

        RandomAccessFile file = new RandomAccessFile(f, "rw");

        /* RIFF header */
        file.writeBytes("RIFF"); // riff id
        file.writeInt(0); // riff chunk size *PLACEHOLDER*
        file.writeBytes("WAVE"); // wave type

        /* fmt chunk */
        file.writeBytes("fmt "); // fmt id
        file.writeInt(Integer.reverseBytes(16)); // fmt chunk size
        file.writeShort(Short.reverseBytes((short) 1)); // format: 1(PCM)
        file.writeShort(Short.reverseBytes((short) CHANNELS)); // channels: 1
        file.writeInt(Integer.reverseBytes(FREQUENCY)); // samples per second
        file.writeInt(Integer.reverseBytes((int) (CHANNELS * FREQUENCY * BITS / 8))); // BPSecond
        file.writeShort(Short.reverseBytes((short) (CHANNELS * BITS / 8))); // BPSample
        file.writeShort(Short.reverseBytes((short) (CHANNELS * BITS))); // bPSample

        /* data chunk */
        file.writeBytes("data"); // data id
        file.writeInt(0); // data chunk size *PLACEHOLDER*

        Log.d(TAG, "wav path: " + path);
        return file;
    }

    private void fwrite(RandomAccessFile file, byte[] data, int offset, int size) throws IOException {
        file.write(data, offset, size);
        Log.d(TAG, "fwrite: " + size);
    }

    private void fclose(RandomAccessFile file) throws IOException {
        try {
            file.seek(4); // riff chunk size
            file.writeInt(Integer.reverseBytes((int) (file.length() - 8)));

            file.seek(40); // data chunk size
            file.writeInt(Integer.reverseBytes((int) (file.length() - 44)));

            Log.d(TAG, "wav size: " + file.length());

        } finally {
            file.close();
        }
    }
}