package com.gogotalk.model.api;


import com.gogotalk.model.entity.ResponseModel;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    @Headers({"Content-Type:application/json","Accept: application/json"})
    @POST("/api/User/Login")
    Observable<ResponseModel.Data<Map<String,String>>> login(@Body RequestBody body);
}

