package com.gogotalk.presenter;

import android.content.Intent;

import com.gogotalk.model.api.ApiService;
import com.gogotalk.model.util.CommonSubscriber;
import com.gogotalk.model.util.HttpUtils;
import com.gogotalk.model.util.RxUtil;
import com.gogotalk.util.SPUtils;
import com.gogotalk.view.activity.MainActivity;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

public class LoginPresenter extends RxPresenter<LoginContract.View> implements LoginContract.Presenter {

    private ApiService mApiService;

    @Inject
    public LoginPresenter( ApiService apiService){
        this.mApiService=apiService;
    }

    @Override
    public void login(String username,String password) {
        Map<String,String> map= new HashMap<>();
        map.put("UserName",username);
        map.put("Password",password);
        addSubscribe(mApiService.login(HttpUtils.getRequestBody(map))
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleMyResult(mView))
                .subscribeWith(new CommonSubscriber<Map<String,String>>(mView){
                    @Override
                    public void onNext(Map<String,String> mapData) {
                        SPUtils.putString("username",username);
                        SPUtils.putString("password",password);
                        SPUtils.putString("usertoken",mapData.get("userToken"));
                        mView.getActivity().startActivity(new Intent(mView.getActivity(), MainActivity.class));
                        mView.getActivity().finish();

                    }

                })
        );
    }

}
