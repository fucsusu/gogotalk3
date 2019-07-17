package com.gogotalk.di.modules;

import com.gogotalk.BuildConfig;
import com.gogotalk.model.api.ApiService;
import com.gogotalk.model.util.GsonUtils;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static com.gogotalk.model.util.Constant.PATH_DEBUG_URL;
import static com.gogotalk.model.util.Constant.PATH_RELEASE_URL;

@Module
public class NetModule {

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        OkHttpClient okhttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build();
        return okhttpClient;
    }
    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient okhttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(okhttpClient)
                .baseUrl(BuildConfig.DEBUG?PATH_DEBUG_URL:PATH_RELEASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonUtils.gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }
    @Provides
    @Singleton
    public ApiService provideApiService(Retrofit retrofit){
        return retrofit.create(ApiService.class);
    }
}
