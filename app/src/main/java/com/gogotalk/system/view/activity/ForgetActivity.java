package com.gogotalk.system.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.gogotalk.system.R;
import com.gogotalk.system.presenter.ForgetContract;
import com.gogotalk.system.presenter.ForgetPresenter;
import com.gogotalk.system.presenter.LoginContract;
import com.gogotalk.system.presenter.LoginPresenter;
import com.gogotalk.system.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 找回密码
 */
public class ForgetActivity extends BaseActivity<ForgetPresenter> implements ForgetContract.View {


    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.btn_code)
    Button btnCode;
    @BindView(R.id.btn_next)
    Button btnNext;
    CountDownTimer countDownTimer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        countDownTimer = new CountDownTimer(60*1000,1000) {
            @Override
            public void onTick(long l) {
                btnCode.setText(((l/1000)<10?"0"+(l/1000):(l/1000))+" S");
                btnCode.setEnabled(false);
            }

            @Override
            public void onFinish() {
                btnCode.setText("获取验证码");
                btnCode.setEnabled(true);
            }
        };
    }


    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget;
    }


    @OnClick({R.id.btn_code, R.id.btn_next,R.id.btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_code:
                if(TextUtils.isEmpty(etPhone.getText())){
                    ToastUtils.showShortToast(ForgetActivity.this,"请填写手机号码！");
                    return;
                }
                countDownTimer.start();
                mPresenter.sendCode(etPhone.getText().toString());
                break;
            case R.id.btn_next:
                if(TextUtils.isEmpty(etPhone.getText())){
                    ToastUtils.showShortToast(ForgetActivity.this,"请填写手机号码！");
                    return;
                }
                if(TextUtils.isEmpty(etCode.getText())){
                    ToastUtils.showShortToast(ForgetActivity.this,"请填写验证码！");
                    return;
                }
                if(!etCode.getText().toString().matches("[0-9]{4}")){
                    ToastUtils.showShortToast(ForgetActivity.this,"验证码格式不正确！");
                    return;
                }
                mPresenter.checkCode(etPhone.getText().toString(),etCode.getText().toString());
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    @Override
    public void onCheckCodeSuccess() {
        Intent intent = new Intent(ForgetActivity.this, UpdatePasswordActivity.class);
        intent.putExtra("phone",etPhone.getText().toString());
        startActivity(intent);
    }
}
