package com.gogotalk.system.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gogotalk.system.R;
import com.gogotalk.system.util.AppUtils;

public class LoadingDialog extends Dialog {

    public LoadingDialog(@NonNull Context context) {
        super(context);
    }

    public LoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected LoadingDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    @Override
    public void show() {
        AppUtils.focusNotAle(this.getWindow());
        super.show();
        AppUtils.fullScreenImmersive(this.getWindow());
        AppUtils.clearFocusNotAle(this.getWindow());
    }
    //2,创建静态内部类Builder，将dialog的部分属性封装进该类
    public static class Builder{
        TextView msgText;
        private Context context;
        //提示信息
        private String message;
        //是否展示提示信息
        private boolean isShowMessage=true;
        //是否按返回键取消
        private boolean isCancelable=true;
        //是否取消
        private boolean isCancelOutside=false;
        LoadingDialog loadingDailog;
        View view;
        public Builder(Context context) {
            this.context = context;
            LayoutInflater inflater = LayoutInflater.from(context);
            view=inflater.inflate(R.layout.dialog_loading,null);
            //设置带自定义主题的dialog
            loadingDailog=new LoadingDialog(context,R.style.LoadingDialog);
            msgText= (TextView) view.findViewById(R.id.tipTextView);
        }

        /**
         * 设置提示信息
         * @param message
         * @return
         */
        public Builder setMessage(String message){
            if(isShowMessage){
                if(!TextUtils.isEmpty(message)){
                    msgText.setText(message);
                }
            }
            return this;
        }

        /**
         * 设置是否显示提示信息
         * @param isShowMessage
         * @return
         */
        public Builder setShowMessage(boolean isShowMessage){
            if(isShowMessage){
                msgText.setVisibility(View.VISIBLE);
            }else{
                msgText.setVisibility(View.GONE);
            }
            return this;
        }

        /**
         * 设置是否可以按返回键取消
         * @param isCancelable
         * @return
         */
        public Builder setCancelable(boolean isCancelable){
            this.isCancelable=isCancelable;
            return this;
        }

        /**
         * 设置是否可以取消
         * @param isCancelOutside
         * @return
         */
        public Builder setCancelOutside(boolean isCancelOutside){
            this.isCancelOutside=isCancelOutside;
            return this;
        }

        //创建Dialog
        public LoadingDialog create(){
            loadingDailog.setContentView(view);
            loadingDailog.setCancelable(isCancelable);
            loadingDailog.setCanceledOnTouchOutside(isCancelOutside);
            return loadingDailog;
        }
    }

}