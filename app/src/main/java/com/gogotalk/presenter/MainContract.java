package com.gogotalk.presenter;

import com.gogotalk.model.entity.CoursesBean;

import java.util.List;

public interface MainContract {
    interface View extends BaseContract.View{
        void showRecelyerViewOrEmptyViewByFlag(boolean flag);
        void updateRecelyerViewData(List<CoursesBean> data);
        void setUserInfoDataToView(String imageUrl,String name,String classTime,String expiry);
    }
    interface Presenter extends BaseContract.Presenter<MainContract.View>{
        void getClassListData(boolean isShowLoading,boolean isHideLoading);
        void getUserInfoData(boolean isShowLoading,boolean isHideLoading);
    }
}
