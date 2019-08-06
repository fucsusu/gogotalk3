package com.gogotalk.system.presenter;

import android.content.Context;
import android.view.View;

/**
 * Created by fucc
 * Date: 2019-07-24 10:32
 */
public interface ClassRoomContract {
    interface IClassRoomPresenter extends BaseContract.Presenter<ClassRoomContract.IClassRoomView> {
        //初始化SDK
        void initSdk(String roomID, int role);

        //进入房间
        void joinRoom(String roomID, int role);

        //开始预览推送自己的流
        void startPreviewOwn(View view);

        //发送房间信令
        void sendRoomCommand(Context context, String content);
    }

    interface IClassRoomView extends BaseContract.View {
        //登陆房间成功
        void joinRoomCompletion();

        //老师进入房间
        void teacherJoinRoom(String streamID, String userName);

        //学生进入房间
        void studentJoinRoom(String streamID, String userName);

        //老师离开房间
        void teacherLeaveRoom();

        //学生离开房间
        void studentLeaveRoom();

        //发送Handle信息
        void sendHandleMessage(int... ags);
    }
}
