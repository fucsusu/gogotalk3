package com.gogotalk.system.presenter;

import java.util.List;

public interface SearchNameContract {
    interface View extends BaseContract.View{
        void updateRecelyerViewData(List<String> beans);
    }
    interface Presenter extends BaseContract.Presenter<SearchNameContract.View>{
        void searchEnglishNameListData(int sex,String keyword);
    }
}
