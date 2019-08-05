package com.gogotalk.system.presenter;

public interface ForgetContract {
    interface View extends BaseContract.View{
        void onCheckCodeSuccess();
    }
    interface Presenter extends BaseContract.Presenter<ForgetContract.View>{
        void sendCode(String phone);
        void checkCode(String phone,String code);
    }
}
