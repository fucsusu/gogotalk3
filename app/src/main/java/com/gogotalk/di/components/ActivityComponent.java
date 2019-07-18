package com.gogotalk.di.components;

import com.gogotalk.di.modules.ActivityModule;
import com.gogotalk.di.scopes.ActivityScope;
import com.gogotalk.view.activity.LoginActivity;
import com.gogotalk.view.activity.MainActivity;
import com.gogotalk.view.activity.WelcomeActivity;

import dagger.Component;

@ActivityScope
@Component(modules = ActivityModule.class,dependencies = NetComponent.class)
public interface ActivityComponent {
    void inject(LoginActivity activity);
    void inject(MainActivity activity);
    void inject(WelcomeActivity activity);
}
