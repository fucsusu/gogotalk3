package com.gogotalk.system.presenter;

import com.gogotalk.system.model.entity.EnglishNameListBean;

public interface SelectNameContract {
    interface View extends BaseContract.View{
        void updateRecelyerViewData(EnglishNameListBean bean);
    }
    interface Presenter extends BaseContract.Presenter<SelectNameContract.View>{
        void getEnglishNameListData(int sex);
    }
}
