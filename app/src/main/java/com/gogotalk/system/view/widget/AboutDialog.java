package com.gogotalk.system.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gogotalk.system.R;
import com.gogotalk.system.util.AppUtils;

public class AboutDialog extends ABBaseDialog {

    public AboutDialog(Context context) {
        super(context);
    }

    public AboutDialog(Context context, int theme) {
        super(context, theme);
    }


    public static class Builder {
        private View layout;
        private AboutDialog dialog;

        public Builder(Context context) {
            dialog = new AboutDialog(context, R.style.CustemDialog);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.dialog_about, null);
            TextView tv_version = layout.findViewById(R.id.tv_version);
            TextView tv_private = layout.findViewById(R.id.dialog_about_private);
            SpannableStringBuilder stringBuilder=new SpannableStringBuilder(context.getString(R.string.poivacy_statement));
            UnderlineSpan un=new UnderlineSpan();
            ForegroundColorSpan foregroundColorSpan=new ForegroundColorSpan(Color.parseColor("#87CEFF"));
            stringBuilder.setSpan(foregroundColorSpan,0,4,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            stringBuilder.setSpan(un,0,4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_private.setText(stringBuilder);
            tv_version.setText(context.getString(R.string.dialog_about_us_version_label) + AppUtils.getAppVersionName(context));
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        public AboutDialog create(View.OnClickListener onClickListener) {
            dialog.setContentView(layout);
            dialog.setCancelable(true);     //用户可以点击手机Back键取消对话框显示
            dialog.setCanceledOnTouchOutside(false);        //用户不能通过点击对话框之外的地方取消对话框显示
            layout.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            layout.findViewById(R.id.dialog_about_private).setOnClickListener(onClickListener);
            return dialog;
        }

    }

}

