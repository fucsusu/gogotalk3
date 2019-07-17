package com.gogotalk.presenter;

import android.app.Activity;

public interface BaseContract {
    interface View{
        void showLoading(String msg);
        void hideLoading();
        Activity getActivity();
    }
    interface Presenter<T extends BaseContract.View>{
        void attachView(T view);
        void detachView();

    }
    interface Moudle{

    }
}
