package com.gogotalk.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import com.gogotalk.R;
import com.gogotalk.model.util.Constant;
import com.gogotalk.presenter.LoginContract;
import com.gogotalk.presenter.LoginPresenter;
import com.gogotalk.util.SPUtils;

public class WelcomeActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String username = SPUtils.getString(Constant.SP_KEY_USERNAME, "");
                String password = SPUtils.getString(Constant.SP_KEY_PASSWORD, "");
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    mPresenter.login(username,password,false);
                    return;
                }
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                finish();
            }
        }, 2000);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

}
