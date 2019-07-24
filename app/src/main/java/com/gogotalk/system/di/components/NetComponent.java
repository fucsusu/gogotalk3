package com.gogotalk.system.di.components;


import com.gogotalk.system.di.modules.NetModule;
import com.gogotalk.system.model.api.ApiService;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@Component(modules = NetModule.class)
@Singleton
public interface NetComponent {
    ApiService getApiService();
    OkHttpClient getOkHttp();
    Retrofit getRetrofit();
}
