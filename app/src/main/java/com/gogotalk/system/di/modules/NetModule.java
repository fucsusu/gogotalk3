package com.gogotalk.system.di.modules;

import android.text.TextUtils;

import com.gogotalk.system.BuildConfig;
import com.gogotalk.system.model.api.ApiService;
import com.gogotalk.system.model.util.Constant;
import com.gogotalk.system.model.util.GsonUtils;
import com.gogotalk.system.util.BaseDownLoadFileImpl;
import com.gogotalk.system.util.SPUtils;
import com.gogotalk.system.util.SystemDownLoadFileImpl;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.gogotalk.system.model.util.Constant.PATH_RELEASE_URL;

@Module
public class NetModule {

    @Provides
    public BaseDownLoadFileImpl provideDownLoadFileImpl() {
        return new SystemDownLoadFileImpl();
    }

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
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request.Builder builder = request.newBuilder();
                        String usertoken = SPUtils.getString(Constant.SP_KEY_USERTOKEN);
                        if (!TextUtils.isEmpty(usertoken)) {
                            builder.addHeader("Authorization", usertoken);
                        }
                        Request build = builder.build();
                        Logger.i("===================" + usertoken + "=====================");
                        return chain.proceed(build);
                    }
                })
                .build();

        return okhttpClient;
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient okhttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(okhttpClient)
                .baseUrl(PATH_RELEASE_URL)
//                .baseUrl(PATH_DEBUG_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonUtils.gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    public ApiService provideApiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }
}
