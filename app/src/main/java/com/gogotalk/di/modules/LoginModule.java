package com.gogotalk.di.modules;

import com.gogotalk.presenter.LoginContract;
import dagger.Module;
import dagger.Provides;

@Module
public class LoginModule {
    private LoginContract.View mView;

    public LoginModule(LoginContract.View mView) {
        this.mView = mView;
    }
    @Provides
    public LoginContract.View provideView() {
        return mView;
    }
}
