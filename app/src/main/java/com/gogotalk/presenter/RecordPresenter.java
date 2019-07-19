package com.gogotalk.presenter;


import com.gogotalk.model.api.ApiService;
import com.gogotalk.model.entity.RecordBean;
import com.gogotalk.model.util.CommonSubscriber;
import com.gogotalk.model.util.RxUtil;
import java.util.List;
import javax.inject.Inject;

public class RecordPresenter extends RxPresenter<RecordContract.View> implements RecordContract.Presenter {

    private ApiService mApiService;

    @Inject
    public RecordPresenter(ApiService apiService){
        this.mApiService=apiService;
    }


    @Override
    public void getClassRecordData() {
        addSubscribe(mApiService.getClassRecordData()
        .compose(RxUtil.rxSchedulerHelper())
        .compose(RxUtil.handleMyResult(getView()))
        .subscribeWith(new CommonSubscriber<List<RecordBean>>(getView()) {
            @Override
            public void onNext(List<RecordBean> recordBeans) {
                getView().showGridViewOrEmptyViewByFlag(true);
                getView().updateGridViewData(recordBeans);
            }

            @Override
            public void onFail() {
                super.onFail();
                getView().showGridViewOrEmptyViewByFlag(false);
            }
        }));
    }
}
