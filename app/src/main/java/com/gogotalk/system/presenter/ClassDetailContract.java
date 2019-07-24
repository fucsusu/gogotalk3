package com.gogotalk.system.presenter;

import com.gogotalk.system.model.entity.ClassDetailBean;
import com.gogotalk.system.model.entity.WeekMakeBean;
import java.util.List;

public interface ClassDetailContract {
    interface View extends BaseContract.View{
        void setClassDetailDataToView(ClassDetailBean bean);
        void setDataToYuyueDialogShow(List<WeekMakeBean> beans);
        void onOrderClassSuccess();
    }
    interface Presenter extends BaseContract.Presenter<ClassDetailContract.View>{
        void getClassDetailData(String id);
        void getWeekMakeBean();
        void orderClass(int bookID,int chapterID,String lessonTime );
    }
}
