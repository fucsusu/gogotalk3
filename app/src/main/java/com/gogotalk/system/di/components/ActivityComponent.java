package com.gogotalk.system.di.components;

import com.gogotalk.system.di.modules.ActivityModule;
import com.gogotalk.system.di.scopes.ActivityScope;
import com.gogotalk.system.view.activity.ClassDetailActivity;
import com.gogotalk.system.view.activity.ClassListActivity;
import com.gogotalk.system.view.activity.ClassRoomActivity;
import com.gogotalk.system.view.activity.LoginActivity;
import com.gogotalk.system.view.activity.MainActivity;
import com.gogotalk.system.view.activity.RecordActivity;
import com.gogotalk.system.view.activity.WelcomeActivity;

import dagger.Component;

@ActivityScope
@Component(modules = ActivityModule.class,dependencies = NetComponent.class)
public interface ActivityComponent {
    void inject(LoginActivity activity);
    void inject(MainActivity activity);
    void inject(WelcomeActivity activity);
    void inject(RecordActivity activity);
    void inject(ClassDetailActivity activity);
    void inject(ClassListActivity activity);
    void inject(ClassRoomActivity activity);
}