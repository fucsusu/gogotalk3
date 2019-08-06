package com.chivox;

import android.util.Log;

import com.chivox.android.AIRecorder;
import com.gogotalk.system.app.AiRoomApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AIEngineUtils {
    private static final String TAG = "AIEngineUtils";
    private static AIRecorder recorder = null;
    private static long engine = 0;
    private static ExecutorService workerThread = Executors.newFixedThreadPool(1);
    private static String appKey = "156499804000001a";
    private static String secretKey = "8d4a44f07c08c1fd572258162429b7b7";
    private String userId = "tester";
    private String refText = "I want to know the present and past of Hong Kong.";
    private long waitEndTime;
    private long waitStartTime;
    /**录音文件（.wav）存放本地地址*/
    String wavPath = AIEngineHelper.getFilesDir(AiRoomApplication.getInstance()).getPath() + "/record/" + System.currentTimeMillis() + ".wav";
    private IEstimateCallback iEstimateCallback;

    public AIEngineUtils setiEstimateCallback(IEstimateCallback iEstimateCallback) {
        this.iEstimateCallback = iEstimateCallback;
        return this;
    }

    public interface IEstimateCallback{
        void onEstimateResult(String result);
    }
    private AIEngineUtils() {
    }

    static class AIEngineUtilsHoder {
        private static AIEngineUtils aiEngineUtils = new AIEngineUtils();
    }

    public static AIEngineUtils getInstance() {
        runOnWorkerThread(new Runnable() {
            public void run() {
                /* create aiengine instance */
                if (engine == 0) {
                    //sdk证书路径
                    String provisionPath = AIEngineHelper.extractResourceOnce(AiRoomApplication.getInstance(), "aiengine.provision", false);
                    //String vadPath = AIEngineHelper.extractResourceOnce(getApplicationContext(), "vad.0.12.20160802.bin", false);
                    Log.d(TAG, "provisionPath:"+provisionPath);
                    String path = AIEngineHelper.getFilesDir(AiRoomApplication.getInstance()).getPath();
					/*String cfg = String.format("{ \"prof\":{\"enable\":1, \"output\":\"E:/log.log\"}, \"appKey\": \"%s\", \"secretKey\": \"%s\", \"provision\": \"%s\", \"cloud\": {\"server\": \"ws://cloud.chivox.com\"}}",
							appKey, secretKey,provisionPath);*/
                    String cfg = String.format("{ \"prof\":{\"enable\":1, \"output\":\""+path+"/log.log\"}, \"appKey\": \"%s\", \"secretKey\": \"%s\", \"provision\": \"%s\", \"cloud\": {\"server\": \"ws://cloud.chivox.com\"}}",
                            appKey, secretKey,provisionPath);
                    Log.d(TAG, "cfg: " + cfg);
                    /**初始化引擎实例*/
                    engine = AIEngine.aiengine_new(cfg,  AiRoomApplication.getInstance());
                    Log.d(TAG, "aiengine: " + engine);
                }
                /* create airecorder instance  */
                if (recorder == null) {
                    recorder = new AIRecorder();
                    Log.d(TAG, "airecorder: " + recorder);
                }
            }
        });
        return AIEngineUtilsHoder.aiEngineUtils;
    }
    public AIEngineUtils setTxt(String txt){
        refText = txt;
        return this;
    }
    public AIEngineUtils setUserId(String id){
        userId = id;
        return this;
    }
    /** 需要注意：AIRecorder回调方法，不是评测回调方法 */
    AIRecorder.Callback recorderCallback = new AIRecorder.Callback() {
        public void onStarted() {
            //句子启动参数
            String param = "{\"coreProvideType\": \"cloud\", \"app\": {\"userId\": \"" + userId + "\"}, \"audio\": {\"audioType\": \"wav\", \"channel\": 1, \"sampleBytes\": 2, \"sampleRate\": 16000,\"compress\":\"speex\"}, \"request\": {\"coreType\": \"en.sent.score\", \"refText\":\"" + refText + "\", \"rank\": 100}}";
            byte[] id = new byte[64];
            /*开启引擎*/
            int rv = AIEngine.aiengine_start(engine, param, id, callback, AiRoomApplication.getInstance());

            Log.d(TAG, "engine start: " + rv);
            Log.d(TAG, "engine param: " + param);
            Log.d(TAG, "id: "+ id );

            int i=0;
            for(;i<id.length;i++)
            {
                if(id[i]=='\0')
                    break;
            }

            String tokenId = new String(id,0,i);
            Log.d(TAG, "token id: "+ tokenId );

//            runOnUiThread(new Runnable() {
//                public void run() {
//                    jsonResultTextEditor.setText("");
//                    waitProgressBar.setVisibility(View.INVISIBLE);
//                    recordButton.setText(R.string.stop);
//                }
//            });
        }

        /*调用引擎停止录音接口*/
        public void onStopped() {
            AIEngine.aiengine_stop(engine);
            waitStartTime = System.currentTimeMillis();
            Log.d(TAG, "engine stopped");
//            runOnUiThread(new Runnable() {
//                public void run() {
//                    recordButton.setText(R.string.record);
//                    waitProgressBar.setVisibility(View.VISIBLE);
//                }
//            });
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
            if (type == AIEngine.AIENGINE_MESSAGE_TYPE_JSON)
            {
                String strId = new String(id);
                Log.d(TAG, "call back token id: " + strId);
                final String result = new String(data, 0, size).trim(); /* must trim the end '\0' */
                try
                {
                    JSONObject json = new JSONObject(result);
                    if (json.has("vad_status") || json.has("volume")) {
                        int status = json.optInt("vad_status");
                        final int volume = json.optInt("volume");
                        if (status == 2) {
                            runOnWorkerThread(new Runnable() {
                                public void run() {
                                    recorder.stop();
                                }
                            });
                        }
                    }else {
                        if (recorder.isRunning()) {
                            recorder.stop();
                        }
                        waitEndTime = System.currentTimeMillis();
                        Log.d(TAG, "wait time for result: " + (waitEndTime - waitStartTime));
                        Log.d(TAG, result);
                        if(iEstimateCallback!=null){
                            iEstimateCallback.onEstimateResult(result);
                        }
                    }
                }
                catch (JSONException e)
                {
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
    public void startRecord(){
        if (engine == 0 || recorder == null) {
            return;
        }
        recorder.start(wavPath, recorderCallback);
    }

    /**
     * 停止录音
     */
    public void stopRecord(){
        if (engine == 0 || recorder == null) {
            return;
        }
        recorder.stop();
    }
    /**
     * 销毁引擎
     */
    public void onDestroy() {
        if (engine != 0) {
            AIEngine.aiengine_delete(engine);
            engine = 0;
            Log.d(TAG, "engine deleted: " + engine);
        }
        if (recorder != null) {
            recorder.stop();
            recorder = null;
        }
    }

}
