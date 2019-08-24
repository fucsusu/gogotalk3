package com.gogotalk.system.presenter;

import android.content.Intent;

import com.gogotalk.system.model.api.ApiService;
import com.gogotalk.system.model.util.CommonSubscriber;
import com.gogotalk.system.model.util.Constant;
import com.gogotalk.system.model.util.GsonUtils;
import com.gogotalk.system.model.util.RxUtil;
import com.gogotalk.system.util.SPUtils;
import com.gogotalk.system.util.ToastUtils;
import com.gogotalk.system.view.activity.MainActivity;
import com.orhanobut.logger.Logger;

import java.util.Map;

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
                .subscribeWith(new CommonSubscriber<Map<String, String>>(getView()) {
                    @Override
                    public void onNext(Map<String, String> mapData) {
                        ToastUtils.showShortToast(getView().getActivity(), "注册成功！");
                        SPUtils.putString(Constant.SP_KEY_USERNAME, phone);
                        SPUtils.putString(Constant.SP_KEY_PASSWORD, password);
                        SPUtils.putString(Constant.SP_KEY_USERTOKEN, mapData.get(Constant.SP_KEY_USERTOKEN));
                        getView().getActivity().startActivity(new Intent(getView().getActivity(), MainActivity.class));
                        getView().getActivity().finish();
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
