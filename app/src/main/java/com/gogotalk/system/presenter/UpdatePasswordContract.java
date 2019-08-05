package com.gogotalk.system.presenter;

public interface UpdatePasswordContract {
    interface View extends BaseContract.View{
        void onUpdatePasswordSuccess();
    }
    interface Presenter extends BaseContract.Presenter<UpdatePasswordContract.View>{
        void updatePassword(String phone, String code);
    }
}
