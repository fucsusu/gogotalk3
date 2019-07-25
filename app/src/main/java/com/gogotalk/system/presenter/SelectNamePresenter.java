package com.gogotalk.system.presenter;

import com.gogotalk.system.model.api.ApiService;
import com.gogotalk.system.model.entity.EnglishNameListBean;
import com.gogotalk.system.model.util.CommonSubscriber;
import com.gogotalk.system.model.util.RxUtil;
import javax.inject.Inject;

public class SelectNamePresenter extends RxPresenter<SelectNameContract.View> implements SelectNameContract.Presenter {

    private ApiService mApiService;

    @Inject
    public SelectNamePresenter(ApiService apiService){
        this.mApiService=apiService;
    }


    @Override
    public void getEnglishNameListData(int sex) {
        addSubscribe(mApiService.getEnglishNameListData(sex)
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleMyResult( getView(),false))
                .subscribeWith(new CommonSubscriber<EnglishNameListBean>(getView()) {
                    @Override
                    public void onNext(EnglishNameListBean englishNameListBean) {
                        getView().updateRecelyerViewData(englishNameListBean);
                    }
                })
        );
    }
}
