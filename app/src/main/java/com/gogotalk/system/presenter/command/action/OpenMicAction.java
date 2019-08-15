package com.gogotalk.system.presenter.command.action;

import android.text.TextUtils;
import android.util.Log;

import com.gogotalk.system.model.util.Constant;
import com.gogotalk.system.presenter.ClassRoomContract;
import com.gogotalk.system.presenter.ClassRoomPresenter;
import com.zego.zegoliveroom.entity.ZegoUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
/**
 * 打开麦克风具体实现动作
 */
public class OpenMicAction {
    String data;
    ClassRoomContract.IClassRoomView view;
    ZegoUser user;
    ClassRoomPresenter presenter;
    public OpenMicAction(ClassRoomPresenter presenter,ClassRoomContract.IClassRoomView view, String data,ZegoUser user) {
        this.data = data;
        this.view = view;
        this.user = user;
        this.presenter = presenter;
    }

    public void action() {
        try {
            JSONObject mObjcet = new JSONObject(data);
            String sessionId = mObjcet.getString("session_id");
            String title = mObjcet.getString("title");
            String msg = title.replace("\\", "");
            JSONObject object1 = new JSONObject(msg);
            int time = object1.getInt("time");
            String content1 = "";
            String type = "";
            String prompt_id = "";
            String correct_resp = "";
            if (object1.has("content") && !object1.isNull("content")) {
                content1 = object1.getString("content");
            }
            if (object1.has("type") && !object1.isNull("type")) {
                type = object1.getString("type");
            }
            if (object1.has("prompt_id") && !object1.isNull("prompt_id")) {
                presenter.prompt_id = object1.getString("prompt_id");
            }
            if (object1.has("correct_resp") && !object1.isNull("correct_resp")) {
                presenter.correct_resp = object1.getString("correct_resp");
            }
            Log.e("TAG", "onRecvMediaSideInfo: " + content1 + type);
            if (!TextUtils.isEmpty(content1) && !TextUtils.isEmpty(type)) {
                ReceiveEvaluationResultAction aiEnginResultAction = new ReceiveEvaluationResultAction(presenter,view,type, content1, prompt_id, correct_resp, sessionId,user);
                aiEnginResultAction.action();
            }
            view.sendHandleMessage(Constant.HANDLE_INFO_MIKE, 0, time);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
