package com.gogotalk.view.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;

import com.gogotalk.R;
import com.gogotalk.presenter.BaseContract;
import com.gogotalk.presenter.MainContract;
import com.gogotalk.presenter.MainPresenter;

import javax.inject.Inject;

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }


}
