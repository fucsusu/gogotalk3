package com.gogotalk.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.widget.EditText;
import androidx.annotation.Nullable;
import com.gogotalk.R;
import com.gogotalk.model.util.Constant;
import com.gogotalk.presenter.LoginContract;
import com.gogotalk.presenter.LoginPresenter;
import com.gogotalk.util.RegexUtils;
import com.gogotalk.util.SPUtils;
import com.gogotalk.util.ToastUtils;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.et_login_phone)
    EditText etLoginPhone;
    @BindView(R.id.et_login_password)
    EditText etLoginPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkLocalStorage();
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }


    @OnClick(R.id.btn_login_submit) void submit() {
        if(validateInputData()){
            mPresenter.login(etLoginPhone.getText().toString(),etLoginPassword.getText().toString(),true);
        }
    }
    /**
     * 登录检查本地存储信息
     */
    private void checkLocalStorage(){
        String username = SPUtils.getString(Constant.SP_KEY_USERNAME, "");
        String password = SPUtils.getString(Constant.SP_KEY_PASSWORD, "");
//        Logger.i("========="+username+"========"+password+"========");
        //本地存储有用户名和密码做登录操作
//        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
//            mPresenter.login(username,password);
//            return;
//        }
        //本地存储只有用户名没有密码的时候将用户名数据绑到控件上
        if(!TextUtils.isEmpty(username)&&TextUtils.isEmpty(password)){
            etLoginPhone.setText(username);
            return;
        }
    }

    /**
     * 校验表单输入信息
     * @return
     */
    private boolean validateInputData(){
        String phone = etLoginPhone.getText().toString().trim();
        String password = etLoginPassword.getText().toString().trim();
        if(TextUtils.isEmpty(phone)||TextUtils.isEmpty(password)){
            ToastUtils.showLongToast(LoginActivity.this,R.string.login_toast_empty_msg);
            return false;
        }
        if(!RegexUtils.isMobileExact(phone)){
            ToastUtils.showLongToast(LoginActivity.this,R.string.login_toast_phone_error_msg);
            return false;
        }
        return true;
    }
}
