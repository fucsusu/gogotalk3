package com.gogotalk.system.presenter;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.gogotalk.system.model.util.Constant;
import com.gogotalk.system.util.AppUtils;
import com.gogotalk.system.util.ToastUtils;
import com.gogotalk.system.zego.AppLogger;
import com.gogotalk.system.zego.ZGBaseHelper;
import com.gogotalk.system.zego.ZGMediaSideInfoDemo;
import com.gogotalk.system.zego.ZGPlayHelper;
import com.gogotalk.system.zego.ZGPublishHelper;
import com.gogotalk.system.zego.ZegoUtil;
import com.orhanobut.logger.Logger;
import com.zego.zegoliveroom.ZegoLiveRoom;
import com.zego.zegoliveroom.callback.IZegoCustomCommandCallback;
import com.zego.zegoliveroom.callback.IZegoInitSDKCompletionCallback;
import com.zego.zegoliveroom.callback.IZegoLoginCompletionCallback;
import com.zego.zegoliveroom.callback.IZegoRoomCallback;
import com.zego.zegoliveroom.constants.ZegoConstants;
import com.zego.zegoliveroom.entity.ZegoStreamInfo;
import com.zego.zegoliveroom.entity.ZegoUser;

import org.json.JSONException;
import org.json.JSONObject;

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
    private ZegoUser teacherUser;
    private String teacherStreamID;//老师的流ID
    private double seqNumber = 0;//媒体信息ID

    @Inject
    public ClassRoomPresenter() {
    }

    {
        ownStreamID = String.valueOf(AppUtils.getUserInfoData().getAccountID());
    }

    @Override
    public void initSdk(String roomID, int role) {
        long appId = ZegoUtil.parseAppIDFromString(Constant.appid);
        ZGBaseHelper.sharedInstance().initZegoSDK(appId, appSign, false, new IZegoInitSDKCompletionCallback() {
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

    //发送房间信令
    @Override
    public void sendRoomCommand(Context context, String content) {
        boolean sendSucess = ZGBaseHelper.sharedInstance().sendCustomCommand(new ZegoUser[] {teacherUser,otherUser}, content, new IZegoCustomCommandCallback() {
            @Override
            public void onSendCustomCommand(int i, String s) {
                Log.e("TAG", "onSendCustomCommand: " + i + "||" + s);
            }
        });
        Log.e("TAG", "sendRoomCommand: 发送信令结果 "+sendSucess );
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
            Log.e("TAG", "onRecvCustomCommand: " + id + name + content + roomid);
        }
    };

    ZGMediaSideInfoDemo.RecvMediaSideInfoCallback callback = new ZGMediaSideInfoDemo.RecvMediaSideInfoCallback() {
        @Override
        public void onRecvMediaSideInfo(String streamID, String content) {
            //处理媒体次要信息
            Log.e("TAG", "onRecvMediaSideInfo流ID：" + streamID + "\n媒体次要信息：" + content);
            try {
                JSONObject object = new JSONObject(content);
                String action = object.getString("action");
                String data = object.getString("data");
                int seq = object.getInt("seq");
                if (seq > seqNumber) {
                    seqNumber = seq;
                    if (action.equals("open_answer")) {//调用JS的exec()方法
                        //开始答题
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
                        String title = mObjcet.getString("title");
                        String msg = title.replace("\\", "");
                        JSONObject object1 = new JSONObject(msg);
                        int time = object1.getInt("time");
                        String content1="";
                        String type="";
                        if(object1.has("content")&&!object1.isNull("content")){
                             content1 = object1.getString("content");
                        }
                        if(object1.has("type")&&!object1.isNull("type")){
                            type = object1.getString("type");
                        }
                        getView().sendHandleMessage(content1,type,Constant.HANDLE_INFO_MIKE, 0, time);
                        Log.e("TAG", "调用麦克风发放奖杯方法\nmsg：" + msg + "\ntime：" + time);
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
}
