package com.gogotalk.system.pay;

/**
 * @author whj
 * @Description:
 * @date 2019/7/31 11:56
 */
public class PayFactory extends BaseFactory {
    @Override
    public <T extends BasePay> T createPay(Class<T> tClass) {
        BasePay basePay = null;
        String className = tClass.getName();
        try {
            basePay = (BasePay) Class.forName(className).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return (T) basePay;
    }
}