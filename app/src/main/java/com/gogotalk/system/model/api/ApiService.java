package com.gogotalk.system.model.api;


import com.gogotalk.system.model.entity.AppInfoDownLoadBean;
import com.gogotalk.system.model.entity.BookLevelBean;
import com.gogotalk.system.model.entity.ClassDetailBean;
import com.gogotalk.system.model.entity.CoursesBean;
import com.gogotalk.system.model.entity.EnglishNameListBean;
import com.gogotalk.system.model.entity.GoGoBean;
import com.gogotalk.system.model.entity.LevelListBean;
import com.gogotalk.system.model.entity.LevelResultBean;
import com.gogotalk.system.model.entity.QuestionsBean;
import com.gogotalk.system.model.entity.RecordBean;
import com.gogotalk.system.model.entity.ResponseModel;
import com.gogotalk.system.model.entity.RoomInfoBean;
import com.gogotalk.system.model.entity.UserInfoBean;
import com.gogotalk.system.model.entity.WeekMakeBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


public interface ApiService {
    @Headers({"Content-Type:application/json", "Accept: application/json"})
    @POST("/api/User/Login")
    Flowable<ResponseModel<Map<String, String>>> login(@Body RequestBody body);

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

    @GET("/api/Lesson/CancelLesson")
    Flowable<ResponseModel<Object>> cancelOrderClass(@Query("DemandId") int demandId);

    @GET("/api/Lesson/GetLessonDate")
    Flowable<ResponseModel<List<WeekMakeBean>>> getWeekMakeBean();

    @GET("/api/Lesson/JoinAttendLesson")
    Flowable<ResponseModel<Object>> orderClass(@Query("bookID") int bookID, @Query("chapterID") int chapterID, @Query("lessonTime") String lessonTime);

    @GET("/api/User/GetNewestVersionInfo?devicetype=4")
    Flowable<ResponseModel<AppInfoDownLoadBean>> getNewAppVersionInfo();

    @Headers({"Content-Type:application/json", "Accept: application/json"})
    @POST("/api/User/UpdateStudentInfo")
    Flowable<ResponseModel<Object>> updateUserInfo(@Body RequestBody body);

    @GET("/api/User/GetEnglishNameList")
    Flowable<ResponseModel<EnglishNameListBean>> getEnglishNameListData(@Query("sexFlag") int sex);

    @GET("/api/User/SeacheEnglishName")
    Flowable<ResponseModel<List<String>>> searchEnglishNameListData(@Query("sexFlag") int sex, @Query("wordName") String keyword);

    @Streaming
    @GET
    Observable<ResponseBody> downLoadClassFile(@Url String url);

    @GET("/api/Lesson/EnterRoom")
    Flowable<ResponseModel<RoomInfoBean>> getRoomInfo(@Query("AttendLessonId") String attendLessonId);

    @GET("/api/User/GoGoTalkSendPhoneCode")
    Flowable<ResponseModel<Object>> sendCode(@Query("Phone") String phone);

    @GET("/api/User/CheckCodeByPhone")
    Flowable<ResponseModel<Object>> checkCode(@Query("Phone") String phone, @Query("Code") String code);

    @Headers({"Content-Type:application/json", "Accept: application/json"})
    @POST("/api/User/ModifyPassword")
    Flowable<ResponseModel<Object>> updatePassword(@Body RequestBody body);

    @GET("/api/User/UserRegister")
    Flowable<ResponseModel<Object>> regUser(@Query("Phone") String phone, @Query("Code") String code, @Query("Password") String password);

    //更新数据库奖杯数量
    @GET("/api/Lesson/UpdateGiftCupNum")
    Flowable<ResponseModel<Object>> saveJbNum(@Query("attendDetialId") int attendDetialId, @Query("giftCupNum") int giftCupNum);

    @GET("/api/User/GetSurveyQuestion")
    Flowable<ResponseModel<QuestionsBean>> getSurveyQuestion();

    @Headers({"Content-Type:application/json", "Accept: application/json"})
    @POST("/api/User/GradeInvestigation")
    Flowable<ResponseModel<LevelResultBean>> gradeInvestigation(@Body RequestBody body);

    @GET("/api/User/UpdateStudentLevel")
    Flowable<ResponseModel<Object>> updateStudentLevel(@Query("level") int level);

    @GET("/api/User/GetLeveInfoList")
    Flowable<ResponseModel<List<LevelListBean>>> getLeveInfoList();

}

