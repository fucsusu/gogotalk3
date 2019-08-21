package com.gogotalk.system.view.widget;

import android.content.Context;

import androidx.annotation.NonNull;

import com.gogotalk.system.util.AppUtils;

import jsc.kit.wheel.dialog.DateTimeWheelDialog;

public class DateTimeBottomDialog extends DateTimeWheelDialog {


    public DateTimeBottomDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public void show() {
        AppUtils.focusNotAle(this.getWindow());
        super.show();
        AppUtils.fullScreenImmersive(this.getWindow());
        AppUtils.clearFocusNotAle(this.getWindow());
    }
}

