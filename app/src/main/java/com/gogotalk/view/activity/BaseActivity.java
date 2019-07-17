package com.gogotalk.view.activity;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.gogotalk.presenter.BaseContract;
import com.gogotalk.util.AppUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements BaseContract.View {
    private List<WeakReference<Activity>> mWeakReferenceList = new ArrayList<>();
    BaseContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtils.hideVirtualKeyView(this);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initPresenter();
        mPresenter = getPresenter();
        initView();
    }
    protected abstract int getLayoutId();
    protected abstract void initPresenter();
    protected void initView(){}
    protected abstract BaseContract.Presenter getPresenter();

    @Override
    public Activity getActivity() {
        if (mWeakReferenceList.size() == 0) {
            WeakReference<Activity> weakReference = new WeakReference<Activity>(this);
            mWeakReferenceList.add(weakReference);
            return weakReference.get();
        } else {
            WeakReference<Activity> weakReference = mWeakReferenceList.get(0);
            return weakReference.get();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
            mPresenter = null;
            System.gc();
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
