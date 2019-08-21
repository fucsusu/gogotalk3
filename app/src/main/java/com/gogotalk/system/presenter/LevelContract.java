package com.gogotalk.system.presenter;

import com.gogotalk.system.model.entity.LevelListBean;
import com.gogotalk.system.model.entity.LevelResultBean;
import com.gogotalk.system.model.entity.QuestionsBean;

import java.util.List;
import java.util.Map;

public interface LevelContract {
    interface View extends BaseContract.View{
        void showQuestions(QuestionsBean bean);
        void showLevelResult(LevelResultBean bean);
        void onGetLevelListSuccess(List<LevelListBean> beans);
    }
    interface Presenter extends BaseContract.Presenter<LevelContract.View>{
        void getSurveyQuestion();
        void gradeInvestigation(Map<String, String> map);
        void updateStudentLevel(int level);
        void getLeveInfoList();
    }
}
