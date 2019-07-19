package com.gogotalk.model.util;


public class Constant {
    /**
     * app网络请求基路径
     * PATH_RELEASE_URL 正式发布路径
     * PATH_DEBUG_URL   测试发布路径
     */
    public static final String PATH_RELEASE_URL = "https://hbrapi.gogo-talk.com";
    public static final String PATH_DEBUG_URL = "https://hftestapi.gogo-talk.com";

    public static final int HTTP_SUCCESS_CODE = 1;
    public static final int HTTP_FAIL_CODE = 0;
    public static final int HTTP_TOKEN_EXPIRE_CODE = 1002;

    public static final String SP_KEY_USERNAME = "username";
    public static final String SP_KEY_PASSWORD = "password";
    public static final String SP_KEY_USERTOKEN = "userToken";
    public static final String SP_KEY_USERINFO = "userInfo";

    public static final String INTENT_DATA_KEY_RECORD_ID = "recordId";
    public static final String INTENT_DATA_KEY_VIDEO_URL = "videoUrl";
    public static final String INTENT_DATA_KEY_GAME_URL = "gameUrl";

}
