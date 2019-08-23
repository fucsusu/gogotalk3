package com.gogotalk.system.model.util;

public class Constant {
    public static final boolean DEBUG = true;
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
    public static final String INTENT_DATA_KEY_SEX = "sex";
    public static final String INTENT_DATA_KEY_NAME = "name";
    public static final String INTENT_DATA_KEY_PHONE = "phone";
    public static final String INTENT_DATA_KEY_DIRECTION = "direction";
    public static final String INTENT_DATA_KEY_LEVEL = "level";

    public static final int DIRECTION_SELECTNAME_TO_HOME = 1;
    public static final int DIRECTION_LOGIN_TO_REG = 2;
    public static final int DIRECTION_LEVEL_TO_SELECTNAME = 3;
    public static final int DIRECTION_SELECTNAME_TO_LEVEL = 4;
    public static final int DIRECTION_LEVEL_TO_CLASS = 5;
    public static final int DIRECTION_CLASS_TO_LEVEL = 6;
    //老师名字
    public static final String INTENT_DATA_KEY_TEACHER_NAME = "teachername";
    //文件下载路径
    public static final String INTENT_DATA_KEY_DOWNLOAD_FILE_PATH = "LessonFile";
    // 教室ID
    public static final String INTENT_DATA_KEY_CLASS_ID = "AttendLessonID";
    //开始上课时间
    public static final String INTENT_DATA_KEY_BEGIN_TIME = "LessonTime";

    public static final String INTENT_DATA_KEY_MY = "my";
    public static final String INTENT_DATA_KEY_OTHER = "other";
    //其他学生的名字
    public static final String INTENT_DATA_KEY_OTHER_NAME = "othername";
    //其他学生的id
    public static final String INTENT_DATA_KEY_OTHER_NAME_ID = "othernameid";
    //教室的详情ID
    public static final String INTENT_DATA_KEY_DETADIL_RECORDID = "DetailRecordID";
    //自己的奖杯数量
    public static final String INTENT_DATA_KEY_OWNJBNUM = "MyGiftCupNum";
    //其他人的奖杯数量
    public static final String INTENT_DATA_KEY_OTHERJBNUM = "OtherGiftCupNum";

    //信令标识
    public static final String MESSAGE_SHOW_JB = "show_jb";
    //获取答题结果
    public static final String MESSAGE_GET_PAGE = "getpageindex";
    //发送答题结果
    public static final String MESSAGE_RESULT = "result";
    //发送答题结果
    public static final String MESSAGE_ANSWER = "answer";

    public static final String ACTION_AUX = "aux";

    public static final String ACTION_ANSWER = "open_answer";

    public static final String ACTION_NEXTPAGE = "next_page";

    public static final String ACTION_OPENMIKE = "open_mic";

    //apk下载默认前缀
    public static final String DOWNLOAD_APK = "gogotalk";
    //媒体信息标识
    public static final int HANDLE_INFO_CLASS_BEGIN = 1;
    public static final int HANDLE_INFO_ANSWER = 2;
    public static final int HANDLE_INFO_NEXTPAGE = 3;
    public static final int HANDLE_INFO_MIKE = 4;
    public static final int HANDLE_INFO_JB = 5;
    public static final int HANDLE_INFO_VOICE = 6;
    //老师的streamId的标识
    public static final String FLAG_TEACHER = "s-AI-teacher-";
    //SDK常量
    public static String appid = "2456789202";
    //    public static String appid = "3335035834";
    public static byte[] appSign = new byte[]{(byte) 0xf7, (byte) 0x3b, (byte) 0x2e, (byte) 0x64, (byte) 0xe8, (byte) 0x7f, (byte) 0x2d, (byte) 0xdc, (byte) 0x40,
            (byte) 0x59, (byte) 0x0c, (byte) 0x7b, (byte) 0xcd, (byte) 0xa4, (byte) 0x24, (byte) 0x45, (byte) 0x0d, (byte) 0x02, (byte) 0xb2, (byte) 0x5e,
            (byte) 0x05, (byte) 0xdb, (byte) 0x2d, (byte) 0x32, (byte) 0x68, (byte) 0xd5, (byte) 0xd9, (byte) 0x45, (byte) 0x7a, (byte) 0x39, (byte) 0xad,
            (byte) 0x9e};
//    public static byte[] appSign = new byte[]{(byte)0xe9,(byte)0xfb,(byte)0x5a,(byte)0x56,(byte)0x6f,(byte)0x6d,(byte)0xeb,(byte)0x44,
//        (byte)0xac,(byte)0x07,(byte)0x77,(byte)0x9b,(byte)0xee,(byte)0x99,(byte)0x52,(byte)0x19,
//        (byte)0x5d,(byte)0xd5,(byte)0xcb,(byte)0xa3,(byte)0xf4,(byte)0x74,(byte)0x16,(byte)0x70,
//        (byte)0x74,(byte)0x41,(byte)0x14,(byte)0x60,(byte)0x34,(byte)0x08,(byte)0xc8,(byte)0xa4};
}
