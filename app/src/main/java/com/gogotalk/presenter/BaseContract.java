package com.gogotalk.presenter;

import android.app.Activity;

public interface BaseContract {
    interface View{
        void showLoading();
        void hideLoading();
        Activity getActivity();
    }
    interface Presenter{
        void onDestroy();
    }
    interface Moudle{

    }
}
