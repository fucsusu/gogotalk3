package com.gogotalk.system.presenter;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.chivox.AIEngineUtils;
import com.gogotalk.system.model.entity.ActionBean;
import com.gogotalk.system.model.util.Constant;
import com.gogotalk.system.model.util.GsonUtils;
import com.gogotalk.system.util.AppUtils;
import com.gogotalk.system.util.DelectFileUtil;
import com.gogotalk.system.zego.ZGBaseHelper;
import com.gogotalk.system.zego.ZGMediaPlayerDemo;
import com.gogotalk.system.zego.ZGMediaSideInfoDemo;
import com.gogotalk.system.zego.ZGPlayHelper;
import com.gogotalk.system.zego.ZGPublishHelper;
import com.gogotalk.system.zego.ZegoUtil;
import com.orhanobut.logger.Logger;
import com.zego.zegoavkit2.soundlevel.IZegoSoundLevelCallback;
import com.zego.zegoavkit2.soundlevel.ZegoSoundLevelInfo;
import com.zego.zegoavkit2.soundlevel.ZegoSoundLevelMonitor;
import com.zego.zegoliveroom.ZegoLiveRoom;
import com.zego.zegoliveroom.callback.IZegoAudioRecordCallback2;
import com.zego.zegoliveroom.callback.IZegoCustomCommandCallback;
import com.zego.zegoliveroom.callback.IZegoInitSDKCompletionCallback;
import com.zego.zegoliveroom.callback.IZegoLoginCompletionCallback;
import com.zego.zegoliveroom.callback.IZegoRoomCallback;
import com.zego.zegoliveroom.constants.ZegoConstants;
import com.zego.zegoliveroom.entity.ZegoStreamInfo;
import com.zego.zegoliveroom.entity.ZegoUser;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import javax.inject.Inject;
import static com.gogotalk.system.model.util.Constant.appSign;

/**
 * Created by fucc
 * Date: 2019-07-24 10:35
 */
public class ClassRoomPresenter extends RxPresenter<ClassRoomContract.IClassRoomView> implements ClassRoomContract.IClassRoomPresenter {
    private ZegoUser otherUser;
    private String otherStreamID;//其他学生的流ID
    private String ownStreamID;//自己推流ID
    private String ownUserName;
    private ZegoUser teacherUser;
    private String teacherStreamID;//老师的流ID
    private double seqNumber = 0;//媒体信息ID
    private int seq = 1;
    //麦克风信令回复
    private String prompt_id;
    private String correct_resp;
    //答题回调
    private String question_id;
    private String roomId;

    @Inject
    public ClassRoomPresenter() {
    }

    {
        ownStreamID = String.valueOf(AppUtils.getUserInfoData().getAccountID());
        ownUserName = String.valueOf(AppUtils.getUserInfoData().getName());
    }


    @Override
    public void initSdk(String roomID, int role) {
        long appId = ZegoUtil.parseAppIDFromString(Constant.appid);
        ZGBaseHelper.sharedInstance().initZegoSDK(appId, appSign, true, new IZegoInitSDKCompletionCallback() {
            @Override
            public void onInitSDK(int errorCode) {
                // errorCode 非0 代表初始化sdk失败
                // 具体错误码说明请查看<a> https://doc.zego.im/CN/308.html </a>
                if (errorCode == 0) {
                    Logger.e("初始化zegoSDK成功");
                    ZegoLiveRoom.setUser(String.valueOf(ownStreamID), AppUtils.getUserInfoData().getName());
                    ZGMediaSideInfoDemo.sharedInstance().activateMediaSideInfoForPublishChannel(false, 0);
                    ZGMediaSideInfoDemo.sharedInstance().setUseCutomPacket(false);
                    ZGMediaSideInfoDemo.sharedInstance().setMediaSideInfoCallback(callback);
                    ZGBaseHelper.sharedInstance().setAudioCallbcak(audioRecordCallback2);
                    ZegoSoundLevelMonitor.getInstance().setCallback(zegoSoundLevelCallback);
                    ZGBaseHelper.sharedInstance().setZegoRoomCallback(roomCallback);
                    joinRoom(roomID, role);
                } else {
                    Logger.e("初始化sdk失败 错误码 :", errorCode);
                }
            }
        });
    }

    @Override
    public void joinRoom(String roomID, int role) {
        roomId = roomID;
        // 登陆房间
        ZGBaseHelper.sharedInstance().loginRoom(roomID, role, new IZegoLoginCompletionCallback() {
            @Override
            public void onLoginCompletion(int errorCode, ZegoStreamInfo[] zegoStreamInfos) {
                if (errorCode == 0) {
                    Log.e("TAG", "登录房间成功准备推流..." + roomID + "||" + role);
                    getView().joinRoomCompletion();
                    //获取教室内的流
                    for (ZegoStreamInfo info : zegoStreamInfos) {
                        if (info.streamID.contains(Constant.FLAG_TEACHER)) {//老师
                            teacherUser = new ZegoUser();
                            teacherUser.userID = info.userID;
                            teacherUser.userName = info.userName;
                            teacherStreamID = info.streamID;
                            getView().teacherJoinRoom(info.streamID, info.userName);
                        } else {
                            otherUser = new ZegoUser();
                            otherUser.userID = info.userID;
                            otherUser.userName = info.userName;
                            otherStreamID = info.streamID;
                            getView().studentJoinRoom(info.streamID, info.userName);
                        }
                        Log.e("TAG", "房间用户信息：\nstreamID:" + info.streamID + "\nextraInfo:" + info.extraInfo
                                + "\nuserID:" + info.userID + "\nuserName:" + info.userName);
                    }

                } else {
                    Log.e("TAG", "登陆房间失败..." + errorCode);
                }
            }
        });
    }

    @Override
    public void startPreviewOwn(View view) {
        // 预览自己的视频且推流
        ZGPublishHelper.sharedInstance().startPreview(view, ownStreamID);

    }


    //发送答题结果
    @Override
    public void sendAnswerRoomCommand(boolean answerResult) {
        if (!TextUtils.isEmpty(question_id)) {
            ActionBean.ActionData actionData = new ActionBean.ActionData();
            actionData.setAnswer(answerResult);
            actionData.setQuestion_id(question_id);
            actionData.setUser_id(ownStreamID);
            actionData.setUser_name(ownUserName);
            ActionBean actionBean = new ActionBean(seq++, "student", "answer", actionData);
            String content = GsonUtils.gson.toJson(actionBean);
            boolean sendSucess = ZGBaseHelper.sharedInstance().sendCustomCommand(new ZegoUser[]{teacherUser, otherUser}, content, new IZegoCustomCommandCallback() {
                @Override
                public void onSendCustomCommand(int i, String s) {
                    Log.e("TAG", "onSendCustomCommand: " + i + "||" + s);
                }
            });
            Log.e("TAG", "sendRoomCommand: 发送信令结果 " + sendSucess);
        }
        question_id = "";
    }

    //发送展示奖杯
    @Override
    public void sendShowJbRoomCommand(int jbNum) {
        ActionBean.ActionData actionData = new ActionBean.ActionData();
        actionData.setJb_num(jbNum);
        actionData.setUser_id(ownStreamID);
        actionData.setUser_name(ownUserName);
        ActionBean actionBean = new ActionBean(seq++, "student", Constant.MESSAGE_SHOW_JB, actionData);
        String content = GsonUtils.gson.toJson(actionBean);
        boolean sendSucess = ZGBaseHelper.sharedInstance().sendCustomCommand(new ZegoUser[]{otherUser}, content, new IZegoCustomCommandCallback() {
            @Override
            public void onSendCustomCommand(int i, String s) {
                Log.e("TAG", "onSendCustomCommand: " + i + "||" + s);
            }
        });
        Log.e("TAG", "sendRoomCommand: 发送信令结果 " + sendSucess);
    }

    @Override
    public void sendEvaluationResult(String promptId, String correctResp, String sessionId) {
        ActionBean.ActionData actionData = new ActionBean.ActionData();
        actionData.setUser_id(ownStreamID);
        actionData.setUser_name(ownUserName);
        actionData.setPrompt_id(promptId);
        actionData.setResult(correctResp);
        actionData.setSession_id(sessionId);
        ActionBean actionBean = new ActionBean(seq++, "student", "result", actionData);
        String content = GsonUtils.gson.toJson(actionBean);
        Logger.json(content);
        boolean sendSucess = ZGBaseHelper.sharedInstance().sendCustomCommand(new ZegoUser[]{teacherUser}, content, new IZegoCustomCommandCallback() {
            @Override
            public void onSendCustomCommand(int i, String s) {
                Log.e("TAG", "onSendCustomCommand: " + i + "||" + s);
            }
        });
        Log.e("TAG", "sendRoomCommand: 发送信令结果 " + sendSucess);
    }

    @Override
    public void detachView() {
        ZGPlayHelper.sharedInstance().stopPlaying(teacherStreamID);
        ZGPlayHelper.sharedInstance().stopPlaying(otherStreamID);
        super.detachView();
    }

    IZegoRoomCallback roomCallback = new IZegoRoomCallback() {
        @Override
        public void onKickOut(int i, String s) {

        }

        @Override
        public void onDisconnect(int i, String s) {

        }

        @Override
        public void onReconnect(int i, String s) {

        }

        @Override
        public void onTempBroken(int i, String s) {

        }

        @Override
        public void onStreamUpdated(int type, ZegoStreamInfo[] zegoStreamInfos, String s) {

            //流变动
            for (ZegoStreamInfo streamInfo : zegoStreamInfos) {
                if (type == ZegoConstants.StreamUpdateType.Added) {
                    if (streamInfo.streamID.contains(Constant.FLAG_TEACHER)) {//老师
                        teacherUser = new ZegoUser();
                        teacherUser.userID = streamInfo.userID;
                        teacherUser.userName = streamInfo.userName;
                        teacherStreamID = streamInfo.streamID;
                        getView().teacherJoinRoom(streamInfo.streamID, streamInfo.userName);
                    } else {//学生
                        otherUser = new ZegoUser();
                        otherUser.userID = streamInfo.userID;
                        otherUser.userName = streamInfo.userName;
                        otherStreamID = streamInfo.streamID;
                        getView().studentJoinRoom(streamInfo.streamID, streamInfo.userName);
                    }
                } else if (type == ZegoConstants.StreamUpdateType.Deleted) {
                    if (streamInfo.streamID.contains(Constant.FLAG_TEACHER)) {//老师
                        getView().teacherLeaveRoom();
                        teacherStreamID = "";
                    } else if (streamInfo.streamID.equals(otherStreamID)) {//学生
                        getView().studentLeaveRoom();
                        otherStreamID = "";
                    }
                }
            }
        }

        @Override
        public void onStreamExtraInfoUpdated(ZegoStreamInfo[] zegoStreamInfos, String s) {

        }

        @Override
        public void onRecvCustomCommand(String id, String name, String content, String roomid) {
            //收到教室信令
            Log.v("TAG", "onRecvCustomCommand: " + id + name + content + roomid);
            if (roomid.equals(roomId)) {
                ActionBean actionBean = GsonUtils.gson.fromJson(content, ActionBean.class);
                switch (actionBean.getAction()) {
                    case Constant.MESSAGE_SHOW_JB:
                        getView().openOtherJBAnim(actionBean.getData().getJb_num());
                        break;
                }
            }


        }
    };

    ZGMediaSideInfoDemo.RecvMediaSideInfoCallback callback = new ZGMediaSideInfoDemo.RecvMediaSideInfoCallback() {
        @Override
        public void onRecvMediaSideInfo(String streamID, String content) {
            //处理媒体次要信息
            Log.v("TAG", "onRecvMediaSideInfo流ID：" + streamID + "\n媒体次要信息：" + content);
            try {
                JSONObject object = new JSONObject(content);
                String action = object.getString("action");
                String data = object.getString("data");
                int seq = object.getInt("seq");
                if (seq > seqNumber) {
                    seqNumber = seq;
                    if (action.equals("aux")) {
                        JSONObject mObjcet = new JSONObject(data);
                        String title = mObjcet.getString("title");
                        String msg = title.replace("\\", "");
                        JSONObject object1 = new JSONObject(msg);
                        String userid = object1.getString("userid");
                        String username = object1.getString("username");
                        if ("1".equals(username)) {
                            if(!DelectFileUtil.isCoursewareExistence(getView().getActivity(),"my.mp3")){
                                ZGMediaPlayerDemo.sharedInstance()
                                        .startPlay(getView().getMyMp3Url(), false);
                                return;
                            }
                            ZGMediaPlayerDemo.sharedInstance()
                                    .startPlay(getView().getActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "my.mp3", false);
                            return;
                        }
                        if ("2".equals(username)) {
                            if(!DelectFileUtil.isCoursewareExistence(getView().getActivity(),"other.mp3")){
                                ZGMediaPlayerDemo.sharedInstance()
                                        .startPlay(getView().getOtherMp3Url(), false);
                                return;
                            }
                            ZGMediaPlayerDemo.sharedInstance()
                                    .startPlay(getView().getActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "other.mp3", false);
                            return;
                        }
                        ZGMediaPlayerDemo.sharedInstance().unInit();
                    }
                    if (action.equals("open_answer")) {//调用JS的exec()方法
                        //开始答题
                        JSONObject mObjcet = new JSONObject(data);
                        if (mObjcet.has("question_id") && !mObjcet.isNull("question_id")) {
                            question_id = mObjcet.getString("question_id");
                        }
                        getView().sendHandleMessage(Constant.HANDLE_INFO_ANSWER);
                    }
                    if (action.equals("next_page")) {//调用JS的PageDown()方法
                        JSONObject mObjcet = new JSONObject(data);
                        String title = mObjcet.getString("title");
                        String msg = title.replace("\\", "");
                        JSONObject object1 = new JSONObject(msg);
                        int page = object1.getInt("page");
                        getView().sendHandleMessage(Constant.HANDLE_INFO_NEXTPAGE, 0, page);
                    }
                    if (action.equals("open_mic")) {//打开麦克风，倒计时6秒
                        JSONObject mObjcet = new JSONObject(data);
                        String sessionId = mObjcet.getString("session_id");
                        String title = mObjcet.getString("title");
                        String msg = title.replace("\\", "");
                        JSONObject object1 = new JSONObject(msg);
                        int time = object1.getInt("time");
                        String content1 = "";
                        String type = "";

                        if (object1.has("content") && !object1.isNull("content")) {
                            content1 = object1.getString("content");
                        }
                        if (object1.has("type") && !object1.isNull("type")) {
                            type = object1.getString("type");
                        }
                        if (object1.has("prompt_id") && !object1.isNull("prompt_id")) {
                            prompt_id = object1.getString("prompt_id");
                        }
                        if (object1.has("correct_resp") && !object1.isNull("correct_resp")) {
                            correct_resp = object1.getString("correct_resp");
                        }

                        Log.e("TAG", "onRecvMediaSideInfo: " + content1 + type);
                        if (!TextUtils.isEmpty(content1) && !TextUtils.isEmpty(type)) {
                            getAIEngineResult(type, content1, prompt_id, correct_resp, sessionId);
                        }
                        getView().sendHandleMessage(Constant.HANDLE_INFO_MIKE, 0, time);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onRecvMixStreamUserData(String streamID, String content) {

        }
    };

    IZegoSoundLevelCallback zegoSoundLevelCallback = new IZegoSoundLevelCallback() {

        //拉流的声音变化
        @Override
        public void onSoundLevelUpdate(ZegoSoundLevelInfo[] zegoSoundLevelInfos) {

        }

        //推流声音变化
        @Override
        public void onCaptureSoundLevelUpdate(ZegoSoundLevelInfo zegoSoundLevelInfo) {
            getView().sendHandleMessage(Constant.HANDLE_INFO_VOICE, 0, (int) zegoSoundLevelInfo.soundLevel);
        }
    };

    public void getAIEngineResult(String type, String content, String promptId, String correctResp, String sessionId) {
        ZGBaseHelper.sharedInstance().startAudioRecord();
        AIEngineUtils.getInstance()
                .setUserId(ownStreamID)
                .setType(type)
                .setContent(content)
                .setiEstimateCallback(new AIEngineUtils.IEstimateCallback() {
                    @Override
                    public void onEstimateResult(String result, int rank) {
                        try {
                            //解析驰声sdk评估结果 overall
                            JSONObject jsonObject = new JSONObject(result);
                            String result1 = jsonObject.getString("result");
                            if (TextUtils.isEmpty(result1)) {
                                return;
                            }
                            JSONObject jsonObject1 = new JSONObject(result1);
                            JSONObject params = jsonObject.getJSONObject("params");
                            JSONObject request = params.getJSONObject("request");

                            int overall = jsonObject1.getInt("overall");
                            String flag = "";
                            int jbNum=0;
                            if (overall > 60) {
                                jbNum = 2;
                                flag = correctResp;
                            } else if (overall > 0 && overall < 60) {
                                jbNum = 1;
                                flag = correctResp;
                            } else {
                                flag = "";
                            }
                            //如果奖杯数大于零发送奖杯
                            if(jbNum>0){
                                getView().sendHandleMessage(Constant.HANDLE_INFO_JB, 0, jbNum);
                            }
                            //发送评估结果信令
                            sendEvaluationResult(promptId, flag, sessionId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .startRecord();
    }

    //录制声音回调
    IZegoAudioRecordCallback2 audioRecordCallback2 = new IZegoAudioRecordCallback2() {
        @Override
        public void onAudioRecordCallback(byte[] bytes, int i, int i1, int i2, int i3) {
            Log.e("TAG", "onAudioRecordCallback: " + bytes.length);
            AIEngineUtils.getInstance().writeAudioData(bytes);
        }
    };


}

