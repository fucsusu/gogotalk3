package com.gogotalk.system.presenter;

import com.gogotalk.system.model.entity.BookLevelBean;
import com.gogotalk.system.model.entity.GoGoBean;
import com.gogotalk.system.model.entity.WeekMakeBean;

import java.util.List;

public interface ClassListContract {
    interface View extends BaseContract.View{
        void updateLevelRecelyerViewData(List<BookLevelBean> levelBeans);
        void updateUnitAndClassRecelyerViewData(List<GoGoBean> beans);
        void onOrderClassSuccess();
        void setDataToYuyueDialogShow(List<WeekMakeBean> beans);
    }
    interface Presenter extends BaseContract.Presenter<ClassListContract.View>{
        void getLevelListData();
        void getClassByLevel(int level);
        void getWeekMakeBean();
        void orderClass(int bookID, int chapterID, String lessonTime);
    }
}