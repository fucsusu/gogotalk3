package com.gogotalk.system.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gogotalk.system.R;
import com.gogotalk.system.model.util.Constant;
import com.gogotalk.system.presenter.RegContract;
import com.gogotalk.system.presenter.RegPresenter;
import com.gogotalk.system.util.FormCheckUtils;
import com.gogotalk.system.util.SPUtils;
import com.gogotalk.system.util.ToastUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class RegActivity extends BaseActivity<RegPresenter> implements RegContract.View {
    @BindView(R.id.et_reg_phone)
    EditText etRegPhone;
    @BindView(R.id.et_reg_code)
    EditText etRegCode;
    @BindView(R.id.btn_code)
    Button btnCode;
    @BindView(R.id.et_reg_password)
    EditText etRegPassword;
    @BindView(R.id.btn_reg_submit)
    Button btnRegSubmit;
    @BindView(R.id.tv_reg_login)
    TextView tvRegLogin;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    CountDownTimer countDownTimer;
//    int direct;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_reg;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initView() {
        super.initView();
        initRegToLoginTxt();
//        if (isFirstReg()) {
//            btnBack.setVisibility(View.GONE);
//        } else {
//            btnBack.setVisibility(View.VISIBLE);
//        }
    }

//    private boolean isFirstReg() {
//        if (direct == 0) {
//            return true;
//        }
//        return false;
//    }

//    @Override
//    public void getIntentData() {
//        super.getIntentData();
//        direct = getIntent().getIntExtra(Constant.INTENT_DATA_KEY_DIRECTION, 0);
//    }

    private void initRegToLoginTxt() {
        SpannableString spannableString = new SpannableString(getString(R.string.reg_login_txt));
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FB7C78"));
        spannableString.setSpan(new NoLineCllikcSpan() {
            @Override
            public void onClick(View view) {
//                if (isFirstReg()) {
                    startActivity(new Intent(RegActivity.this, LoginActivity.class));
//                }
                finish();
            }
        }, 6, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(colorSpan, 6, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvRegLogin.setHighlightColor(Color.parseColor("#00FFFFFF"));
        tvRegLogin.setMovementMethod(LinkMovementMethod.getInstance());
        tvRegLogin.setText(spannableString);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                btnCode.setBackgroundResource(R.mipmap.bg_reg_code_selected);
                btnCode.setText(((l / 1000) < 10 ? "0" + (l / 1000) : (l / 1000)) + " S");
                btnCode.setTextColor(Color.WHITE);
                btnCode.setEnabled(false);
            }

            @Override
            public void onFinish() {
                btnCode.setBackgroundResource(R.mipmap.bg_reg_code_normal);
                btnCode.setTextColor(Color.parseColor("#FF5F7A"));
                btnCode.setText("获取验证码");
                btnCode.setEnabled(true);
            }
        };
    }

    @OnClick({R.id.btn_code, R.id.btn_reg_submit, R.id.btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_code:
                if (FormCheckUtils.checkPhoneEmpty(etRegPhone.getText().toString())) {
                    return;
                }
                countDownTimer.start();
                mPresenter.sendCode(etRegPhone.getText().toString());
                break;
            case R.id.btn_reg_submit:
                if (FormCheckUtils.checkPhoneEmpty(etRegPhone.getText().toString())) {
                    return;
                }
                if (FormCheckUtils.checkCodeEmpty(etRegCode.getText().toString())) {
                    return;
                }
                if (FormCheckUtils.checkCodeFormat(etRegCode.getText().toString())) {
                    return;
                }
                if (FormCheckUtils.checkPasswordEmpty(etRegPassword.getText().toString())) {
                    return;
                }
                if (FormCheckUtils.checkPasswordFormat(etRegPassword.getText().toString())) {
                    return;
                }
                mPresenter.regUser(etRegPhone.getText().toString(), etRegCode.getText().toString(), etRegPassword.getText().toString());
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }



    class NoLineCllikcSpan extends ClickableSpan {

        public NoLineCllikcSpan() {
            super();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
        }
    }
}
