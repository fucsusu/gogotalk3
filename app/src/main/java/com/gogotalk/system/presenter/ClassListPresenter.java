package com.gogotalk.system.presenter;

import android.os.Environment;

import com.gogotalk.system.model.api.ApiService;
import com.gogotalk.system.model.entity.BookLevelBean;
import com.gogotalk.system.model.entity.GoGoBean;
import com.gogotalk.system.model.entity.GoItemBean;
import com.gogotalk.system.model.entity.RoomInfoBean;
import com.gogotalk.system.model.entity.WeekMakeBean;
import com.gogotalk.system.model.util.CommonSubscriber;
import com.gogotalk.system.model.util.GsonUtils;
import com.gogotalk.system.model.util.RxUtil;
import com.gogotalk.system.util.DelectFileUtil;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

public class ClassListPresenter extends RxPresenter<ClassListContract.View> implements ClassListContract.Presenter {

    private ApiService mApiService;

    @Inject
    public ClassListPresenter(ApiService apiService) {
        this.mApiService = apiService;
    }


    @Override
    public void getLevelListData() {
        addSubscribe(mApiService.getLevelListData()
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleMyResult(getView(), false))
                .subscribeWith(new CommonSubscriber<List<BookLevelBean>>(getView()) {
                    @Override
                    public void onNext(List<BookLevelBean> levelBeans) {
                        if (levelBeans == null) return;
                        if (levelBeans.size() == 0) return;
                        getView().updateLevelRecelyerViewData(levelBeans);
                    }

                    @Override
                    public boolean isShowLoading() {
                        return false;
                    }

                    @Override
                    public boolean isHideLoading() {
                        return false;
                    }
                }));
    }

    @Override
    public void getClassByLevel(int level) {
        addSubscribe(mApiService.getClassByLevel(String.valueOf(level))
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleMyResult(getView(), false))
                .subscribeWith(new CommonSubscriber<List<GoGoBean>>(getView()) {
                    @Override
                    public void onNext(List<GoGoBean> beans) {
                        if (beans == null) return;
                        if (beans.size() == 0) return;
                        if (beans.get(0).getChapterData() == null) return;
                        if (beans.get(0).getChapterData().size() == 0) return;
                        getView().updateUnitAndClassRecelyerViewData(beans);
                    }

                })
        );
    }

    @Override
    public void getWeekMakeBean() {
        addSubscribe(mApiService.getWeekMakeBean()
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleMyResult(getView(), false))
                .subscribeWith(new CommonSubscriber<List<WeekMakeBean>>(getView()) {
                    @Override
                    public void onNext(List<WeekMakeBean> beans) {
                        getView().setDataToYuyueDialogShow(beans);
                    }
                })
        );
    }

    @Override
    public void orderClass(int bookID, int chapterID, String lessonTime) {
        addSubscribe(mApiService.orderClass(bookID, chapterID, lessonTime)
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleMyResult(getView(), false))
                .subscribeWith(new CommonSubscriber<Object>(getView()) {
                    @Override
                    public void onNext(Object bean) {
                        getView().onOrderClassSuccess();
                    }
                })
        );
    }


    @Override
    public void getRoomInfo(GoItemBean goItemBean, String filePath) {
        addSubscribe(mApiService.getRoomInfo(goItemBean.getAttendLessonID())
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleMyResult(getView(), false))
                .subscribeWith(new CommonSubscriber<RoomInfoBean>(getView()) {
                    @Override
                    public void onNext(RoomInfoBean bean) {
                        //清除本地已经下载的叫名字自己和其他人的音频文件
                        if (DelectFileUtil.isCoursewareExistence(getView().getActivity(), "my.mp3")) {
                            DelectFileUtil.deleteFile(new File(getView().getActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "my.mp3"));
                        }
                        if (DelectFileUtil.isCoursewareExistence(getView().getActivity(), "other.mp3")) {
                            DelectFileUtil.deleteFile(new File(getView().getActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "other.mp3"));
                        }
                        //下载叫名字自己和其他人的音频文件
                        DelectFileUtil.downLoadFIle(getView().getActivity(), bean.getMyStudentSoundUrl(), "my.mp3");
                        DelectFileUtil.downLoadFIle(getView().getActivity(), bean.getOtherStudentSoundUrl(), "other.mp3");
                        getView().onRoomInfoSuccess(bean, filePath);
                    }

                    @Override
                    public String getLoadingTxt() {
                        return "加载中...";
                    }
                })
        );
    }

}
