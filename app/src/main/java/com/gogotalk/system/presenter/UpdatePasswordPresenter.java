package com.gogotalk.system.presenter;

import com.gogotalk.system.model.api.ApiService;
import com.gogotalk.system.model.util.CommonSubscriber;
import com.gogotalk.system.model.util.HttpUtils;
import com.gogotalk.system.model.util.RxUtil;
import com.gogotalk.system.util.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class UpdatePasswordPresenter extends RxPresenter<UpdatePasswordContract.View> implements UpdatePasswordContract.Presenter {

    private ApiService mApiService;

    @Inject
    public UpdatePasswordPresenter(ApiService apiService){
        this.mApiService=apiService;
    }


    @Override
    public void updatePassword(String phone, String password) {
        Map<String,String> map= new HashMap<>();
        map.put("Phone",phone);
        map.put("NewPsw",password);
        map.put("ConfirmPsw",password);
        addSubscribe(mApiService.updatePassword(HttpUtils.getRequestBody(map))
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleMyResult(getView(),true))
                .subscribeWith(new CommonSubscriber<Object>( getView()){
                    @Override
                    public void onNext(Object  mapData) {
                        getView().onUpdatePasswordSuccess();
                    }
                })
        );
    }
}
