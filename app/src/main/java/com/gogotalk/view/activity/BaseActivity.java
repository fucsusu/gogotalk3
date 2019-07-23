package com.gogotalk.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gogotalk.app.AiRoomApplication;
import com.gogotalk.app.AppManager;
import com.gogotalk.di.components.ActivityComponent;
import com.gogotalk.di.components.DaggerActivityComponent;
import com.gogotalk.di.modules.ActivityModule;
import com.gogotalk.presenter.BaseContract;
import com.gogotalk.util.AppUtils;
import com.gogotalk.view.widget.LoadingDialog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


import javax.inject.Inject;

import butterknife.ButterKnife;

public abstract class BaseActivity<T extends BaseContract.Presenter> extends AppCompatActivity implements BaseContract.View {
    private List<WeakReference<Activity>> mWeakReferenceList = new ArrayList<>();
    @Inject
    T mPresenter;
    LoadingDialog.Builder loadingDialogBuilder;
    LoadingDialog loadingDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtils.hideVirtualKeyView(this);
        loadingDialogBuilder = new LoadingDialog.Builder(this);
        loadingDialog = loadingDialogBuilder.create();
        loadingDialogBuilder.setShowMessage(true);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initInject();
        if (mPresenter != null)
            mPresenter.attachView(this);
        initView();
        getIntentData();
        AppManager.getAppManager().addActivity(this);
    }
    protected ActivityComponent getActivityComponent() {
        return DaggerActivityComponent.builder()
                .netComponent(AiRoomApplication.get(this).getNetComponent())
                .activityModule(getActivityModule())
                .build();
    }
    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }
    protected abstract int getLayoutId();
    protected abstract void initInject();
    protected void initView(){}

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
        if (mPresenter != null)
            mPresenter.detachView();
        if(loadingDialog!=null){
            loadingDialog.dismiss();
            loadingDialogBuilder = null;
            loadingDialog=null;
        }
        super.onDestroy();
    }

    public void getIntentData(){
        if(getIntent()==null)return;
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
