package com.gogotalk.system.util;

import android.content.Context;
import android.text.TextUtils;

import com.gogotalk.system.R;
import com.gogotalk.system.app.AiRoomApplication;
import com.gogotalk.system.view.activity.UpdatePasswordActivity;

/**
 * 表单校验
 */
public class FormCheckUtils {
    static Context applicationContext = AiRoomApplication.getInstance().getApplicationContext();

    /**
     * 检查手机号是否为空
     * @param phone
     * @return
     */
    public static boolean checkPhoneEmpty(String phone){
        if(TextUtils.isEmpty(phone)){
            ToastUtils.showShortToast(applicationContext, applicationContext.getString(R.string.reg_phone_error_empty_toast_txt));
            return true;
        }
        return false;
    }

    /**
     * 检查验证码是否为空
     * @param code
     * @return
     */
    public static boolean checkCodeEmpty(String code){
        if(TextUtils.isEmpty(code)){
            ToastUtils.showShortToast(applicationContext, applicationContext.getString(R.string.reg_code_error_empty_toast_txt));
            return true;
        }
        return false;
    }

    /**
     * 检查密码是否为空
     * @param password
     * @return
     */
    public static boolean checkPasswordEmpty(String password){
        if(TextUtils.isEmpty(password)){
            ToastUtils.showShortToast(applicationContext, applicationContext.getString(R.string.reg_password_error_empty_toast_txt));
            return true;
        }
        return false;
    }
    /**
     * 检查再次密码是否为空
     * @param password
     * @return
     */
    public static boolean checkTwoPasswordEmpty(String password){
        if(TextUtils.isEmpty(password)){
            ToastUtils.showShortToast(applicationContext, applicationContext.getString(R.string.reg_password2_error_empty_toast_txt));
            return true;
        }
        return false;
    }

    /**
     * 检查两次输入密码是否一致
     * @param password01
     * @param password02
     * @return
     */
    public static boolean checkPasswordSame(String password01,String password02){
        if (!password01.equalsIgnoreCase(password02)) {
            ToastUtils.showShortToast(applicationContext, applicationContext.getString(R.string.reg_password_same_error_empty_toast_txt));
            return true;
        }
        return false;
    }
    /**
     * 检查验证码格式是否正确
     * @param code
     * @return
     */
    public static boolean checkCodeFormat(String code){
        if(!code.matches("[0-9]{4}")){
            ToastUtils.showShortToast(applicationContext, applicationContext.getString(R.string.reg_code_error_format_toast_txt));
            return true;
        }
        return false;
    }

    /**
     * 检查密码格式是否正确
     * @param password
     * @return
     */
    public static boolean checkPasswordFormat(String password){
        if(!password.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$")){
            ToastUtils.showShortToast(applicationContext, applicationContext.getString(R.string.reg_password_error_format_toast_txt));
            return true;
        }
        return false;
    }
}
