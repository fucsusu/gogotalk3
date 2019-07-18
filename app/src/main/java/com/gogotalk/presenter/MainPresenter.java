package com.gogotalk.presenter;

import com.gogotalk.model.api.ApiService;
import com.gogotalk.model.entity.CoursesBean;
import com.gogotalk.model.entity.UserInfoBean;
import com.gogotalk.model.util.CommonSubscriber;
import com.gogotalk.model.util.RxUtil;
import com.gogotalk.util.AppUtils;
import java.util.List;
import javax.inject.Inject;

public class MainPresenter extends RxPresenter<MainContract.View> implements MainContract.Presenter{
    private ApiService mApiService;

    @Inject
    public MainPresenter( ApiService apiService){
        this.mApiService=apiService;
    }

    @Override
    public void getClassListData(boolean isShowLoading,boolean isHideLoading) {
        addSubscribe(mApiService.getClassListData()
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleMyResult(getView()))
                .subscribeWith(new CommonSubscriber<List<CoursesBean>>( getView()){

                    @Override
                    public void onNext(List<CoursesBean> coursesBeans) {
                        if (coursesBeans == null) return;
                        if (coursesBeans.size() == 0) return;
                        getView().showRecelyerViewOrEmptyViewByFlag(true);
                        getView().updateRecelyerViewData(coursesBeans);
                    }
                    @Override
                    public boolean isShowLoading() {
                        if(!isShowLoading){
                            return false;
                        }
                        return super.isShowLoading();
                    }

                    @Override
                    public boolean isHideLoading() {
                        if(!isHideLoading){
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
    public void getUserInfoData(boolean isShowLoading,boolean isHideLoading) {
        addSubscribe(mApiService.getUserInfoData()
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleMyResult(getView()))
                .subscribeWith(new CommonSubscriber<UserInfoBean>( getView()){

                    @Override
                    public void onNext(UserInfoBean userInfoBean) {
                        if (userInfoBean == null) return;
                        AppUtils.saveUserInfoData(userInfoBean);
                        getView().setUserInfoDataToView(userInfoBean.getImageUrl()
                                ,userInfoBean.getName()
                                ,String.valueOf(userInfoBean.getStudentLessonCount())
                                ,userInfoBean.getExpireTime());
                    }

                    @Override
                    public boolean isShowLoading() {
                        if(!isShowLoading){
                            return false;
                        }
                        return super.isShowLoading();
                    }

                    @Override
                    public boolean isHideLoading() {
                        if(!isHideLoading){
                            return false;
                        }
                        return super.isHideLoading();
                    }
                })
        );
    }

}
