package com.gogotalk.system.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.gogotalk.system.R;
import com.gogotalk.system.model.util.Constant;
import com.gogotalk.system.presenter.LoginContract;
import com.gogotalk.system.presenter.LoginPresenter;
import com.gogotalk.system.util.FormCheckUtils;
import com.gogotalk.system.util.PermissionsUtil;
import com.gogotalk.system.util.SPUtils;
import com.gogotalk.system.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录页
 */
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.et_login_phone)
    EditText etLoginPhone;
    @BindView(R.id.et_login_password)
    EditText etLoginPassword;
    @BindView(R.id.btn_forget)
    TextView btnForget;
    @BindView(R.id.tv_reg)
    TextView tvReg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkLocalStorage();
        PermissionsUtil.getInstance().requestPermissions(this);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        super.initView();
        SpannableString spannableString = new SpannableString(getString(R.string.login_reg_tv));
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FB7C78"));
        spannableString.setSpan(colorSpan, 3, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvReg.setText(spannableString);
    }

    @OnClick(R.id.btn_login_submit)
    void submit() {
        if (FormCheckUtils.checkPhoneEmpty(etLoginPhone.getText().toString())) {
            return;
        }
        if (FormCheckUtils.checkPasswordEmpty(etLoginPassword.getText().toString())) {
            return;
        }
        if (FormCheckUtils.checkPasswordFormat(etLoginPassword.getText().toString())) {
            return;
        }
        mPresenter.login(etLoginPhone.getText().toString(), etLoginPassword.getText().toString(), true, false);
    }

    /**
     * 登录检查本地存储信息
     */
    private void checkLocalStorage() {
        String username = SPUtils.getString(Constant.SP_KEY_USERNAME, "");
        String password = SPUtils.getString(Constant.SP_KEY_PASSWORD, "");
        //本地存储只有用户名没有密码的时候将用户名数据绑到控件上
        if (!TextUtils.isEmpty(username) && TextUtils.isEmpty(password)) {
            etLoginPhone.setText(username);
            return;
        }
    }

    @OnClick({R.id.btn_forget, R.id.tv_reg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_forget:
                startActivity(new Intent(LoginActivity.this, ForgetActivity.class));
                break;
            case R.id.tv_reg:
                Intent intent = new Intent(LoginActivity.this, RegActivity.class);
                intent.putExtra(Constant.INTENT_DATA_KEY_DIRECTION, Constant.DIRECTION_LOGIN_TO_REG);
                startActivity(intent);
                break;
        }

    }

}
