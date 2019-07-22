package com.gogotalk.presenter;


import com.gogotalk.model.api.ApiService;
import com.gogotalk.model.entity.ClassDetailBean;
import com.gogotalk.model.entity.RecordBean;
import com.gogotalk.model.entity.WeekMakeBean;
import com.gogotalk.model.util.CommonSubscriber;
import com.gogotalk.model.util.GsonUtils;
import com.gogotalk.model.util.RxUtil;
import com.orhanobut.logger.Logger;

import java.util.List;

import javax.inject.Inject;

public class ClassDetailPresenter extends RxPresenter<ClassDetailContract.View> implements ClassDetailContract.Presenter {

    private ApiService mApiService;

    @Inject
    public ClassDetailPresenter(ApiService apiService){
        this.mApiService=apiService;
    }


    @Override
    public void getClassDetailData(String id) {
        addSubscribe(mApiService.getClassDetailData(id)
        .compose(RxUtil.rxSchedulerHelper())
        .compose(RxUtil.handleMyResult(getView(),false))
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
                .compose(RxUtil.handleMyResult(getView(),false))
                .subscribeWith(new CommonSubscriber<List<WeekMakeBean>>(getView()) {
                    @Override
                    public void onNext(List<WeekMakeBean> beans) {
                        getView().setDataToYuyueDialogShow(beans);
                    }
                })
        );
    }

    @Override
    public void orderClass(int bookID, int chapterID, String lessonTime) {
        addSubscribe(mApiService.orderClass(bookID,chapterID,lessonTime)
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleMyResult(getView(),false))
                .subscribeWith(new CommonSubscriber<Object>(getView()) {
                    @Override
                    public void onNext(Object bean) {
                        getView().onOrderClassSuccess();
                    }
                })
        );
    }
}
