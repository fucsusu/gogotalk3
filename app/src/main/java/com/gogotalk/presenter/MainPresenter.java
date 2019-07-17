package com.gogotalk.presenter;


import com.gogotalk.model.api.ApiService;

import javax.inject.Inject;

public class MainPresenter extends RxPresenter<MainContract.View> {
    private ApiService mApiService;

    @Inject
    public MainPresenter( ApiService apiService){
        this.mApiService=apiService;
    }
}
