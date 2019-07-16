package com.gogotalk.presenter;

import com.gogotalk.model.api.ApiService;
import com.gogotalk.model.entity.ResponseModel;
import com.gogotalk.model.util.HttpUtils;
import com.orhanobut.logger.Logger;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpUtils.BaseResponseObserver<ResponseModel.Data<Map<String,String>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Logger.i("===============onSubscribe================");
                    }

                    @Override
                    public void onNext(ResponseModel.Data<Map<String,String>> data) {
                        Logger.i(data.getData().get("userToken"));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.i("===============onError================");
                    }

                    @Override
                    public void onComplete() {
                        Logger.i("===============onComplete================");
                    }
                });
    }

    @Override
    public void onDestroy() {
        mView = null;
        System.gc();
    }
}
