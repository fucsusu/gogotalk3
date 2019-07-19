package com.gogotalk.presenter;


import com.gogotalk.model.entity.ClassDetailBean;

public interface ClassDetailContract {
    interface View extends BaseContract.View{
        void setClassDetailDataToView(ClassDetailBean bean);
    }
    interface Presenter extends BaseContract.Presenter<ClassDetailContract.View>{
        void getClassDetailData(String id);
    }
}
