package com.gogotalk.system.presenter.command.action;

import android.text.TextUtils;

import com.chivox.AIEngineUtils;
import com.gogotalk.system.model.util.Constant;
import com.gogotalk.system.presenter.ClassRoomContract;
import com.gogotalk.system.presenter.ClassRoomPresenter;
import com.gogotalk.system.util.AppUtils;
import com.gogotalk.system.util.LogUtil;
import com.gogotalk.system.zego.ZGBaseHelper;
import com.zego.zegoliveroom.entity.ZegoUser;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * 处理驰声sdk评测结果具体实现动作
 */
public class ReceiveEvaluationResultAction {
    ClassRoomContract.IClassRoomView view;
    String type;
    String content;
    String promptId;
    String correctResp;
    String sessionId;
    ZegoUser user;
    ClassRoomPresenter presenter;
    public ReceiveEvaluationResultAction(ClassRoomPresenter presenter,ClassRoomContract.IClassRoomView view, String type, String content, String promptId, String correctResp, String sessionId, ZegoUser user) {
        this.view = view;
        this.type = type;
        this.content = content;
        this.promptId = promptId;
        this.correctResp = correctResp;
        this.sessionId = sessionId;
        this.user = user;
        this.presenter = presenter;
    }

    public void action() {
//        ZGBaseHelper.sharedInstance().startAudioRecord();
//        AIEngineUtils.getInstance()
//                .setUserId(String.valueOf(AppUtils.getUserInfoData().getAccountID()))
//                .setType(type)
//                .setContent(content)
//                .setiEstimateCallback(new AIEngineUtils.IEstimateCallback() {
//                    @Override
//                    public void onEstimateResult(String result, int rank) {
//                        try {
//                            //解析驰声sdk评估结果 overall
//                            JSONObject jsonObject = new JSONObject(result);
//                            String result1 = jsonObject.getString("result");
//                            if (TextUtils.isEmpty(result1)) {
//                                return;
//                            }
//                            JSONObject jsonObject1 = new JSONObject(result1);
//                            JSONObject params = jsonObject.getJSONObject("params");
//                            JSONObject request = params.getJSONObject("request");
//
//                            int overall = jsonObject1.getInt("overall");
//                            String flag = "";
//                            int jbNum = 0;
//                            if (overall > 60) {
//                                jbNum = 2;
//                                flag = correctResp;
//                            } else if (overall > 0 && overall < 60) {
//                                jbNum = 1;
//                                flag = correctResp;
//                            } else {
//                                flag = "";
//                            }
//                            LogUtil.e("TAG", "onEstimateResult: ", rank, overall);
//                            //如果奖杯数大于零发送奖杯
//                            view.sendHandleMessage(Constant.HANDLE_INFO_JB, 0, jbNum);
//                            //发送评估结果信令
//                            SendEvaluationResultAction sendEvaluationResultAction = new SendEvaluationResultAction(presenter,promptId, flag, sessionId,correctResp,user);
//                            sendEvaluationResultAction.action();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                })
//                .startRecord(view.getActivity());
    }
}
