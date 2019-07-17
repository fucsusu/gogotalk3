package com.gogotalk.presenter;

import com.gogotalk.model.api.ApiService;
import com.gogotalk.model.util.CommonSubscriber;
import com.gogotalk.model.util.HttpUtils;
import com.gogotalk.model.util.RxUtil;
import com.orhanobut.logger.Logger;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View mView;
    private ApiService mApiService;

    @Inject
    public LoginPresenter(LoginContract.View view, ApiService apiService){
        this.mView = view;
        this.mApiService=apiService;
    }

    @Override
    public void login(String username,String password) {
        Map<String,String> map= new HashMap<>();
        map.put("UserName",username);
        map.put("Password",password);
        mApiService.login(HttpUtils.getRequestBody(map))
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleMyResult())
                .subscribeWith(new CommonSubscriber<Map<String,String>>(mView){
                    @Override
                    public void onNext(Map<String,String> mapData) {
                        Logger.i(mapData.get("userToken"));
                    }

                });
    }

    @Override
    public void onDestroy() {
        mView = null;
        System.gc();
    }
}
