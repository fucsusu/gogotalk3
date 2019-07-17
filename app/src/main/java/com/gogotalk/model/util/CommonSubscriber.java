package com.gogotalk.model.util;

import com.gogotalk.presenter.BaseContract;
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

    @Override
    public void onComplete() {
        if(isLoading())
        mView.hideLoading();
    }
}