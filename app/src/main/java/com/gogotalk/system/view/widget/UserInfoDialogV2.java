package com.gogotalk.system.view.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.gogotalk.system.R;
import com.gogotalk.system.model.util.Constant;
import com.gogotalk.system.util.ToastUtils;
import com.gogotalk.system.view.activity.SelectNameActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import jsc.kit.wheel.dialog.DateTimeWheelDialog;

public class UserInfoDialogV2 extends ABBaseDialog {

    public UserInfoDialogV2(Context context) {
        super(context);
    }

    public UserInfoDialogV2(Context context, int theme) {
        super(context, theme);
    }

    private static OnSaveClickLisener saveClickLisener;

    public static void setSaveClickLisener(OnSaveClickLisener saveClickLisener) {
        UserInfoDialogV2.saveClickLisener = saveClickLisener;
    }

    public interface OnSaveClickLisener {
        void onClick(int sex, String name,String birthday);
    }

    public static class Builder {
        private View layout;
        private UserInfoDialogV2 dialog;
        private TextView tvName;
        private RadioGroup rgSex;
        private LinearLayout layoutName;
        private LinearLayout layoutBirthday;
        private LinearLayout btnSave;
        private int currentSex = 1;
        DateTimeBottomDialog timeBottomDialog = null;
        TextView tvBirthday;
        /**
         * 生成时间选择底部弹出框
         * @return
         */
        private DateTimeBottomDialog createDialog(Context context,TextView textView) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, 1900);
            calendar.set(Calendar.MONTH, 0);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            Date startDate = calendar.getTime();
            calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) + 3);
            Date endDate = calendar.getTime();

            DateTimeBottomDialog dialog = new DateTimeBottomDialog(context);
            dialog.show();
            dialog.setTitle("选择出生日期");
            int config = DateTimeWheelDialog.SHOW_YEAR_MONTH_DAY;
            dialog.configShowUI(config);
            dialog.setCancelButton("取消", null);
            dialog.setOKButton("确定", new DateTimeWheelDialog.OnClickCallBack() {
                @Override
                public boolean callBack(View v, @NonNull Date selectedDate) {
                    if (selectedDate.after(new Date())) {
                        dialog.updateSelectedDate(new Date());
                        return true;
                    }
                    textView.setText(new SimpleDateFormat("yyyy-MM-dd").format(selectedDate));
                    return false;
                }
            });
            dialog.setDateArea(startDate, endDate, true);
            dialog.updateSelectedDate(new Date());
            return dialog;
        }
        public Builder(final Context context) {
            dialog = new UserInfoDialogV2(context, R.style.Dialog);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.dialog_user_info_v2, null);
            layoutName = layout.findViewById(R.id.layout_select_name);
            layoutBirthday = layout.findViewById(R.id.layout_birthday);
            tvBirthday = layout.findViewById(R.id.tv_birthday);
            layoutBirthday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (timeBottomDialog == null) {
                        timeBottomDialog = createDialog(context,tvBirthday);
                    } else {
                        timeBottomDialog.show();
                    }
                }
            });
            layoutName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SelectNameActivity.class);
                    intent.putExtra(Constant.INTENT_DATA_KEY_SEX, currentSex);
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(0, 0);
                }
            });
            tvName = layout.findViewById(R.id.tv_name);
            rgSex = layout.findViewById(R.id.rg_sex);
            rgSex.check(R.id.rb_man);
            rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if (i == R.id.rb_man) {
                        currentSex = 1;
                    } else {
                        currentSex = 0;
                    }
                    tvName.setText("");
                }
            });
            btnSave = layout.findViewById(R.id.btn_save);
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (saveClickLisener != null) {
                        if (TextUtils.isEmpty(tvName.getText().toString())) {
                            ToastUtils.showLongToast(context, "请选择英文名！");
                            return;
                        }
                        saveClickLisener.onClick(currentSex, tvName.getText().toString(),tvBirthday.getText().toString());
                    }
                }
            });
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        public Builder setName(String name) {
            tvName.setText(name);
            return this;
        }
        public Builder setBirthday(String birthday) {
            tvBirthday.setText(birthday);
            return this;
        }
        public Builder setSex(int sex) {
            if (sex == 0) {
                rgSex.check(R.id.rb_women);
            } else if (sex == 1) {
                rgSex.check(R.id.rb_man);
            }
            return this;
        }

        public UserInfoDialogV2 create() {
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

