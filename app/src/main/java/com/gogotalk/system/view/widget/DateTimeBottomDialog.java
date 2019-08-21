package com.gogotalk.system.view.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

