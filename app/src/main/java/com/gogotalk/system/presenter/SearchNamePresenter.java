package com.gogotalk.system.presenter;

import com.gogotalk.system.model.api.ApiService;
import com.gogotalk.system.model.util.CommonSubscriber;
import com.gogotalk.system.model.util.RxUtil;
import java.util.List;
import javax.inject.Inject;

public class SearchNamePresenter extends RxPresenter<SearchNameContract.View> implements SearchNameContract.Presenter {

    private ApiService mApiService;

    @Inject
    public SearchNamePresenter(ApiService apiService){
        this.mApiService=apiService;
    }


    @Override
    public void searchEnglishNameListData(int sex,String keyword) {
        addSubscribe(mApiService.searchEnglishNameListData(sex,keyword)
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleMyResult( getView(),false))
                .subscribeWith(new CommonSubscriber<List<String>>(getView()) {
                    @Override
                    public void onNext(List<String> beans) {
                        getView().updateRecelyerViewData(beans);
                    }
                })
        );
    }
}
