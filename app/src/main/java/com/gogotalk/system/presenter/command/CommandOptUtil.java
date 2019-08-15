package com.gogotalk.system.presenter.command;

import com.gogotalk.system.presenter.ClassRoomContract;
import com.gogotalk.system.presenter.ClassRoomPresenter;
import com.gogotalk.system.presenter.command.action.AuxAction;
import com.gogotalk.system.presenter.command.action.NextPageAction;
import com.gogotalk.system.presenter.command.action.OpenAnswerAction;
import com.gogotalk.system.presenter.command.action.OpenMicAction;
import com.zego.zegoliveroom.entity.ZegoUser;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * 信令处理工具类
 */
public class CommandOptUtil {
    /**
     * 处理解析信令操作
     *
     * @param user
     */
    public static void receiveCommandOpt(ClassRoomPresenter presenter, String content, ClassRoomContract.IClassRoomView view, ZegoUser user) {
        try {
            JSONObject object = new JSONObject(content);
            String action = object.getString("action");
            String data = object.getString("data");
            int seq = object.getInt("seq");
            if (seq > presenter.seqNumber) {
                presenter.seqNumber = seq;
                Command command = null;
                if ("next_page".equals(action)) {
                    command = new NextPageCommandImpl(new NextPageAction(presenter,view, data));
                } else if ("aux".equals(action)) {
                    command = new AuxCommandImpl(new AuxAction(presenter,view, data));
                } else if ("open_mic".equals(action)) {
                    command = new OpenMicCommandImpl(new OpenMicAction(presenter, view, data, user));
                } else if ("open_answer".equals(action)) {
                    command = new OpenAnswerCommandImpl(new OpenAnswerAction(presenter,view, data));
                }
                command.execute();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 主动发送信令操作
     */
    public static void sendCommandOpt(Command command) {
        command.execute();
    }
}
