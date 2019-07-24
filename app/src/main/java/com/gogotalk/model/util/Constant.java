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
    //老师名字
    public static final String INTENT_DATA_KEY_TEACHER_NAME = "teachername";
    //文件下载路径
    public static final String INTENT_DATA_KEY_DOWNLOAD_FILE_PATH = "LessonFile";

    //媒体信息标识
    public static final int HANDLE_INFO_CLASS_BEGIN = 1;
    public static final int HANDLE_INFO_ANSWER = 2;
    public static final int HANDLE_INFO_NEXTPAGE = 3;
    public static final int HANDLE_INFO_MIKE = 4;
    public static final int HANDLE_INFO_JB = 5;
    //老师的streamId的标识
    public static final String FLAG_TEACHER = "s-AI-teacher-";
    //SDK常量
    public static String appid = "2456789202";
    public static byte[] appSign = new byte[]{(byte) 0xf7, (byte) 0x3b, (byte) 0x2e, (byte) 0x64, (byte) 0xe8, (byte) 0x7f, (byte) 0x2d, (byte) 0xdc, (byte) 0x40,
            (byte) 0x59, (byte) 0x0c, (byte) 0x7b, (byte) 0xcd, (byte) 0xa4, (byte) 0x24, (byte) 0x45, (byte) 0x0d, (byte) 0x02, (byte) 0xb2, (byte) 0x5e,
            (byte) 0x05, (byte) 0xdb, (byte) 0x2d, (byte) 0x32, (byte) 0x68, (byte) 0xd5, (byte) 0xd9, (byte) 0x45, (byte) 0x7a, (byte) 0x39, (byte) 0xad,
            (byte) 0x9e};

}
