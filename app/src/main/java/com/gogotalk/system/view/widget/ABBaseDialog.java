package com.gogotalk.system.view.widget;

import android.app.Dialog;
import android.content.Context;

import com.gogotalk.system.util.AppUtils;

public abstract class ABBaseDialog extends Dialog {
    public ABBaseDialog(Context context) {
        super(context);
    }

    public ABBaseDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ABBaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    @Override
    public void show() {
        AppUtils.focusNotAle(this.getWindow());
        super.show();
        AppUtils.fullScreenImmersive(this.getWindow());
        AppUtils.clearFocusNotAle(this.getWindow());
    }
}
