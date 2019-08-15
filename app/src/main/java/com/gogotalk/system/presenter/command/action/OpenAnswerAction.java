package com.gogotalk.system.presenter.command.action;

import com.gogotalk.system.model.util.Constant;
import com.gogotalk.system.presenter.ClassRoomContract;
import com.gogotalk.system.presenter.ClassRoomPresenter;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * 开始答题具体实现动作
 */
public class OpenAnswerAction {
    String data;
    ClassRoomContract.IClassRoomView view;
    ClassRoomPresenter presenter;
    public OpenAnswerAction(ClassRoomPresenter presenter,ClassRoomContract.IClassRoomView view, String data) {
        this.data = data;
        this.view = view;
        this.presenter = presenter;
    }

    public void action() {
        JSONObject mObjcet = null;
        try {
            mObjcet = new JSONObject(data);

            if (mObjcet.has("question_id") && !mObjcet.isNull("question_id")) {
                presenter.question_id =  mObjcet.getString("question_id");
            }
            view.sendHandleMessage(Constant.HANDLE_INFO_ANSWER);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
