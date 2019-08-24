package com.gogotalk.system.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gogotalk.system.R;
import com.gogotalk.system.util.AppUtils;

public class CommonDialog extends ABBaseDialog {

    public CommonDialog(Context context) {
        super(context);
    }

    public CommonDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private View layout;
        private CommonDialog dialog;
        private String positiveButtonText;
        private String negativeButtonText;
        private TextView tvMessage;

        public Builder(Context context) {
            dialog = new CommonDialog(context, R.style.CustemDialog);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.dialog_common, null);
            tvMessage = layout.findViewById(R.id.tv_message);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        //设置提示信息
        public CommonDialog.Builder setMessage(String msg) {
            tvMessage.setText(msg);
            return this;
        }

        public Builder(Context context, View layout) {
            dialog = new CommonDialog(context, R.style.Dialog);
            this.layout = layout;
            tvMessage = layout.findViewById(R.id.tv_message);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        //设置正确按钮点击事件
        public CommonDialog.Builder setPositiveButton(String positiveButtonText, View.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            layout.findViewById(R.id.btn_ok).setOnClickListener(listener);
            return this;
        }

        //设置取消按钮点击事件
        public CommonDialog.Builder setNegativeButton(String negativeButtonText, View.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            layout.findViewById(R.id.btn_cancel).setOnClickListener(listener);
            return this;
        }

        //设置两按钮提示
        public CommonDialog createOneButtonDialog() {
            if (positiveButtonText != null && !"".equals(positiveButtonText)) {
                ((Button) layout.findViewById(R.id.btn_ok)).setText(positiveButtonText);
            }
            layout.findViewById(R.id.btn_cancel).setVisibility(View.GONE);
            create();
            return dialog;
        }

        //设置两按钮提示
        public CommonDialog createTwoButtonDialog() {
            if (positiveButtonText != null && !"".equals(positiveButtonText)) {
                ((Button) layout.findViewById(R.id.btn_ok)).setText(positiveButtonText);
            }
            if (negativeButtonText != null && !"".equals(negativeButtonText)) {
                ((Button) layout.findViewById(R.id.btn_cancel)).setText(negativeButtonText);
            }
            create();
            return dialog;
        }

        //设置两按钮提示
        public CommonDialog createTwoButtonDialog(boolean flag) {
            if (positiveButtonText != null && !"".equals(positiveButtonText)) {
                ((Button) layout.findViewById(R.id.btn_ok)).setText(positiveButtonText);
            }
            if (negativeButtonText != null && !"".equals(negativeButtonText)) {
                ((Button) layout.findViewById(R.id.btn_cancel)).setText(negativeButtonText);
            }
            create(flag);
            return dialog;
        }

        public CommonDialog create() {
            dialog.setContentView(layout);
            dialog.setCancelable(true);     //用户可以点击手机Back键取消对话框显示
            dialog.setCanceledOnTouchOutside(false);        //用户不能通过点击对话框之外的地方取消对话框显示
            return dialog;
        }

        public CommonDialog create(boolean flag) {
            dialog.setContentView(layout);
            dialog.setCancelable(flag);     //用户可以点击手机Back键取消对话框显示
            dialog.setCanceledOnTouchOutside(false);        //用户不能通过点击对话框之外的地方取消对话框显示
            return dialog;
        }

        public CommonDialog getDialog() {
            return dialog;
        }
    }

}

