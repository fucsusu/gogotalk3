package com.gogotalk.system.presenter.command.action;


import com.gogotalk.system.model.entity.SignallingActionBean;
import com.gogotalk.system.model.util.Constant;
import com.gogotalk.system.model.util.GsonUtils;
import com.gogotalk.system.presenter.ClassRoomContract;
import com.gogotalk.system.presenter.ClassRoomPresenter;
import com.gogotalk.system.util.AppUtils;
import com.gogotalk.system.util.LogUtil;
import com.gogotalk.system.zego.ZGBaseHelper;
import com.zego.zegoliveroom.callback.IZegoCustomCommandCallback;
import com.zego.zegoliveroom.entity.ZegoUser;
import java.util.HashMap;

/**
 */
public class SendGetPageDataAction {
    ClassRoomPresenter presenter;
    ZegoUser user;
    public SendGetPageDataAction(ClassRoomPresenter presenter, ZegoUser user) {
        this.presenter = presenter;
        this.user= user;
    }

    public void action() {
        SignallingActionBean signallingActionBean = new SignallingActionBean();
        signallingActionBean.setAction(Constant.MESSAGE_GET_PAGE);
        signallingActionBean.setRole("student");
//        signallingActionBean.setSeq(presenter.seq++);
        HashMap<String, String> resultMap = new HashMap<>();
        resultMap.put("user_id", String.valueOf(AppUtils.getUserInfoData().getAccountID()));
        resultMap.put("user_name", String.valueOf(AppUtils.getUserInfoData().getName()));
        signallingActionBean.setData(resultMap);
        String content = GsonUtils.gson.toJson(signallingActionBean);
        LogUtil.e("TAG", "sendGetPageData: " + content);
        boolean sendSucess = ZGBaseHelper.sharedInstance().sendCustomCommand(new ZegoUser[]{user}, content, new IZegoCustomCommandCallback() {
            @Override
            public void onSendCustomCommand(int i, String s) {
                LogUtil.e("TAG", "sendGetPageData: " + i + "||" + s);
            }
        });
        LogUtil.e("TAG", "sendGetPageData: 发送信令结果 " + sendSucess);
    }
}
