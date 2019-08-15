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
        void initSdk(Context context, String roomID, int role);

        //进入房间
        void joinRoom(String roomID, int role);

        //开始预览推送自己的流
        void startPreviewOwn(View view);

        //发送答题结果
        void sendAnswerRoomCommand(boolean answerResult);

        //发送收到奖杯结果
        void sendShowJbRoomCommand(int jbNum);

        //发送评估结果
        void sendEvaluationResult(String promptId, String correctResp, String sessionId);

        //获取语音识别结果
        void getAIEngineResult(String type, String content, String promptId, String correctResp, String sessionId);

        //获取当前页数
        void sendGetPageData();
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

        //发送奖杯
        void openOtherJBAnim(int num);

        //获取自己的mp3远程地址
        String getMyMp3Url();

        //获取其他人的mp3远程地址
        String getOtherMp3Url();

        //播放名字MP3地址
        void playNameMp3(String name);

        //跳转页数
        void toPage(int page);
    }
}
