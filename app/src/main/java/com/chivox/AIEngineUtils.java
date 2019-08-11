package com.chivox;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.chivox.android.AIRecorder;
import com.chivox.android.MyRecorder;
import com.gogotalk.system.app.AiRoomApplication;
import com.gogotalk.system.util.LogUtil;
import com.gogotalk.system.zego.ZGBaseHelper;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AIEngineUtils {
    private static final String TAG = "AIEngineUtils";
    private static long engine = 0;
    private static ExecutorService workerThread = Executors.newFixedThreadPool(1);
    private static String appKey = "156499804000001a";
    private static String secretKey = "8d4a44f07c08c1fd572258162429b7b7";
    private String userId = "tester";
    private String refText = "I want to know the present and past of Hong Kong.";
    private long waitEndTime;
    private long waitStartTime;
    private int rank = 100;
    private String type = "en.word.score";
    private IEstimateCallback iEstimateCallback;
    private boolean isStart;
    private static AIEngineUtils aiEngineUtils;

    public AIEngineUtils setiEstimateCallback(IEstimateCallback iEstimateCallback) {
        this.iEstimateCallback = iEstimateCallback;
        return this;
    }

    public interface IEstimateCallback {
        void onEstimateResult(String result, int rank);
    }

    private AIEngineUtils() {
    }

    public static AIEngineUtils getInstance() {
        if (aiEngineUtils == null) {
            synchronized (AIEngineUtils.class) {
                if (aiEngineUtils == null) {
                    aiEngineUtils = new AIEngineUtils();
                }
            }
        }
        return aiEngineUtils;
    }


    public void initSDK() {
        runOnWorkerThread(new Runnable() {
            public void run() {
                /* create aiengine instance */
                if (engine == 0) {
                    //sdk证书路径
                    String provisionPath = AIEngineHelper.extractResourceOnce(AiRoomApplication.getInstance().getApplicationContext(), "aiengine.provision", false);
                    //String vadPath = AIEngineHelper.extractResourceOnce(getApplicationContext(), "vad.0.12.20160802.bin", false);
                    Log.d(TAG, "provisionPath:" + provisionPath);
                    String path = AIEngineHelper.getFilesDir(AiRoomApplication.getInstance().getApplicationContext()).getPath();
					/*String cfg = String.format("{ \"prof\":{\"enable\":1, \"output\":\"E:/log.log\"}, \"appKey\": \"%s\", \"secretKey\": \"%s\", \"provision\": \"%s\", \"cloud\": {\"server\": \"ws://cloud.chivox.com\"}}",
							appKey, secretKey,provisionPath);*/
                    String cfg = String.format("{ \"prof\":{\"enable\":1, \"output\":\"" + path + "/log.log\"}, \"appKey\": \"%s\", \"secretKey\": \"%s\", \"provision\": \"%s\", \"cloud\": {\"server\": \"ws://cloud.chivox.com\"}}",
                            appKey, secretKey, provisionPath);
                    Log.d(TAG, "cfg: " + cfg);
                    /**初始化引擎实例*/
                    engine = AIEngine.aiengine_new(cfg, AiRoomApplication.getInstance().getApplicationContext());
                    Log.d(TAG, "aiengine: " + engine);
                }
            }
        });
    }

    public AIEngineUtils setContent(String content) {
        refText = content;
        return this;
    }

    public AIEngineUtils setType(String type) {
        if ("word".equals(type)) {
            this.type = "en.word.score";
        } else if ("sent".equals(type)) {
            this.type = "en.sent.score";
        }
        return this;
    }

    public AIEngineUtils setUserId(String id) {
        userId = id;
        return this;
    }

    /**
     * 需要注意：AIRecorder回调方法，不是评测回调方法
     */
    AIRecorder.Callback recorderCallback = new AIRecorder.Callback() {
        public void onStarted() {
            //句子启动参数
            String param = "{\"coreProvideType\": \"cloud\", \"app\": {\"userId\": \"" + userId + "\"}, \"audio\": {\"audioType\": \"wav\", \"channel\": 1, \"sampleBytes\": 2, \"sampleRate\": 16000,\"compress\":\"speex\"}, \"request\": {\"coreType\": \"" + type + "\", \"refText\":\"" + refText + "\", \"rank\": " + rank + "}}";
            byte[] id = new byte[64];
            /*开启引擎*/
            int rv = AIEngine.aiengine_start(engine, param, id, callback, AiRoomApplication.getInstance().getApplicationContext());

            int i = 0;
            for (; i < id.length; i++) {
                if (id[i] == '\0')
                    break;
            }

            String tokenId = new String(id, 0, i);
        }

        /*调用引擎停止录音接口*/
        public void onStopped() {
            AIEngine.aiengine_stop(engine);
            waitStartTime = System.currentTimeMillis();
        }

        public void onData(byte[] data, int size) {
            AIEngine.aiengine_feed(engine, data, size);
        }
    };

    /**
     * 评测回调接口
     */
    private AIEngine.aiengine_callback callback = new AIEngine.aiengine_callback() {
        @Override
        public int run(byte[] id, int type, byte[] data, int size) {
            if (type == AIEngine.AIENGINE_MESSAGE_TYPE_JSON) {
                String strId = new String(id);
                Log.d(TAG, "call back token id: " + strId);
                final String result = new String(data, 0, size).trim(); /* must trim the end '\0' */
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.has("vad_status") || json.has("volume")) {
                        int status = json.optInt("vad_status");
                        final int volume = json.optInt("volume");
                        if (status == 2) {
                            ZGBaseHelper.sharedInstance().stopAudioRecord();
                        }
                    } else {
                        ZGBaseHelper.sharedInstance().stopAudioRecord();
                        waitEndTime = System.currentTimeMillis();
                        Log.d(TAG, "wait time for result: " + (waitEndTime - waitStartTime));
                        iEstimateCallback.onEstimateResult(result, rank);
                    }
                } catch (JSONException e) {
                    /* ignore */
                }

            }
            return 0;
        }
    };


    public static void runOnWorkerThread(Runnable runnable) {
        workerThread.execute(runnable);
    }

    /**
     * 开始录音
     */
    public void startRecord() {
        if (engine == 0) {
            LogUtil.e("TAG", "startRecord: ", engine);
            return;
        }
        isStart = true;
        ZGBaseHelper.sharedInstance().startAudioRecord();
        recorderCallback.onStarted();
    }

    public void writeAudioData(byte[] bytes) {
        if (isStart) {
            recorderCallback.onData(bytes, bytes.length);
        }
    }

    /**
     * 停止录音
     */
    public void stopRecord() {
        isStart = false;
        ZGBaseHelper.sharedInstance().stopAudioRecord();
        if (engine == 0) {
            return;
        }
        recorderCallback.onStopped();
    }

    public boolean isStart() {
        return isStart;
    }

    /**
     * 销毁引擎
     */
    public void onDestroy() {
        if (engine != 0) {
            int i = AIEngine.aiengine_delete(engine);
            engine = 0;
            Log.d(TAG, "engine deleted: " + i);
        }
    }

}
