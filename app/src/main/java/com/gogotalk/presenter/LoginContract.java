package com.gogotalk.presenter;

public interface LoginContract {
    interface View extends BaseContract.View{

    }
    interface Presenter extends BaseContract.Presenter<LoginContract.View>{
        void login(String username,String password1,boolean isLoading);
    }
}
