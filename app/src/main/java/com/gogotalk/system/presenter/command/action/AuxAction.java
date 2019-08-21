package com.gogotalk.system.presenter.command.action;

import android.os.Environment;

import com.gogotalk.system.presenter.ClassRoomContract;
import com.gogotalk.system.presenter.ClassRoomPresenter;
import com.gogotalk.system.util.DelectFileUtil;
import com.gogotalk.system.zego.ZGMediaPlayerDemo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Map;

/**
 * 混音具体实现动作
 */
public class AuxAction {
    String data;
    ClassRoomContract.IClassRoomView view;
    ClassRoomPresenter presenter;
    public AuxAction(ClassRoomPresenter presenter, ClassRoomContract.IClassRoomView view, String data) {
        this.data = data;
        this.view = view;
        this.presenter = presenter;
    }

    public void action() {
        try {
            JSONObject mObjcet = new JSONObject(data);
            String title = mObjcet.getString("title");
            String msg = title.replace("\\", "");
            JSONObject object1 = new JSONObject(msg);
            String userid = object1.getString("userid");
            String username = object1.getString("username");
//            if ("1".equals(username)) {
//                if (!DelectFileUtil.isCoursewareExistence(view.getActivity(), "my.mp3")) {
//                    ZGMediaPlayerDemo.sharedInstance(1)
//                            .startPlay(view.getMyMp3Url(), false);
//                    return;
//                }
//                ZGMediaPlayerDemo.sharedInstance(1)
//                        .startPlay(view.getActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "my.mp3", false);
//            }
//            if ("2".equals(username)) {
//                if (!DelectFileUtil.isCoursewareExistence(view.getActivity(), "other.mp3")) {
//                    ZGMediaPlayerDemo.sharedInstance(1)
//                            .startPlay(view.getOtherMp3Url(), false);
//                    return;
//                }
//                ZGMediaPlayerDemo.sharedInstance(1)
//                        .startPlay(view.getActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "other.mp3", false);
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
