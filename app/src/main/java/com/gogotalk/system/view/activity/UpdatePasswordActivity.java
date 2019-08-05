package com.gogotalk.system.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.gogotalk.system.R;
import com.gogotalk.system.app.AppManager;
import com.gogotalk.system.model.util.Constant;
import com.gogotalk.system.presenter.UpdatePasswordContract;
import com.gogotalk.system.presenter.UpdatePasswordPresenter;
import com.gogotalk.system.util.FormCheckUtils;
import com.gogotalk.system.util.SPUtils;
import com.gogotalk.system.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 找回密码
 */
public class UpdatePasswordActivity extends BaseActivity<UpdatePasswordPresenter> implements UpdatePasswordContract.View {


    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.et_password_01)
    EditText etPassword01;
    @BindView(R.id.et_password_02)
    EditText etPassword02;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    String phone;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        phone = getIntent().getStringExtra(Constant.INTENT_DATA_KEY_PHONE);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_update_password;
    }


    @OnClick({R.id.btn_submit,R.id.btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                if (FormCheckUtils.checkPasswordEmpty(etPassword01.getText().toString())) {
                    return;
                }
                if (FormCheckUtils.checkTwoPasswordEmpty(etPassword02.getText().toString())) {
                    return;
                }
                if (FormCheckUtils.checkPasswordSame(etPassword01.getText().toString(),etPassword02.getText().toString())) {
                    return;
                }
                mPresenter.updatePassword(phone, etPassword01.getText().toString());
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    @Override
    public void onUpdatePasswordSuccess() {
        SPUtils.remove(Constant.SP_KEY_PASSWORD);
        SPUtils.remove(Constant.SP_KEY_USERTOKEN);
        SPUtils.remove(Constant.SP_KEY_USERINFO);
        AppManager.getAppManager().finishAllActivity();
        startActivity(new Intent(UpdatePasswordActivity.this, LoginActivity.class));

    }
}
