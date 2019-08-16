package com.gogotalk.system.presenter;

import android.app.Activity;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.gogotalk.system.app.AiRoomApplication;
import com.gogotalk.system.model.api.ApiService;
import com.gogotalk.system.model.entity.CoursesBean;
import com.gogotalk.system.model.entity.RoomInfoBean;
import com.gogotalk.system.model.entity.UserInfoBean;
import com.gogotalk.system.model.util.CommonSubscriber;
import com.gogotalk.system.model.util.Constant;
import com.gogotalk.system.model.util.HttpUtils;
import com.gogotalk.system.model.util.RxUtil;
import com.gogotalk.system.util.AppUtils;
import com.gogotalk.system.util.BaseDownLoadFileImpl;
import com.gogotalk.system.util.DelectFileUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class MainPresenter extends RxPresenter<MainContract.View> implements MainContract.Presenter {
    private ApiService mApiService;

    @Inject
    public MainPresenter(ApiService apiService) {
        this.mApiService = apiService;
    }

    @Override
    public void getClassListData(boolean isShowLoading, boolean isHideLoading) {
        addSubscribe(mApiService.getClassListData()
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleMyResult(getView(), false))
                .subscribeWith(new CommonSubscriber<List<CoursesBean>>(getView()) {

                    @Override
                    public void onNext(List<CoursesBean> coursesBeans) {
                        if (coursesBeans == null) return;
                        if (coursesBeans.size() == 0) return;
                        getView().showRecelyerViewOrEmptyViewByFlag(true);
                        getView().updateRecelyerViewData(coursesBeans);
                    }

                    @Override
                    public boolean isError() {
                        return isHideLoading;
                    }

                    @Override
                    public boolean isShowLoading() {
                        if (!isShowLoading) {
                            return false;
                        }
                        return super.isShowLoading();
                    }

                    @Override
                    public boolean isHideLoading() {
                        if (!isHideLoading) {
                            return false;
                        }
                        return super.isHideLoading();
                    }

                    @Override
                    public void onFail() {
                        getView().showRecelyerViewOrEmptyViewByFlag(false);
                    }
                })
        );
    }

    @Override
    public void getUserInfoData(boolean isShowLoading, boolean isHideLoading) {
        addSubscribe(mApiService.getUserInfoData()
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleMyResult(getView(), false))
                .subscribeWith(new CommonSubscriber<UserInfoBean>(getView()) {

                    @Override
                    public void onNext(UserInfoBean userInfoBean) {
                        if (userInfoBean == null) return;
                        AppUtils.saveUserInfoData(userInfoBean);
                        getView().setUserInfoDataToView(userInfoBean.getImageUrl()
                                , userInfoBean.getName()
                                , String.valueOf(userInfoBean.getStudentLessonCount())
                                , userInfoBean.getExpireTime());
                    }

                    @Override
                    public boolean isShowLoading() {
                        if (!isShowLoading) {
                            return false;
                        }
                        return super.isShowLoading();
                    }

                    @Override
                    public boolean isHideLoading() {
                        if (!isHideLoading) {
                            return false;
                        }
                        return super.isHideLoading();
                    }
                })
        );
    }

    @Override
    public void canelOrderClass(int demandId) {
        addSubscribe(mApiService.cancelOrderClass(demandId)
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleMyResult(getView(), true))
                .subscribeWith(new CommonSubscriber<Object>(getView()) {
                    @Override
                    public void onNext(Object bean) {
                        getView().onCanelOrderClassSuccess();
                    }

                })
        );
    }

    @Override
    public void updateUserInfo(String name, int sex) {
        Map<String, String> map = new HashMap<>();
        map.put("EName", name);
        map.put("Gender", String.valueOf(sex));
        addSubscribe(mApiService.updateUserInfo(HttpUtils.getRequestBody(map))
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleMyResult(getView(), true))
                .subscribeWith(new CommonSubscriber<Object>(getView()) {
                    @Override
                    public void onNext(Object bean) {
                        getView().onUpdateUserInfoSuceess();
                    }
                })
        );
    }

    @Override
    public void getRoomInfo(CoursesBean coursesBean, String filePath) {
        addSubscribe(mApiService.getRoomInfo(String.valueOf(coursesBean.getAttendLessonID()))
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleMyResult(getView(), false))
                .subscribeWith(new CommonSubscriber<RoomInfoBean>(getView()) {
                    @Override
                    public void onNext(RoomInfoBean bean) {
                        //清除本地已经下载的叫名字自己和其他人的音频文件
                        if (DelectFileUtil.isCoursewareExistence(getView().getActivity(), "1.mp3")) {
                            DelectFileUtil.deleteFile(new File(getView().getActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "my.mp3"));
                        }
                        if (DelectFileUtil.isCoursewareExistence(getView().getActivity(), "2.mp3")) {
                            DelectFileUtil.deleteFile(new File(getView().getActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "other.mp3"));
                        }
                        //下载叫名字自己和其他人的音频文件
                        String myStudentSoundUrl = bean.getMyStudentSoundUrl();
                        if (!TextUtils.isEmpty(myStudentSoundUrl)) {
                            DelectFileUtil.downLoadFIle(getView().getActivity(), myStudentSoundUrl, "1.mp3");
                        }

                        String otherStudentSoundUrl = bean.getOtherStudentSoundUrl();
                        if (!TextUtils.isEmpty(otherStudentSoundUrl)) {
                            DelectFileUtil.downLoadFIle(getView().getActivity(), otherStudentSoundUrl, "2.mp3");
                        }
                        getView().onRoomInfoSuccess(bean, filePath);
                    }
                })
        );
    }


}
