package com.gogotalk.model.util;

import android.widget.Toast;

import com.gogotalk.presenter.BaseContract;
import com.gogotalk.util.ToastUtils;

import io.reactivex.subscribers.ResourceSubscriber;

public abstract class CommonSubscriber<T> extends ResourceSubscriber<T> {
    private BaseContract.View mView;

    protected CommonSubscriber(BaseContract.View view){
        this.mView = view;
    }

    @Override
    public void onError(Throwable e) {
        if (mView == null) {
            return;
        }
        if(isLoading()){
            mView.hideLoading();
        }
        if(isError()){
            ToastUtils.showShortToast(mView.getActivity(),e.getMessage());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isLoading())
        mView.showLoading(null);
    }

    public boolean isLoading(){
        return true;
    }
    public boolean isError(){
        return true;
    }
    @Override
    public void onComplete() {
        if(isLoading())
        mView.hideLoading();
    }
}