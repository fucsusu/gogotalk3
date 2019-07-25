package com.gogotalk.system.presenter;

import android.content.Intent;

import com.gogotalk.system.model.api.ApiService;
import com.gogotalk.system.model.util.CommonSubscriber;
import com.gogotalk.system.model.util.Constant;
import com.gogotalk.system.model.util.HttpUtils;
import com.gogotalk.system.model.util.RxUtil;
import com.gogotalk.system.util.SPUtils;
import com.gogotalk.system.view.activity.LoginActivity;
import com.gogotalk.system.view.activity.MainActivity;
import com.gogotalk.system.view.activity.WelcomeActivity;

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
    public void login(String username,String password,boolean isLoading,boolean isAuto) {
        Map<String,String> map= new HashMap<>();
        map.put("UserName",username);
        map.put("Password",password);
        addSubscribe(mApiService.login(HttpUtils.getRequestBody(map))
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleMyResult( getView(),false))
                .subscribeWith(new CommonSubscriber<Map<String,String>>( getView()){
                    @Override
                    public void onNext(Map<String,String> mapData) {
                        SPUtils.putString(Constant.SP_KEY_USERNAME,username);
                        SPUtils.putString(Constant.SP_KEY_PASSWORD,password);
                        SPUtils.putString(Constant.SP_KEY_USERTOKEN,mapData.get(Constant.SP_KEY_USERTOKEN));
                        getView().getActivity().startActivity(new Intent(getView().getActivity(), MainActivity.class));
                        getView().getActivity().finish();
                    }

                    @Override
                    public String getLoadingTxt() {
                        return "登录中...";
                    }

                    @Override
                    public boolean isShowLoading() {
                        if(!isLoading){
                            return false;
                        }
                        return super.isShowLoading();
                    }

                    @Override
                    public boolean isHideLoading() {
                        if(!isLoading){
                            return false;
                        }
                        return super.isHideLoading();
                    }

                    @Override
                    public void onFail() {
                        super.onFail();
                        if(isAuto){
                            getView().getActivity().startActivity(new Intent(getView().getActivity(), LoginActivity.class));
                            getView().getActivity().finish();
                        }
                    }
                })
        );
    }

}
