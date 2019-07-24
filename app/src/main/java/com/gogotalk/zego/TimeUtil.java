package com.gogotalk.zego;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Copyright (C)
 * <p>
 * FileName: TimeUtil
 * <p>
 * Author: 赵小钧
 * <p>
 * Date: 2019\6\24 0024 11:22
 */
public class TimeUtil {
    static final private SimpleDateFormat sFormat = new SimpleDateFormat("yyMMddHHmmssSSS");
    static final private SimpleDateFormat sLogFormat = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");

    static public String getNowTimeStr() {
        return sFormat.format(new Date());
    }

    static public String getLogStr() {
        return sLogFormat.format(new Date());
    }
}
