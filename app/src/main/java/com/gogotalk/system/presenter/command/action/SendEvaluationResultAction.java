package com.gogotalk.system.presenter.command.action;

import com.gogotalk.system.model.entity.SignallingActionBean;
import com.gogotalk.system.model.util.Constant;
import com.gogotalk.system.model.util.GsonUtils;
import com.gogotalk.system.presenter.ClassRoomPresenter;
import com.gogotalk.system.util.AppUtils;
import com.gogotalk.system.util.LogUtil;
import com.gogotalk.system.zego.ZGBaseHelper;
import com.zego.zegoliveroom.callback.IZegoCustomCommandCallback;
import com.zego.zegoliveroom.entity.ZegoUser;
import java.util.HashMap;

/**
 * 向服务器发送驰声sdk评估结果具体实现动作
 */
public class SendEvaluationResultAction {
    String promptId;
    String flag;
    String sessionId;
    String result;
    ZegoUser user;
    ClassRoomPresenter presenter;
    public SendEvaluationResultAction(ClassRoomPresenter presenter,String promptId, String flag, String sessionId,String result,ZegoUser user) {
        this.promptId = promptId;
        this.flag = flag;
        this.sessionId = sessionId;
        this.result = result;
        this.user = user;
        this.presenter = presenter;
    }

    public void action() {
        SignallingActionBean signallingActionBean = new SignallingActionBean();
        signallingActionBean.setAction(Constant.MESSAGE_RESULT);
        signallingActionBean.setRole("student");
        signallingActionBean.setSeq(presenter.seq++);
        HashMap<String, String> resultMap = new HashMap<>();
        resultMap.put("prompt_id", promptId);
        resultMap.put("result", result);
        resultMap.put("session_id", sessionId);
        resultMap.put("user_id", String.valueOf(AppUtils.getUserInfoData().getAccountID()));
        resultMap.put("user_name", String.valueOf(AppUtils.getUserInfoData().getName()));
        signallingActionBean.setData(resultMap);
        String content = GsonUtils.gson.toJson(signallingActionBean);
        LogUtil.e("TAG", "sendEvaluationResult: " + content);
        boolean sendSucess = ZGBaseHelper.sharedInstance().sendCustomCommand(new ZegoUser[]{user}, content, new IZegoCustomCommandCallback() {
            @Override
            public void onSendCustomCommand(int i, String s) {
                LogUtil.e("TAG", "onSendCustomCommand: " + i + "||" + s);
            }
        });
        LogUtil.e("TAG", "sendRoomCommand: 发送信令结果 " + sendSucess);
    }
}
