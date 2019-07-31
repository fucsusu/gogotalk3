package com.gogotalk.system.pay;

/**
 * @author whj
 * @Description:
 * @date 2019/7/31 11:56
 */
public abstract class BaseFactory {
    public abstract <T extends BasePay> T createPay(Class<T> tClass);
}
