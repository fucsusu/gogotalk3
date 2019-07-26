package com.gogotalk.system.view.activity;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gogotalk.system.R;
import com.gogotalk.system.model.util.Constant;
import com.gogotalk.system.presenter.ClassRoomContract;
import com.gogotalk.system.presenter.ClassRoomPresenter;
import com.gogotalk.system.zego.ZGPublishHelper;
import com.gogotalk.system.util.AnimatorUtils;
import com.gogotalk.system.util.AppUtils;
import com.gogotalk.system.util.DateUtils;
import com.gogotalk.system.zego.AppLogger;
import com.gogotalk.system.zego.ZGBaseHelper;
import com.gogotalk.system.zego.ZGMediaSideInfoDemo;
import com.gogotalk.system.zego.ZGPlayHelper;
import com.orhanobut.logger.Logger;
import com.zego.zegoliveroom.constants.ZegoConstants;

import java.io.File;
import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.OnClick;

public class ClassRoomActivity extends BaseActivity<ClassRoomPresenter> implements ClassRoomContract.IClassRoomView, CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.id_mWebView_Class)
    public WebView webView;
    @BindView(R.id.id_mVideo1)
    public TextureView mTeacherTV;
    @BindView(R.id.id_mVideo2)
    public TextureView mOwnTV;
    @BindView(R.id.id_mVideo3)
    public TextureView mOtherTV;
    @BindView(R.id.id_mTime_Class)
    public TextView mTime;
    @BindView(R.id.id_mTeacherName_Class)
    public TextView mTeacherName;
    @BindView(R.id.id_mDJS_Class)
    public LinearLayout mLayouts;
    //自己视频开关
    @BindView(R.id.id_SXTCheck1_Class)
    public CheckBox mvideo_swtich_own;
    //其他学生视频开关
    @BindView(R.id.id_SXTCheck2_Class)
    public CheckBox mvideo_switch_other;
    //其他学生声音开关
    @BindView(R.id.id_SYCheck_Class)
    public CheckBox mvoice_switch_other;
    @BindView(R.id.id_mWJR1_Class)
    public ImageView mImgs1;
    @BindView(R.id.id_mWJR2_Class)
    public ImageView mImgs3;
    @BindView(R.id.id_mMyName_Class)
    public TextView mMyName;
    @BindView(R.id.id_mMyJB_Class)
    public TextView mMyJB;
    @BindView(R.id.jb_num_other_class)
    public TextView mOtherJBNum;
    @BindView(R.id.id_mYouName_Class)
    public TextView otherSNText;
    @BindView(R.id.id_mMkfPhoto_Class)
    public ImageView mMkfPhoto;

    @BindView(R.id.id_mImageJB_Class)
    public ImageView mJB;//奖杯
    @BindView(R.id.id_mImageJY_Class)
    public ImageView mJB_jiayi;//加一
    @BindView(R.id.class_jb_xing)
    public ImageView mJbX;//奖杯星星bg
    @BindView(R.id.jb_other_class)
    public ImageView mJB_other;//其他学生奖杯
    @BindView(R.id.jb_xing_other_class)
    public ImageView mJB_xing_other;//其他学生的星星背景
    @BindView(R.id.jb_jiayi_other_Class)
    public ImageView mJB_jiayi_other;//其他学生加一

    @BindView(R.id.answer_countdown_class)
    public ImageView answer_countdown;//答题倒计时

    @BindView(R.id.courseware_class)
    public FrameLayout courseware_class;

    public int AttendLessonID;//房间的ID
    public String ChapterFilePath;//H5的课件地址
    public String LessonTime;//开课时间
    public String finalRoomId;//房间号
    public String otherStudentName = "Bella";//其他学生姓名
    public int roomRole = ZegoConstants.RoomRole.Audience;//用户角色

    public boolean isClassBegin;//是否开始上课
    public int mJBNum;//奖杯的数量

    public String otherStreamID;//其他学生的流ID
    public String ownStreamID;//自己推流ID
    public String teacherStreamID;//老师的流ID

    public int mLeaveMessage = 0;//离开教室的提示信息

    public MediaPlayer player;//奖杯声音播放

    public String mCoursewareFile = "";


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Log.e("TAG", "handleMessage: " + msg.toString());
            switch (msg.what) {
                case Constant.HANDLE_INFO_CLASS_BEGIN://开课倒计时
                    msg.arg1 = msg.arg1 - 1;
                    if (msg.arg1 <= 0) {
                        classBegin();
                    } else if (!isClassBegin) {
                        mTime.setText(DateUtils.getClassBeginTime(msg.arg1));
                        sendHandleMessage(Constant.HANDLE_INFO_CLASS_BEGIN, 1000, msg.arg1);
                    }
                    break;
                case Constant.HANDLE_INFO_ANSWER:
                    startAnswer(msg.arg1);
                    break;
                case Constant.HANDLE_INFO_NEXTPAGE:
                    toPage(msg.arg1);
                    break;
                case Constant.HANDLE_INFO_MIKE:
                    openMikeTimer(msg.arg1);
                    break;
                case Constant.HANDLE_INFO_JB:
                    openJBAnim();
                    break;
            }
            return false;
        }
    });
    public String teacherName;
    public String ownName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //屏幕全屏和屏幕常亮和支持视频播放
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        super.onCreate(savedInstanceState);
        mPresenter.initSdk(finalRoomId, roomRole);//初始化SDK
    }

    //获取初始化数据
    public void getIntentData() {
        Intent mIntent = getIntent();
        AttendLessonID = mIntent.getIntExtra("AttendLessonID", 0);
        ChapterFilePath = mIntent.getStringExtra("ChapterFilePath");
        LessonTime = mIntent.getStringExtra("LessonTime");
        mCoursewareFile = mIntent.getStringExtra(Constant.INTENT_DATA_KEY_DOWNLOAD_FILE_PATH);
        teacherName = mIntent.getStringExtra(Constant.INTENT_DATA_KEY_TEACHER_NAME);
        ownName = AppUtils.getUserInfoData().getName();
        ownStreamID = String.valueOf(AppUtils.getUserInfoData().getAccountID());
        finalRoomId = "#AI-ClassRoom-" + String.valueOf(AttendLessonID);
        Log.e("TAG", "initData: " + mCoursewareFile);
        // /storage/emulated/0/Android/data/com.gogotalk/files/Download/F3A975D50DE74124B2FB07C5E4CB7348
    }

    @Override
    protected void initView() {
        super.initView();
        initWebView();
        //设置监听
        mvideo_swtich_own.setOnCheckedChangeListener(this);
        mvideo_switch_other.setOnCheckedChangeListener(this);
        mvoice_switch_other.setOnCheckedChangeListener(this);

        mMyName.setText(ownName);
        if (!TextUtils.isEmpty(teacherName)) {
            mTeacherName.setText(teacherName);
        } else {
            mTeacherName.setText("teacher");
        }

        String endDateTime;//开课时间
        if (LessonTime.indexOf("今天") == -1) {//没有
            endDateTime = LessonTime;
        } else {//有
            String str = LessonTime.substring(2);
            String date = DateUtils.StringData();
            endDateTime = date + str;
        }

        //是否需要开启课程开始倒计时
        int timeDiff = DateUtils.getTimeDiff(endDateTime);
        if (timeDiff > 0) {
            sendHandleMessage(Constant.HANDLE_INFO_CLASS_BEGIN, 1000, timeDiff);
        } else {
            classBegin();
        }
    }

    @OnClick(R.id.id_mGuanB_Class)
    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.id_mGuanB_Class:
                dialog();
                break;
        }
    }

    /**
     * 关闭房间弹窗
     */
    private void dialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.dialog_class_room_finsh, null);// 得到加载view
        LinearLayout layout = v.findViewById(R.id.id_mLayout_ZHIBO);// 加载布局
        TextView mMsg = v.findViewById(R.id.id_mTitleMsg_ZHIBO);
        Button mBtn1 = v.findViewById(R.id.id_mTC_ZHIBO);
        Button mBtn2 = v.findViewById(R.id.id_mQx_ZHIBO);
        if (mLeaveMessage == 0) {
            mMsg.setText("课程即将开始，最好不要离开哦！");
        }
        if (mLeaveMessage == 1) {
            mMsg.setText("课程还在进行中，最好不要离开哦！");
        }
        if (mLeaveMessage == 2) {
            mMsg.setText("课程已结束，确定要离开吗？");
        }
        final Dialog loadingDialog = new Dialog(this, R.style.MyDialogStyle);// 创建自定义样式dialog
        loadingDialog.setCancelable(true); // 是否可以按“返回键”消失
        loadingDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局

        mBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.dismiss();
                startActivity(new Intent(ClassRoomActivity.this, MainActivity.class));
                finish();
            }
        });
        mBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.dismiss();
            }
        });
        Window window = loadingDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.PopWindowAnimStyle);
        loadingDialog.show();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_class_room;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void joinRoomCompletion() {
        Logger.e("登录房间成功 roomId:", finalRoomId);
        // 预览自己的视频且推流
        mPresenter.startPreviewOwn(mOwnTV);
    }

    @Override
    public void teacherJoinRoom(String streamID, String userName) {
        if (ZGPlayHelper.sharedInstance().startPlaying(streamID, mTeacherTV)) {
            AppLogger.getInstance().i(ZGPublishHelper.class, "老师拉流失败, streamID : %s", streamID);
        }
        teacherStreamID = streamID;
        mImgs1.setVisibility(View.GONE);
        mTeacherTV.setVisibility(View.VISIBLE);
        mvideo_swtich_own.setChecked(true);
        mvideo_swtich_own.setClickable(true);
        mvideo_swtich_own.setOnCheckedChangeListener(this);
        classBegin();
    }

    @Override
    public void studentJoinRoom(String streamID, String userName) {
        if (ZGPlayHelper.sharedInstance().startPlaying(streamID, mOtherTV)) {
            AppLogger.getInstance().i(ZGPublishHelper.class, "其他学生拉流失败, streamID : %s", streamID);
        }
        otherStreamID = streamID;
        otherStudentName = userName;
        otherSNText.setText(userName);
        mImgs3.setVisibility(View.GONE);
        mOtherTV.setVisibility(View.VISIBLE);
        mvideo_switch_other.setChecked(true);
        mvideo_switch_other.setClickable(true);
        mvoice_switch_other.setChecked(true);
        mvoice_switch_other.setClickable(true);
        mvoice_switch_other.setOnCheckedChangeListener(this);
        mvideo_switch_other.setOnCheckedChangeListener(this);
    }

    @Override
    public void teacherLeaveRoom() {
        isClassBegin = false;
        mImgs1.setVisibility(View.VISIBLE);
        mImgs1.setImageResource(R.mipmap.bg_class_room_finish);
        mTeacherTV.setVisibility(View.GONE);
        mvideo_swtich_own.setClickable(false);
        mvideo_swtich_own.setChecked(false);
        mLeaveMessage = 2;
    }

    @Override
    public void studentLeaveRoom() {
        otherSNText.setText("");
        mImgs3.setImageResource(R.mipmap.bg_class_room_student_video_off);
        mImgs3.setVisibility(View.VISIBLE);
        mOtherTV.setVisibility(View.GONE);
        mvideo_switch_other.setClickable(false);
        mvideo_switch_other.setChecked(false);
        mvoice_switch_other.setChecked(false);
        mvoice_switch_other.setClickable(false);
    }

    @Override
    public void sendHandleMessage(int... ags) {
        if (handler != null) {
            Message msg = new Message();
            msg.what = ags[0];
            if (ags.length >= 3) {
                msg.arg1 = ags[2];
            }
            if (ags.length == 4) {
                msg.arg2 = ags[3];
            }
            if (ags.length > 1 && ags[1] > 0) {
                handler.sendMessageDelayed(msg, ags[1]);
            } else {
                handler.sendMessage(msg);
            }
        }
    }

    //开启奖杯
    private void openJBAnim() {
        mMkfPhoto.setVisibility(View.GONE);
        mJBNum++;
        AnimatorUtils.showOwnJiangbei(mJbX, mJB, mJB_jiayi, mMyJB, new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mJbX.setVisibility(View.VISIBLE);
                mJB.setVisibility(View.VISIBLE);
                mJB_jiayi.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mMyJB.setText(mJBNum + "");
                mJbX.setVisibility(View.INVISIBLE);
                mJB.setVisibility(View.INVISIBLE);
                mJB_jiayi.setVisibility(View.INVISIBLE);
                mJB.setTranslationX(0);
                mJB.setTranslationY(0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mMyJB.setText(mJBNum + "");
                mJbX.setVisibility(View.INVISIBLE);
                mJB.setVisibility(View.INVISIBLE);
                mJB_jiayi.setVisibility(View.INVISIBLE);
                mJB.setTranslationX(0);
                mJB.setTranslationY(0);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        //开始奖杯动画
        AnimatorUtils.showOtherJiangbei(mJB_xing_other, mJB_other, mJB_jiayi_other, mOtherJBNum, new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mJB_xing_other.setVisibility(View.VISIBLE);
                mJB_other.setVisibility(View.VISIBLE);
                mJB_jiayi_other.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mOtherJBNum.setText(mJBNum + "");
                mJB_xing_other.setVisibility(View.INVISIBLE);
                mJB_other.setVisibility(View.INVISIBLE);
                mJB_jiayi_other.setVisibility(View.INVISIBLE);
                mJB_other.setTranslationX(0);
                mJB_other.setTranslationY(0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mOtherJBNum.setText(mJBNum + "");
                mJB_xing_other.setVisibility(View.INVISIBLE);
                mJB_other.setVisibility(View.INVISIBLE);
                mJB_jiayi_other.setVisibility(View.INVISIBLE);
                mJB_other.setTranslationX(0);
                mJB_other.setTranslationY(0);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        player = MediaPlayer.create(this, R.raw.trophy);
        if (player.isPlaying()) {
            return;
        }
        player.start();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                player.pause();
                player.seekTo(0);
                player.release();
                Log.e("TAG", "停止播放");
            }
        });
    }

    //开启麦克风倒计时
    private void openMikeTimer(int time) {
        mMkfPhoto.setVisibility(View.VISIBLE);
        if (time == 6) {
            mMkfPhoto.setBackground(getResources().getDrawable(R.drawable.list_class_room_mike_six));
            ((AnimationDrawable) mMkfPhoto.getBackground()).start();
        } else {
            mMkfPhoto.setBackground(getResources().getDrawable(R.drawable.list_class_room_mike_twelve));
            ((AnimationDrawable) mMkfPhoto.getBackground()).start();
        }
        sendHandleMessage(Constant.HANDLE_INFO_JB, time * 1000);
    }

    //跳转页数
    private void toPage(int page) {
        webView.evaluateJavascript("javascript:ToPage(" + page + ")", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                Log.i("TAG", value);
            }
        });
    }

    //答题处理
    private void startAnswer(int status) {
        if (status > 0) {
            webView.evaluateJavascript("javascript:exec()", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    Log.i("TAG", value);
                }
            });
            answer_countdown.setVisibility(View.VISIBLE);
            ((AnimationDrawable) answer_countdown.getBackground()).start();
            sendHandleMessage(Constant.HANDLE_INFO_ANSWER, 10000, 0);
        } else {
            //答题结束
            ((AnimationDrawable) answer_countdown.getBackground()).stop();
            ((AnimationDrawable) answer_countdown.getBackground()).selectDrawable(0);
            answer_countdown.setVisibility(View.INVISIBLE);
        }
    }

    //教室开始上课
    private void classBegin() {
        if (!isClassBegin) {
            mLeaveMessage = 1;
            isClassBegin = true;
            if (!TextUtils.isEmpty(mCoursewareFile) && new File(mCoursewareFile + File.separator + "preview.html").exists()) {
                webView.loadUrl("file://" + mCoursewareFile + File.separator + "preview.html");
                Log.e("TAG", "classBegin: 加载本地课件");
            } else {
                webView.loadUrl(ChapterFilePath);
                Log.e("TAG", "classBegin: 加载网络课件");
            }
            mLayouts.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            Log.e("TAG", "classBegin: " + ChapterFilePath);
        }
    }

    /**
     * 加载webview
     */
    private void initWebView() {
        //加载assets目录下的html
        //加上下面这段代码可以使网页中的链接不以浏览器的方式打开
        webView.setWebViewClient(new WebViewClient());
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);//滚动条风格，为0指滚动条不占用空间，直接覆盖在网页上
        //得到webview设置
        WebSettings webSettings = webView.getSettings();
        //允许使用javascript
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setSupportZoom(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setDomStorageEnabled(true);// 必须保留，否则无法播放优酷视频，其他的OK

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(webSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        if (Build.VERSION.SDK_INT >= 19) {//设置是否自动加载图片
            webSettings.setLoadsImagesAutomatically(true);
        } else {
            webSettings.setLoadsImagesAutomatically(false);
        }

        try {
            if (Build.VERSION.SDK_INT >= 16) {
                Class<?> clazz = webSettings.getClass();
                Method method = clazz.getMethod("setAllowUniversalAccessFromFileURLs", boolean.class);
                if (method != null) {
                    method.invoke(webSettings, true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        webView.setWebViewClient(new WebViewClient() {

            /**
             * 当前网页的链接仍在webView中跳转
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            /**
             * 处理ssl请求
             */
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            /**
             * 页面载入完成回调
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.loadUrl("javascript:try{autoplay();}catch(e){}");
                sendName(ownName, otherStudentName);
            }

        });


        webView.setWebChromeClient(new WebChromeClient() {
            /**
             * 显示自定义视图，无此方法视频不能播放
             */
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
            }
        });
    }

    //发送学员姓名
    private void sendName(final String name1, final String name2) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.evaluateJavascript("javascript:GetName('" + name1 + "','" + name2 + "')", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Log.e("TAG", "设置名字结果" + value);
                    }
                });
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == mvideo_swtich_own) {
            if (isChecked) {
                ZGPublishHelper.sharedInstance().startPublishing(ownStreamID, "", ZegoConstants.PublishFlag.JoinPublish);
                Toast.makeText(this, getResources().getString(R.string.open_video), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getResources().getString(R.string.close_video), Toast.LENGTH_SHORT).show();
                ZGPublishHelper.sharedInstance().stopPublishing();
            }
        }
        if (buttonView == mvideo_switch_other) {
            if (isChecked) {
                mImgs3.setVisibility(View.GONE);
                mOtherTV.setVisibility(View.VISIBLE);
                mvoice_switch_other.setChecked(true);
                mvoice_switch_other.setClickable(true);
                boolean isPlaySuccess = ZGPlayHelper.sharedInstance().startPlaying(otherStreamID, mOtherTV);
                if (!isPlaySuccess) {
                    AppLogger.getInstance().i(ZGPublishHelper.class, "拉流失败, streamID : %s", otherStreamID);
                }
            } else {
                mvoice_switch_other.setChecked(false);
                mvoice_switch_other.setClickable(false);
                mImgs3.setVisibility(View.VISIBLE);
                mOtherTV.setVisibility(View.GONE);
                mImgs3.setImageResource(R.mipmap.bg_class_room_student_close_video);
                ZGPlayHelper.sharedInstance().stopPlaying(otherStreamID);
            }
        }
        if (buttonView == mvoice_switch_other) {
            if (isChecked) {
                ZGPlayHelper.sharedInstance().enableAudioPlayStream(otherStreamID, true);
            } else {
                ZGPlayHelper.sharedInstance().enableAudioPlayStream(otherStreamID, false);
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        /**
         * 系统返回键控制
         * 关闭推/拉流
         * 关闭房间
         * 释放SDK
         */
        ZGPublishHelper.sharedInstance().stopPreviewView();
        ZGPublishHelper.sharedInstance().stopPublishing();
        ZGBaseHelper.sharedInstance().loginOutRoom();
        ZGBaseHelper.sharedInstance().unInitZegoSDK();
        ZGMediaSideInfoDemo.sharedInstance().unSetMediaSideInfoCallback();
        courseware_class.removeView(webView);
        webView.destroy();
        if (player != null) {
            player.release();
            player = null;
        }
        if (handler != null) {
            handler = null;
        }
        AnimatorUtils.destory();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
