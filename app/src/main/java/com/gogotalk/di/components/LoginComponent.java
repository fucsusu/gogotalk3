package com.gogotalk.di.components;

import com.gogotalk.di.modules.LoginModule;
import com.gogotalk.di.scopes.ActivityScope;
import com.gogotalk.view.activity.LoginActivity;
import dagger.Component;

@ActivityScope
@Component(modules = LoginModule.class,dependencies = NetComponent.class)
public interface LoginComponent {
    void inject(LoginActivity activity);
}
