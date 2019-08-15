package com.gogotalk.system.presenter;

import com.gogotalk.system.model.api.ApiService;
import com.gogotalk.system.model.util.CommonSubscriber;
import com.gogotalk.system.model.util.RxUtil;
import com.gogotalk.system.util.ToastUtils;

import javax.inject.Inject;

public class RegPresenter extends RxPresenter<RegContract.View> implements RegContract.Presenter {

    private ApiService mApiService;

    @Inject
    public RegPresenter(ApiService apiService) {
        this.mApiService = apiService;
    }


    @Override
    public void regUser(String phone, String code, String password) {
        addSubscribe(mApiService.regUser(phone, code, password)
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleMyResult(getView(), false))
                .subscribeWith(new CommonSubscriber<Object>(getView(), "注册失败") {
                    @Override
                    public void onNext(Object o) {
                        getView().onRegSuccess();
                    }
                })
        );
    }

    @Override
    public void sendCode(String phone) {
        addSubscribe(mApiService.sendCode(phone)
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleMyResult(getView(), false))
                .subscribeWith(new CommonSubscriber<Object>(getView()) {
                    @Override
                    public void onNext(Object mapData) {
                        ToastUtils.showShortToast(getView().getActivity(), "您即将获取验证码请注意查收！");
                    }
                })
        );
    }
}
