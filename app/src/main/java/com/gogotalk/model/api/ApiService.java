package com.gogotalk.model.api;


import com.gogotalk.model.entity.CoursesBean;
import com.gogotalk.model.entity.ResponseModel;
import com.gogotalk.model.entity.UserInfoBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    @Headers({"Content-Type:application/json","Accept: application/json"})
    @POST("/api/User/Login")
    Flowable<ResponseModel<Map<String,String>>> login(@Body RequestBody body);

    @GET("/api/Lesson/GetUnFinishLessonList")
    Flowable<ResponseModel<List<CoursesBean>>> getClassListData();
    @GET("/api/User/GetStudentInfo")
    Flowable<ResponseModel<UserInfoBean>> getUserInfoData();
}

