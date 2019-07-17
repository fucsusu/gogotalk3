package com.gogotalk.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.gogotalk.presenter.BaseContract;
import com.gogotalk.util.AppUtils;
import com.gogotalk.view.widget.LoadingDialog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements BaseContract.View {
    private List<WeakReference<Activity>> mWeakReferenceList = new ArrayList<>();
    BaseContract.Presenter mPresenter;
    LoadingDialog.Builder loadingDialogBuilder;
    LoadingDialog loadingDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtils.hideVirtualKeyView(this);
        loadingDialogBuilder = new LoadingDialog.Builder(this);
        loadingDialogBuilder.setShowMessage(true);
        loadingDialog = loadingDialogBuilder.create();
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
        if(loadingDialog!=null){
            loadingDialog.dismiss();
            loadingDialog=null;
        }
    }

    @Override
    public void showLoading(String msg) {
        if(!TextUtils.isEmpty(msg)){
            loadingDialogBuilder.setMessage(msg);
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }
}
