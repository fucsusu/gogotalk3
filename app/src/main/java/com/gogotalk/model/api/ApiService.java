package com.gogotalk.model.api;


import com.gogotalk.model.entity.BookLevelBean;
import com.gogotalk.model.entity.ClassDetailBean;
import com.gogotalk.model.entity.CoursesBean;
import com.gogotalk.model.entity.GoGoBean;
import com.gogotalk.model.entity.RecordBean;
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
import retrofit2.http.Query;
import retrofit2.http.QueryName;

public interface ApiService {
    @Headers({"Content-Type:application/json","Accept: application/json"})
    @POST("/api/User/Login")
    Flowable<ResponseModel<Map<String,String>>> login(@Body RequestBody body);

    @GET("/api/Lesson/GetUnFinishLessonList")
    Flowable<ResponseModel<List<CoursesBean>>> getClassListData();

    @GET("/api/User/GetStudentInfo")
    Flowable<ResponseModel<UserInfoBean>> getUserInfoData();

    @GET("/api/Lesson/GetFinishLessonList")
    Flowable<ResponseModel<List<RecordBean>>> getClassRecordData();

    @GET("/api/Lesson/GetChapterInfo")
    Flowable<ResponseModel<ClassDetailBean>> getClassDetailData(@Query("detialRecordId") String id);

    @GET("/api/Lesson/GetBookInfoList")
    Flowable<ResponseModel<List<BookLevelBean>>> getLevelListData();

    @GET("/api/Lesson/GetLessonUnitList")
    Flowable<ResponseModel<List<GoGoBean>>> getClassByLevel(@Query("level") String level);

}

