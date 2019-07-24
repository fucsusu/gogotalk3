package com.gogotalk.system.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gogotalk.system.R;
import com.gogotalk.system.util.AppUtils;

public class UserInfoDialog extends Dialog {

    public UserInfoDialog(Context context) {
        super(context);
    }
    public UserInfoDialog(Context context, int theme) {
        super(context, theme);
    }
    private static OnNameClickLisener onNameClickLisener;

    public void setOnNameClickLisener(OnNameClickLisener onNameClickLisener) {
        this.onNameClickLisener = onNameClickLisener;
    }

    public interface OnNameClickLisener{
        void onClick(int sex);
    }
    @Override
    public void show() {
        AppUtils.focusNotAle(this.getWindow());
        super.show();
        AppUtils.fullScreenImmersive(this.getWindow());
        AppUtils.clearFocusNotAle(this.getWindow());
    }
    public static class Builder {
        private View layout;
        private UserInfoDialog dialog;
        private ImageView ivHeader;
        private TextView tvName;
        private TextView tvSex;
        private TextView tvDate;
        private Context mContext;
        public Builder(Context context) {
            mContext = context;
            dialog = new UserInfoDialog(context, R.style.CustemDialog);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.dialog_user_info, null);
            ivHeader = layout.findViewById(R.id.iv_header);
            tvName = layout.findViewById(R.id.tv_name);
            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onNameClickLisener!=null){
                        if ("女".equals(tvSex.getText())) {
                            onNameClickLisener.onClick(0);
                        } else if ("男".equals(tvSex.getText())) {
                            onNameClickLisener.onClick(1);
                        }
                    }
                }
            });
            tvSex = layout.findViewById(R.id.tv_sex);
            tvDate = layout.findViewById(R.id.tv_date);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        public Builder setName(String name){
            tvName.setText(name);
            return  this;
        }
        public Builder setHeader(String imageUrl){
            if (imageUrl != null && !"".equals(imageUrl) && !"null".equals(imageUrl)) {
                Glide.with(mContext).load(imageUrl).placeholder(R.mipmap.ic_main_user_info_header_default)
                        .diskCacheStrategy(DiskCacheStrategy.NONE).into(ivHeader);
            } else {
                ivHeader.setImageResource(R.mipmap.ic_main_user_info_header_default);
            }
            return  this;
        }
        public Builder setSex(int sex){
            if (sex == 0) {
                tvSex.setText("女");
            } else if (sex == 1) {
                tvSex.setText("男");
            }
            return  this;
        }
        public Builder setDate(String date){
            tvDate.setText(date);
            return  this;
        }
        public UserInfoDialog create() {
            dialog.setContentView(layout);
            dialog.setCancelable(true);     //用户可以点击手机Back键取消对话框显示
            dialog.setCanceledOnTouchOutside(false);        //用户不能通过点击对话框之外的地方取消对话框显示
            layout.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            return dialog;
        }

    }

}

