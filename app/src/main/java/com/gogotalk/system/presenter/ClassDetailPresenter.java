package com.gogotalk.system.presenter;


import com.gogotalk.system.model.api.ApiService;
import com.gogotalk.system.model.entity.ClassDetailBean;
import com.gogotalk.system.model.entity.ResponseModel;
import com.gogotalk.system.model.entity.WeekMakeBean;
import com.gogotalk.system.model.util.CommonSubscriber;
import com.gogotalk.system.model.util.RxUtil;

import java.util.List;

import javax.inject.Inject;

public class ClassDetailPresenter extends RxPresenter<ClassDetailContract.View> implements ClassDetailContract.Presenter {

    private ApiService mApiService;

    @Inject
    public ClassDetailPresenter(ApiService apiService) {
        this.mApiService = apiService;
    }


    @Override
    public void getClassDetailData(String id) {
        addSubscribe(mApiService.getClassDetailData(id)
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleMyResult(getView(), false))
                .subscribeWith(new CommonSubscriber<ClassDetailBean>(getView()) {
                    @Override
                    public void onNext(ClassDetailBean bean) {
                        getView().setClassDetailDataToView(bean);
                    }
                })
        );
    }

    @Override
    public void getWeekMakeBean() {
        addSubscribe(mApiService.getWeekMakeBean()
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleMyResult(getView()))
                .subscribeWith(new CommonSubscriber<ResponseModel<List<WeekMakeBean>>>(getView()) {
                    @Override
                    public void onNext(ResponseModel<List<WeekMakeBean>> beans) {
                        getView().setDataToYuyueDialogShow(beans);
                    }
                })
        );
    }

    @Override
    public void orderClass(int bookID, int chapterID, String lessonTime) {
        addSubscribe(mApiService.orderClass(bookID, chapterID, lessonTime)
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleMyResult(getView()))
                .subscribeWith(new CommonSubscriber<ResponseModel<Object>>(getView()) {
                    @Override
                    public void onNext(ResponseModel<Object> bean) {
                        getView().onOrderClassSuccess();
                    }
                })
        );
    }
}
