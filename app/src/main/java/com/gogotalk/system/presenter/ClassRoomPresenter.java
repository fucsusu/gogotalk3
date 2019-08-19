package com.gogotalk.system.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.chivox.AIEngineUtils;
import com.gogotalk.system.model.entity.ActionBean;
import com.gogotalk.system.model.entity.SignallingActionBean;
import com.gogotalk.system.model.util.Constant;
import com.gogotalk.system.model.util.GsonUtils;
import com.gogotalk.system.util.AppUtils;
import com.gogotalk.system.util.LogUtil;
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

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import static com.gogotalk.system.model.util.Constant.ACTION_ANSWER;
import static com.gogotalk.system.model.util.Constant.ACTION_AUX;
import static com.gogotalk.system.model.util.Constant.ACTION_NEXTPAGE;
import static com.gogotalk.system.model.util.Constant.ACTION_OPENMIKE;
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
    Context context;

    @Inject
    public ClassRoomPresenter() {
    }

    {
        ownStreamID = String.valueOf(AppUtils.getUserInfoData().getAccountID());
        ownUserName = String.valueOf(AppUtils.getUserInfoData().getName());
        ZGMediaPlayerDemo.sharedInstance().setVolume(80);
    }


    @Override
    public void initSdk(Context context, String roomID, int role) {
        this.context = context;
        long appId = ZegoUtil.parseAppIDFromString(Constant.appid);
        ZGBaseHelper.sharedInstance().initZegoSDK(appId, appSign, Constant.DEBUG, new IZegoInitSDKCompletionCallback() {
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
                    LogUtil.e("TAG", "登录房间成功准备推流..." + roomID + "||" + role, zegoStreamInfos.length);
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
                        LogUtil.e("TAG", "房间用户信息：\nstreamID:" + info.streamID + "\nextraInfo:" + info.extraInfo
                                + "\nuserID:" + info.userID + "\nuserName:" + info.userName);
                    }

                } else {
                    LogUtil.e("TAG", "登陆房间失败..." + errorCode);
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
            SignallingActionBean signallingActionBean = new SignallingActionBean();
            signallingActionBean.setAction(Constant.MESSAGE_ANSWER);
            signallingActionBean.setRole("student");
            signallingActionBean.setSeq(seq++);
            HashMap<String, String> resultMap = new HashMap<>();
            resultMap.put("question_id", question_id);
            resultMap.put("user_id", ownStreamID);
            resultMap.put("user_name", ownUserName);
            resultMap.put("result", answerResult + "");
            signallingActionBean.setData(resultMap);
            String content = GsonUtils.gson.toJson(signallingActionBean);
            LogUtil.e("TAG", "sendAnswerRoomCommand: " + content);
            boolean sendSucess = ZGBaseHelper.sharedInstance().sendCustomCommand(new ZegoUser[]{teacherUser}, content, new IZegoCustomCommandCallback() {
                @Override
                public void onSendCustomCommand(int i, String s) {
                    LogUtil.e("TAG", "sendAnswerRoomCommand: " + i + "||" + s);
                }
            });
            LogUtil.e("TAG", "sendAnswerRoomCommand: 发送信令结果 " + sendSucess);
        }
        question_id = "";
    }

    //发送展示奖杯
    @Override
    public void sendShowJbRoomCommand(int jbNum) {
        SignallingActionBean signallingActionBean = new SignallingActionBean();
        signallingActionBean.setAction(Constant.MESSAGE_SHOW_JB);
        signallingActionBean.setRole("student");
        signallingActionBean.setSeq(seq++);
        HashMap<String, String> resultMap = new HashMap<>();
        resultMap.put("jb_num", jbNum + "");
        resultMap.put("user_id", ownStreamID);
        resultMap.put("user_name", ownUserName);
        signallingActionBean.setData(resultMap);
        String content = GsonUtils.gson.toJson(signallingActionBean);
        LogUtil.e("TAG", "sendShowJbRoomCommand: ", content);
        boolean sendSucess = ZGBaseHelper.sharedInstance().sendCustomCommand(new ZegoUser[]{otherUser}, content, new IZegoCustomCommandCallback() {
            @Override
            public void onSendCustomCommand(int i, String s) {
                LogUtil.e("TAG", "sendShowJbRoomCommand: " + i + "||" + s);
            }
        });
        LogUtil.e("TAG", "sendShowJbRoomCommand: 发送信令结果 " + sendSucess);
    }

    //发送语音判断结果
    @Override
    public void sendEvaluationResult(String promptId, String correctResp, String sessionId) {
        SignallingActionBean signallingActionBean = new SignallingActionBean();
        signallingActionBean.setAction(Constant.MESSAGE_RESULT);
        signallingActionBean.setRole("student");
        signallingActionBean.setSeq(seq++);
        HashMap<String, String> resultMap = new HashMap<>();
        resultMap.put("prompt_id", promptId);
        resultMap.put("result", correctResp);
        resultMap.put("session_id", sessionId);
        resultMap.put("user_id", ownStreamID);
        resultMap.put("user_name", ownUserName);
        signallingActionBean.setData(resultMap);
        String content = GsonUtils.gson.toJson(signallingActionBean);
        LogUtil.e("TAG", "sendEvaluationResult: " + content);
        boolean sendSucess = ZGBaseHelper.sharedInstance().sendCustomCommand(new ZegoUser[]{teacherUser}, content, new IZegoCustomCommandCallback() {
            @Override
            public void onSendCustomCommand(int i, String s) {
                LogUtil.e("TAG", "onSendCustomCommand: " + i + "||" + s);
            }
        });
        LogUtil.e("TAG", "sendRoomCommand: 发送信令结果 " + sendSucess);

    }

    //发送获取页数信令
    public void sendGetPageData() {
        SignallingActionBean signallingActionBean = new SignallingActionBean();
        signallingActionBean.setAction(Constant.MESSAGE_GET_PAGE);
        signallingActionBean.setRole("student");
        signallingActionBean.setSeq(seq++);
        HashMap<String, String> resultMap = new HashMap<>();
        resultMap.put("user_id", ownStreamID);
        resultMap.put("user_name", ownUserName);
        signallingActionBean.setData(resultMap);
        String content = GsonUtils.gson.toJson(signallingActionBean);
        LogUtil.e("TAG", "sendGetPageData: " + content);
        boolean sendSucess = ZGBaseHelper.sharedInstance().sendCustomCommand(new ZegoUser[]{teacherUser}, content, new IZegoCustomCommandCallback() {
            @Override
            public void onSendCustomCommand(int i, String s) {
                LogUtil.e("TAG", "sendGetPageData: " + i + "||" + s);
            }
        });
        LogUtil.e("TAG", "sendGetPageData: 发送信令结果 " + sendSucess);
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
            Log.e("TAG", "onRecvCustomCommand: " + id + name + content + roomid);
            if (roomid.equals(roomId)) {
                SignallingActionBean actionBean = GsonUtils.gson.fromJson(content, SignallingActionBean.class);
                switch (actionBean.getAction()) {
                    case Constant.MESSAGE_SHOW_JB:
                        if (!TextUtils.isEmpty(actionBean.getData().get("jb_num"))) {
                            getView().openOtherJBAnim(Integer.parseInt(actionBean.getData().get("jb_num")));
                        }
                        break;
                    case Constant.MESSAGE_GET_PAGE:
                        if (!TextUtils.isEmpty(actionBean.getData().get("pageindex"))) {
                            getView().toPage(Integer.parseInt(actionBean.getData().get("pageindex")));
                        }
                        break;
                }
            }
        }
    };

    ZGMediaSideInfoDemo.RecvMediaSideInfoCallback callback = new ZGMediaSideInfoDemo.RecvMediaSideInfoCallback() {
        @Override
        public void onRecvMediaSideInfo(String streamID, String content) {
            //处理媒体次要信息
            Log.e("TAG", "onRecvMediaSideInfo流ID：" + streamID + "\n媒体次要信息：" + content);
            ActionBean actionBean = GsonUtils.gson.fromJson(content, ActionBean.class);
            Map map = null;
            if (!TextUtils.isEmpty(actionBean.getData().getTitle()) && !actionBean.getData().getTitle().equals("\"\"")) {
                map = GsonUtils.gson.fromJson(actionBean.getData().getTitle(), Map.class);
            }
            if (actionBean.getSeq() > seqNumber) {
                seqNumber = actionBean.getSeq();
                switch (actionBean.getAction()) {
                    case ACTION_AUX:
                        getView().playNameMp3((String) map.get("username"));
                        break;
                    case ACTION_ANSWER:
                        getView().sendHandleMessage(Constant.HANDLE_INFO_ANSWER);
                        break;
                    case ACTION_NEXTPAGE:
                        int page = (int) Double.parseDouble(String.valueOf(map.get("page")));
                        getView().sendHandleMessage(Constant.HANDLE_INFO_NEXTPAGE, 0, page);
                        break;
                    case ACTION_OPENMIKE:
                        prompt_id = (String) map.get("prompt_id");
                        correct_resp = (String) map.get("correct_resp");
                        getAIEngineResult((String) map.get("type"), (String) map.get("content"),
                                prompt_id, correct_resp, actionBean.getData().getSession_id());
                        getView().sendHandleMessage(Constant.HANDLE_INFO_MIKE, 0, (int) Double.parseDouble(String.valueOf(map.get("time"))));
                        break;
                }
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
        if (TextUtils.isEmpty(type) || TextUtils.isEmpty(content) || TextUtils.isEmpty(promptId) || TextUtils.isEmpty(correctResp)) {
            return;
        }
        ZGBaseHelper.sharedInstance().startAudioRecord();
        AIEngineUtils.getInstance()
                .setUserId(ownStreamID)
                .setType(type)
                .setContent(content)
                .setiEstimateCallback(new AIEngineUtils.IEstimateCallback() {
                    @Override
                    public void onEstimateResult(int overall, int rank) {
                        //解析驰声sdk评估结果 overall
                        String flag = "";
                        int jbNum = 0;
                        if (overall > 80) {
                            jbNum = 3;
                            flag = correctResp;
                        } else if (overall > 50) {
                            jbNum = 2;
                            flag = correctResp;
                        } else if (overall > 10) {
                            jbNum = 1;
                            flag = correctResp;
                        } else {
                            flag = "";
                        }
                        LogUtil.e("TAG", "onEstimateResult: ", rank, overall);
                        //如果奖杯数大于零发送奖杯
                        getView().sendHandleMessage(Constant.HANDLE_INFO_JB, 0, jbNum);
                        //发送评估结果信令
                        sendEvaluationResult(promptId, flag, sessionId);
                    }
                })
                .startRecord(context);
    }

    //录制声音回调
    IZegoAudioRecordCallback2 audioRecordCallback2 = new IZegoAudioRecordCallback2() {
        @Override
        public void onAudioRecordCallback(byte[] bytes, int i, int i1, int i2, int i3) {
            Log.d("TAG", "onAudioRecordCallback: " + bytes.length);
            AIEngineUtils.getInstance().writeAudioData(bytes);
        }
    };


}

