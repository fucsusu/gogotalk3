package com.gogotalk.system.presenter;


import com.gogotalk.system.model.api.ApiService;
import com.gogotalk.system.model.entity.RecordBean;
import com.gogotalk.system.model.util.CommonSubscriber;
import com.gogotalk.system.model.util.RxUtil;
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
        .compose(RxUtil.handleMyResult(getView(),false))
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
