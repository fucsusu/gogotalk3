package com.gogotalk.view.activity;

import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.gogotalk.R;
import com.gogotalk.app.AiRoomApplication;
import com.gogotalk.di.components.DaggerLoginComponent;
import com.gogotalk.di.modules.LoginModule;
import com.gogotalk.presenter.BaseContract;
import com.gogotalk.presenter.LoginContract;
import com.gogotalk.presenter.LoginPresenter;

import javax.inject.Inject;


import butterknife.BindView;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity implements LoginContract.View {

    @BindView(R.id.et_login_phone)
    EditText etLoginPhone;
    @BindView(R.id.et_login_password)
    EditText etLoginPassword;

    @Inject
    LoginPresenter loginPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initPresenter() {
        DaggerLoginComponent.builder()
                .loginModule(new LoginModule(this))
                .netComponent(AiRoomApplication.get(this).getNetComponent())
                .build()
                .inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected BaseContract.Presenter getPresenter() {
        return loginPresenter;
    }

    @OnClick(R.id.btn_login_submit) void submit() {
        loginPresenter.login(etLoginPhone.getText().toString(),etLoginPassword.getText().toString());
    }
}
