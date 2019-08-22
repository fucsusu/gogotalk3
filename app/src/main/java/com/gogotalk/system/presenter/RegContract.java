package com.gogotalk.system.presenter;

import java.util.Map;

public interface RegContract {
    interface View extends BaseContract.View{
    }
    interface Presenter extends BaseContract.Presenter<RegContract.View>{
        void regUser(String phone,String code,String password);
        void sendCode(String phone);
    }
}
