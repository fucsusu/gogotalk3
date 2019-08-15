package com.gogotalk.system.presenter.command.action;

import com.gogotalk.system.model.util.Constant;
import com.gogotalk.system.presenter.ClassRoomContract;
import com.gogotalk.system.presenter.ClassRoomPresenter;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * 课件跳页具体实现动作
 */
public class NextPageAction {
    String data;
    ClassRoomContract.IClassRoomView view;
    ClassRoomPresenter presenter;
    public NextPageAction(ClassRoomPresenter presenter,ClassRoomContract.IClassRoomView view, String data){
        this.data = data;
        this.view = view;
        this.presenter = presenter;
    }
    public void action(){
        JSONObject mObjcet = null;
        try {
            mObjcet = new JSONObject(data);
            String title = mObjcet.getString("title");
            String msg = title.replace("\\", "");
            JSONObject object1 = new JSONObject(msg);
            int page = object1.getInt("page");
            view.sendHandleMessage(Constant.HANDLE_INFO_NEXTPAGE, 0, page);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
