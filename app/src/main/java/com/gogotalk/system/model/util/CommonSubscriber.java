package com.gogotalk.system.model.util;

import android.util.Log;

import com.gogotalk.system.model.api.ApiService;
import com.gogotalk.system.model.exception.ApiException;
import com.gogotalk.system.model.exception.NoNetworkException;
import com.gogotalk.system.presenter.BaseContract;
import com.gogotalk.system.util.ToastUtils;

import io.reactivex.subscribers.ResourceSubscriber;

public abstract class CommonSubscriber<T> extends ResourceSubscriber<T> {
    private BaseContract.View mView;
    public boolean next = true;

    protected CommonSubscriber(BaseContract.View view, String message) {
        this.mView = view;
    }

    protected CommonSubscriber(BaseContract.View view) {
        this.mView = view;
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof ApiException && ((ApiException) e).getCode() == Constant.HTTP_FAIL_CODE) {
            onFail();
        }
        if (isHideLoading()) {
            mView.hideLoading();
        }
        if (!(e instanceof ApiException)) {
            ToastUtils.showShortToast(mView.getActivity(), "网络异常，请检查网络后重试");
            return;
        }
        if (isError()) {
            ToastUtils.showLongToast(mView.getActivity(), e.getMessage());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isShowLoading())
            mView.showLoading(getLoadingTxt());
    }

    public boolean isShowLoading() {
        return true;
    }

    public boolean isHideLoading() {
        return true;
    }

    public void setNext(boolean next) {
        this.next = next;
    }

    public boolean isError() {
        return true;
    }

    @Override
    public void onComplete() {
        if (isHideLoading())
            mView.hideLoading();
    }

    public void onFail() {
    }

    public String getLoadingTxt() {
        return "加载中...";
    }
}