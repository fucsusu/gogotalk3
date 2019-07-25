package com.gogotalk.system.presenter;

import com.gogotalk.system.model.entity.CoursesBean;

import java.util.List;

public interface MainContract {
    interface View extends BaseContract.View{
        void showRecelyerViewOrEmptyViewByFlag(boolean flag);
        void updateRecelyerViewData(List<CoursesBean> data);
        void setUserInfoDataToView(String imageUrl,String name,String classTime,String expiry);
        void onCanelOrderClassSuccess();
        void onUpdateUserInfoSuceess();
    }
    interface Presenter extends BaseContract.Presenter<MainContract.View>{
        void getClassListData(boolean isShowLoading,boolean isHideLoading);
        void getUserInfoData(boolean isShowLoading,boolean isHideLoading);
        void canelOrderClass(int demandId);
        void updateUserInfo(String name,int sex);
    }
}
