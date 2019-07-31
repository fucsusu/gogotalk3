package com.gogotalk.system.pay;

/**
 * @author whj
 * @Description:
 * @date 2019/7/31 11:54
 */
public interface OnPayCallBack {
    void onPaySuccess(String orderId);
    void onPayFailed();
}
