package com.gogotalk.zego;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * Copyright (C)
 * <p>
 * FileName: ZegoUtil
 * <p>
 * Author: 赵小钧
 * <p>
 * Date: 2019\6\24 0024 13:17
 */
public class ZegoUtil {
    /**
     * 字符串转换成 byte 数组
     * 主要用于 appSign 的转换
     * @param strSignKey
     * @return
     * @throws NumberFormatException
     */
    public static byte[] parseSignKeyFromString(String strSignKey) throws NumberFormatException {
        String[] keys = strSignKey.split(",");
        if (keys.length != 32) {
            AppLogger.getInstance().i(ZegoUtil.class, "appSign 格式非法");

            return null;
        }
        byte[] byteSignKey = new byte[32];
        for (int i = 0; i < 32; i++) {
            int data = Integer.valueOf(keys[i].trim().replace("0x", ""), 16);
            byteSignKey[i] = (byte) data;
        }
        return byteSignKey;
    }


    public static long parseAppIDFromString(String strAppID) throws NumberFormatException {

        // 使用正则表达式校验是否包含除数字以外的字符
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        boolean isInt = pattern.matcher(strAppID).matches();
        if (TextUtils.isEmpty(strAppID) || !isInt) {
            AppLogger.getInstance().i(ZegoUtil.class, "appID 格式非法");
            return 0;
        }

        return Long.parseLong(strAppID);
    }

    /**
     * 获取随机生成StreamID
     * @return
     */
    public static String getPublishStreamID(){
        return "s" + System.currentTimeMillis();
    }
}
