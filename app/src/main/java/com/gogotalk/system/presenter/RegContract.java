package com.gogotalk.system.presenter;

public interface RegContract {
    interface View extends BaseContract.View{
        void onRegSuccess();
    }
    interface Presenter extends BaseContract.Presenter<RegContract.View>{
        void regUser(String phone,String code,String password);
        void sendCode(String phone);
    }
}
