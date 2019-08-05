package com.gogotalk.system.presenter;

import android.content.Intent;

import com.gogotalk.system.model.api.ApiService;
import com.gogotalk.system.model.util.CommonSubscriber;
import com.gogotalk.system.model.util.Constant;
import com.gogotalk.system.model.util.HttpUtils;
import com.gogotalk.system.model.util.RxUtil;
import com.gogotalk.system.util.SPUtils;
import com.gogotalk.system.util.ToastUtils;
import com.gogotalk.system.view.activity.LoginActivity;
import com.gogotalk.system.view.activity.MainActivity;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class ForgetPresenter extends RxPresenter<ForgetContract.View> implements ForgetContract.Presenter {

    private ApiService mApiService;

    @Inject
    public ForgetPresenter(ApiService apiService){
        this.mApiService=apiService;
    }

    @Override
    public void sendCode(String phone) {
        addSubscribe(mApiService.sendCode(phone)
                        .compose(RxUtil.rxSchedulerHelper())
                        .compose(RxUtil.handleMyResult(getView(),false))
                        .subscribeWith(new CommonSubscriber<Object>( getView()){
                            @Override
                            public void onNext(Object mapData) {
                                ToastUtils.showShortToast(getView().getActivity(),"您即将获取验证码请注意查收！");
                            }
                        })
        );
    }

    @Override
    public void checkCode(String phone, String code) {
        addSubscribe(mApiService.checkCode(phone,code)
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleMyResult(getView(),false))
                .subscribeWith(new CommonSubscriber<Object>( getView()){
                    @Override
                    public void onNext(Object mapData) {
                        getView().onCheckCodeSuccess();
                    }
                })
        );
    }

}
