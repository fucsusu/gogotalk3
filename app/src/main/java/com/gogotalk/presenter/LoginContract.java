package com.gogotalk.presenter;

public interface LoginContract {
    interface View extends BaseContract.View{

    }
    interface Presenter extends BaseContract.Presenter{
        void login(String username,String password);
    }
}
