package com.gogotalk.system.presenter;

import com.gogotalk.system.model.entity.RecordBean;

import java.util.List;

public interface RecordContract {
    interface View extends BaseContract.View{
        void showGridViewOrEmptyViewByFlag(boolean flag);
        void updateGridViewData(List<RecordBean> data);
    }
    interface Presenter extends BaseContract.Presenter<RecordContract.View>{
        void getClassRecordData();
    }
}
