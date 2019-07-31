package com.gogotalk.system.pay;

import android.app.Activity;

/**
 * @author whj
 * @Description: 支付抽象基类
 * @date 2019/7/31 11:51
 */
public abstract class BasePay {
    public abstract void pay(Activity activity, String orderString, OnPayCallBack callBack);
}
