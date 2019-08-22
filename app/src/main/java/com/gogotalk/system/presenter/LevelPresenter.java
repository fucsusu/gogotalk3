package com.gogotalk.system.presenter;

import com.gogotalk.system.model.api.ApiService;
import com.gogotalk.system.model.entity.LevelListBean;
import com.gogotalk.system.model.entity.LevelResultBean;
import com.gogotalk.system.model.entity.QuestionsBean;
import com.gogotalk.system.model.entity.UserInfoBean;
import com.gogotalk.system.model.util.CommonSubscriber;
import com.gogotalk.system.model.util.GsonUtils;
import com.gogotalk.system.model.util.HttpUtils;
import com.gogotalk.system.model.util.RxUtil;
import com.gogotalk.system.util.AppUtils;
import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class LevelPresenter extends RxPresenter<LevelContract.View> implements LevelContract.Presenter {

    private ApiService mApiService;

    @Inject
    public LevelPresenter(ApiService apiService) {
        this.mApiService = apiService;
    }

    @Override
    public void getSurveyQuestion() {
        addSubscribe(mApiService.getSurveyQuestion()
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleMyResult(getView(), false))
                .subscribeWith(new CommonSubscriber<QuestionsBean>(getView(), "") {
                    @Override
                    public void onNext(QuestionsBean o) {
                        getView().showQuestions(o);
                    }
                })
        );
    }

    //定级接口
    @Override
    public void gradeInvestigation(Map<String, String> map) {
        addSubscribe(mApiService.gradeInvestigation(HttpUtils.getRequestBody(map))
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleMyResult(getView(), false))
                .subscribeWith(new CommonSubscriber<LevelResultBean>(getView()) {
                    @Override
                    public void onNext(LevelResultBean bean) {
                        getView().showLevelResult(bean);
                    }
                })
        );
    }

    @Override
    public void updateStudentLevel(int level) {
        addSubscribe(mApiService.updateStudentLevel(level)
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleMyResult(getView(), false))
                .subscribeWith(new CommonSubscriber<Object>(getView()) {
                    @Override
                    public void onNext(Object bean) {
                        UserInfoBean userInfoData = AppUtils.getUserInfoData();
                        userInfoData.setLevel(level);
                        AppUtils.saveUserInfoData(userInfoData);
                        Logger.json(GsonUtils.gson.toJson(bean));
                    }
                })
        );
    }

    @Override
    public void getLeveInfoList() {
        addSubscribe(mApiService.getLeveInfoList()
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleMyResult(getView(), false))
                .subscribeWith(new CommonSubscriber<List<LevelListBean>>(getView()) {
                    @Override
                    public void onNext(List<LevelListBean> beans) {
                        Logger.json(GsonUtils.gson.toJson(beans));
                        getView().onGetLevelListSuccess(beans);
                    }
                })
        );
    }
}
