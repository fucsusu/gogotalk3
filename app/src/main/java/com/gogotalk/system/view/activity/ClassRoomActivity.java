package com.gogotalk.system.view.activity;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
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
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;

import com.chivox.AIEngineUtils;
import com.gogotalk.system.R;
import com.gogotalk.system.model.util.Constant;
import com.gogotalk.system.presenter.ClassRoomContract;
import com.gogotalk.system.presenter.ClassRoomPresenter;
import com.gogotalk.system.util.AnimatorUtils;
import com.gogotalk.system.util.AppUtils;
import com.gogotalk.system.util.DateUtils;
import com.gogotalk.system.util.ToastUtils;
import com.gogotalk.system.view.widget.AnswerCountDown;
import com.gogotalk.system.view.widget.MikeRateView;
import com.gogotalk.system.view.widget.MyVoiceValue;
import com.gogotalk.system.zego.AppLogger;
import com.gogotalk.system.zego.ZGBaseHelper;
import com.gogotalk.system.zego.ZGMediaSideInfoDemo;
import com.gogotalk.system.zego.ZGPlayHelper;
import com.gogotalk.system.zego.ZGPublishHelper;
import com.orhanobut.logger.Logger;
import com.zego.zegoavkit2.soundlevel.ZegoSoundLevelMonitor;
import com.zego.zegoliveroom.constants.ZegoConstants;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

public class ClassRoomActivity extends BaseActivity<ClassRoomPresenter> implements ClassRoomContract.IClassRoomView, CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.class_room_web)
    public WebView webView;
    @BindView(R.id.class_room_teacher_video)
    public TextureView mTeacherTV;
    @BindView(R.id.class_room_own_video)
    public TextureView mOwnTV;
    @BindView(R.id.class_room_other_video)
    public TextureView mOtherTV;
    @BindView(R.id.class_room_timer_tv)
    public TextView mTime;
    @BindView(R.id.class_room_teacher_name_tv)
    public TextView mTeacherName;
    @BindView(R.id.class_room_timer_group)
    public Group timer_group;
    //自己视频开关
    @BindView(R.id.class_room_own_video_cb)
    public CheckBox mvideo_swtich_own;
    //其他学生视频开关
    @BindView(R.id.class_room_other_video_cb)
    public CheckBox mvideo_switch_other;
    //其他学生声音开关
    @BindView(R.id.class_room_other_voice_cb)
    public CheckBox mvoice_switch_other;
    @BindView(R.id.class_room_teacher_video_bg)
    public ImageView mTeacherVideoBg;
    @BindView(R.id.class_room_other_video_bg)
    public ImageView mOtherStudentVideoBg;
    @BindView(R.id.class_room_own_name_tv)
    public TextView mMyName;
    @BindView(R.id.class_room_own_jb_num)
    public TextView mMyJB;
    @BindView(R.id.class_room_other_jb_num)
    public TextView mOtherJBNum;
    @BindView(R.id.class_room_other_name_tv)
    public TextView otherSNText;

    @BindView(R.id.class_room_mike)
    public ImageView mMkfPhoto;
    @BindView(R.id.class_room_mike_progress)
    public MikeRateView mikeRateView;
    @BindView(R.id.class_room_mike_voice)
    public MyVoiceValue myVoiceValue;

    @BindView(R.id.class_room_jb_own)
    public ImageView mOwnJB;//奖杯
    @BindView(R.id.class_room_jb_own_jiayi)
    public ImageView mJB_jiayi;//加一
    @BindView(R.id.class_room_jb_own_jiaer)
    public ImageView mJB_jiaer;//加二
    @BindView(R.id.class_room_jb_own_jiasan)
    public ImageView mJb_jiasan;//加三
    @BindView(R.id.class_room_jb_own_xing)
    public ImageView mJbX;//奖杯星星bg
    @BindView(R.id.class_room_jb_other)
    public ImageView mJB_other;//其他学生奖杯
    @BindView(R.id.class_room_jb_other_xing)
    public ImageView mJB_xing_other;//其他学生的星星背景
    @BindView(R.id.class_room_jb_other_jiayi)
    public ImageView mJB_jiayi_other;//其他学生加一
    @BindView(R.id.class_room_jb_other_jiaer)
    public ImageView mJB_jiaer_other;//其他学生加二
    @BindView(R.id.class_room_jb_other_jiasan)
    public ImageView mJB_jiaSan_other;//其他学生加三

    @BindView(R.id.class_room_answer_countdown)
    public AnswerCountDown answer_countdown;//答题倒计时

    @BindView(R.id.class_room_root)
    public ConstraintLayout courseware_class;

    @BindView(R.id.class_room_loud)
    public ImageView loud_class;

    public String AttendLessonID;//房间的ID
    public String LessonTime;//开课时间
    public String finalRoomId;//房间号
    public String otherStudentName = "Bella";//其他学生姓名
    public int roomRole = ZegoConstants.RoomRole.Audience;//用户角色

    public boolean isClassBegin = false;//是否开始上课
    public int mOwnJBNum;//自己奖杯的数量
    private int mOtherJbNum;//其他学生的奖杯数量

    public String otherStreamID;//其他学生的流ID
    public String ownStreamID;//自己推流ID
    public String teacherStreamID;//老师的流ID

    public int mLeaveMessage = 0;//离开教室的提示信息

    public MediaPlayer player;//奖杯声音播放

    public String mCoursewareFile = "";

    private boolean isWebFinsh = false;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
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
                    startAnswer();
                    break;
                case Constant.HANDLE_INFO_NEXTPAGE:
                    toPage(msg.arg1);
                    break;
                case Constant.HANDLE_INFO_MIKE:
                    openMikeTimer(msg.arg1);
                    break;
                case Constant.HANDLE_INFO_JB:
                    showJb(msg.arg1);
                    break;
                case Constant.HANDLE_INFO_VOICE:
                    //myVoiceValue.setVoiceNum(msg.arg1);
                    break;
            }
            return false;
        }
    });
    public String teacherName;
    public String ownName;
    public WebSettings webSettings;
    public int jumpPage = -1;
    public int pptPage;
    public boolean isStudentJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //屏幕全屏和屏幕常亮和支持视频播放
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        super.onCreate(savedInstanceState);
        AIEngineUtils.getInstance().initSDK();
        mPresenter.initSdk(finalRoomId, roomRole);//初始化SDK
    }

    @Override
    protected void onResume() {
        super.onResume();
        webSettings.setJavaScriptEnabled(true);
        webView.onResume();
    }

    //获取初始化数据
    public void getIntentData() {
        Intent mIntent = getIntent();
        AttendLessonID = mIntent.getStringExtra(Constant.INTENT_DATA_KEY_CLASS_ID);
        LessonTime = mIntent.getStringExtra(Constant.INTENT_DATA_KEY_BEGIN_TIME);
        mCoursewareFile = mIntent.getStringExtra(Constant.INTENT_DATA_KEY_DOWNLOAD_FILE_PATH);
        teacherName = mIntent.getStringExtra(Constant.INTENT_DATA_KEY_TEACHER_NAME);
        jumpPage = mIntent.getIntExtra(Constant.INTENT_DATA_KEY_TOPAGE, -1);
        ownName = AppUtils.getUserInfoData().getName();
        ownStreamID = String.valueOf(AppUtils.getUserInfoData().getAccountID());
        finalRoomId = "#AI-ClassRoom-" + AttendLessonID;
        Log.e("TAG", "initData: " + finalRoomId);
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
        endDateTime = LessonTime;
        //是否需要开启课程开始倒计时
        int timeDiff = DateUtils.getTimeDiff(endDateTime);
        if (timeDiff > 0) {
            sendHandleMessage(Constant.HANDLE_INFO_CLASS_BEGIN, 1000, timeDiff);
        } else {
            classBegin();
        }
    }

    @OnClick(R.id.class_room_close)
    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.class_room_close:
//                dialog();
                openMikeTimer(5);
                mPresenter.getAIEngineResult("sent", "Let's go to the zoo!");
//                String[] types = new String[]{"word", "sent"};
//                String[] words = new String[]{"zoo", "tiger", "monkey", "parrot", "crocodile", "snake"};
//                String[] sents = new String[]{"Let's go to the zoo!", "It's a tiger", "It's a monkey", "It's a parrot", "It's a crocodile", "It's a snake"};
//                int typeMax = types.length;
//                int max = words.length, min = 0;
//                int typeRan = (int) (Math.random() * (typeMax - min) + min);
//                int wordRan = (int) (Math.random() * (max - min) + min);
//                int sentRan = (int) (Math.random() * (max - min) + min);
//                String currentType = types[typeRan];
//                String currentContent = "";
//                if ("word".equals(currentType)) {
//                    currentContent = words[wordRan];
//                } else {
//                    currentContent = sents[sentRan];
//                }
//                Log.d("wuhongjie", "=======" + currentType + "=========" + currentContent + "==============");
                //  openMikeTimer(6, currentType, currentContent);

                // mPresenter.sendRoomCommand("answer", "123456", true);
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
        mTeacherTV.setVisibility(View.VISIBLE);
        classBegin();
    }

    @Override
    public void studentJoinRoom(String streamID, String userName) {
        isStudentJoin = true;
        otherStreamID = streamID;
        otherStudentName = userName;
        otherSNText.setText(userName);
        mvideo_switch_other.setClickable(true);
        mvoice_switch_other.setClickable(true);
        mvoice_switch_other.setOnCheckedChangeListener(this);
        mvideo_switch_other.setOnCheckedChangeListener(this);
        if (mvideo_switch_other.isChecked() && !ZGPlayHelper.sharedInstance().startPlaying(streamID, mOtherTV)) {
            AppLogger.getInstance().i(ZGPublishHelper.class, "其他学生拉流失败, streamID : %s", streamID);
        }
    }

    @Override
    public void teacherLeaveRoom() {
        isClassBegin = false;
        mTeacherVideoBg.setImageResource(R.mipmap.bg_class_room_finish);
        mTeacherTV.setVisibility(View.INVISIBLE);
        mLeaveMessage = 2;
    }

    @Override
    public void studentLeaveRoom() {
        isStudentJoin = false;
        otherSNText.setText("");
        mOtherStudentVideoBg.setImageResource(R.mipmap.bg_class_room_student_video_off);
        mOtherStudentVideoBg.setVisibility(View.VISIBLE);
        mOtherTV.setVisibility(View.INVISIBLE);
        mvideo_switch_other.setClickable(false);
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

    @SuppressLint("JavascriptInterface")
    @JavascriptInterface
    public void showJb(int jbNum) {
        Log.e("TAG", "showJb: " + jbNum);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                openOwnJBAnim(jbNum);
            }
        });
    }

    @SuppressLint("JavascriptInterface")
    @JavascriptInterface
    public void answerResult(boolean result) {
        Log.e("TAG", "answerResult: " + result);
        showJb(1);
    }

    //开启奖杯
    private void openOwnJBAnim(int addNum) {
        isHideMicor(true);
        mOwnJBNum = mOwnJBNum + addNum;
        //给自己发奖杯
        switch (addNum) {
            case 1:
                AnimatorUtils.showOwnJiangbei(mJbX, mOwnJB, mJB_jiayi, mMyJB, mOwnJBNum);
                break;
            case 2:
                AnimatorUtils.showOwnJiangbei(mJbX, mOwnJB, mJB_jiaer, mMyJB, mOwnJBNum);
                break;
            case 3:
                AnimatorUtils.showOwnJiangbei(mJbX, mOwnJB, mJb_jiasan, mMyJB, mOwnJBNum);
                break;
        }
        mPresenter.sendShowJbRoomCommand(addNum);
        if (!isStudentJoin) {
            openOtherJBAnim(1);
        }
        //奖杯声音播放
        if (player == null) {
            player = MediaPlayer.create(this, R.raw.trophy);
        }
        if (!player.isPlaying()) {
            player.start();
        }
    }

    //开启奖杯
    public void openOtherJBAnim(int addNum) {
        if (addNum < 0) return;
        isHideMicor(true);
        mOtherJbNum = mOtherJbNum + addNum;
        //给自己发奖杯
        switch (addNum) {
            case 1:
                AnimatorUtils.showOtherJiangbei(mJB_xing_other, mJB_other, mJB_jiayi_other, mOtherJBNum, mOtherJbNum);
                break;
            case 2:
                AnimatorUtils.showOtherJiangbei(mJB_xing_other, mJB_other, mJB_jiayi_other, mOtherJBNum, mOtherJbNum);
                break;
            case 3:
                AnimatorUtils.showOtherJiangbei(mJB_xing_other, mJB_other, mJB_jiayi_other, mOtherJBNum, mOtherJbNum);
                break;
        }
        //奖杯声音播放
        if (player == null) {
            player = MediaPlayer.create(this, R.raw.trophy);
        }
        if (!player.isPlaying()) {
            player.start();
        }
    }


    /**
     * 是否显示麦克风view
     *
     * @param flag
     */
    private void isHideMicor(boolean flag) {
        if (flag) {
            mMkfPhoto.setVisibility(View.INVISIBLE);
            mikeRateView.setVisibility(View.INVISIBLE);
            loud_class.setVisibility(View.INVISIBLE);
            myVoiceValue.setVisibility(View.INVISIBLE);
            ZegoSoundLevelMonitor.getInstance().stop();
        } else {
            mMkfPhoto.setVisibility(View.VISIBLE);
            mikeRateView.setVisibility(View.VISIBLE);
            loud_class.setVisibility(View.VISIBLE);
            myVoiceValue.setVisibility(View.VISIBLE);
            ZegoSoundLevelMonitor.getInstance().start();
        }
    }

    //开启麦克风倒计时
    private void openMikeTimer(int time) {
        if (time > 0) {
            //打开麦克风倒计时
            isHideMicor(false);
            mikeRateView.start(time);
            sendHandleMessage(Constant.HANDLE_INFO_MIKE, time * 1000, -1);
        } else {
            //关闭麦克风倒计时
            isHideMicor(true);
            Log.e("TAG", "openMikeTimer: " + AIEngineUtils.getInstance().isStart());
            if (AIEngineUtils.getInstance().isStart()) {
                AIEngineUtils.getInstance().stopRecord();
            } else {
                sendHandleMessage(Constant.HANDLE_INFO_JB, 0, 1);
            }
        }
    }

    //跳转页数
    private void toPage(int page) {
        if (page >= 0 && page > pptPage) {
            pptPage = page;
            webView.clearCache(true);
            webView.evaluateJavascript("javascript:ToPage(" + page + ")", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    Log.i("TAG", value);
                }
            });
        }
    }

    //答题处理
    private void startAnswer() {
        webView.evaluateJavascript("javascript:exec()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                Log.i("TAG", value);
            }
        });
        answer_countdown.startCountDown();
    }

    //教室开始上课
    private void classBegin() {
        if (!isClassBegin) {
            mLeaveMessage = 1;
            isClassBegin = true;
            if (!TextUtils.isEmpty(mCoursewareFile) && new File(mCoursewareFile + File.separator + "preview.html").exists()) {
                webView.loadUrl("file://" + mCoursewareFile + File.separator + "preview.html");
                //webView.loadUrl("file:///android_asset/index2.html");
                Log.e("TAG", "classBegin: 加载本地课件");
            } else {
                ToastUtils.showLongToast(this, "课件下载失败！");
                finish();
                return;
            }
            timer_group.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        webSettings.setJavaScriptEnabled(false);
    }

    /**
     * 加载webview
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        //加载assets目录下的html
        //加上下面这段代码可以使网页中的链接不以浏览器的方式打开
        webView.setWebViewClient(new WebViewClient());

        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);//滚动条风格，为0指滚动条不占用空间，直接覆盖在网页上
        //得到webview设置
        webSettings = webView.getSettings();
        //允许使用javascript
        webSettings.setJavaScriptEnabled(true);
        //设置加载网页时暂不加载图片
        webSettings.setBlockNetworkImage(false);
        //设置webview推荐使用的窗口，使html界面自适应屏幕
        webSettings.setUseWideViewPort(true);
        //缩放至屏幕的大小
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        //设置可以访问文件加载本地html
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setSupportZoom(true);
        //设置图片加载
        webSettings.setLoadsImagesAutomatically(true);
        //设置是否需要手势去播放视频
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setDomStorageEnabled(true);// 必须保留，否则无法播放优酷视频，其他的OK
        //设置不缓存
        webSettings.setAppCacheEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //设置渲染优先级
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);

        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        webView.addJavascriptInterface(this, "androidApi");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
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
             * 页面载入完成回调
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.loadUrl("javascript:try{autoplay();}catch(e){}");
                if (!isWebFinsh) {
                    isWebFinsh = true;
                    sendName(ownName, otherStudentName);
                    toPage(jumpPage);
                }
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
                ToastUtils.showLongToast(this, getResources().getString(R.string.open_video));
            } else {
                ToastUtils.showLongToast(this, getResources().getString(R.string.close_video));
                ZGPublishHelper.sharedInstance().stopPublishing();
            }
        }
        if (buttonView == mvideo_switch_other) {
            if (isChecked) {
                mOtherStudentVideoBg.setVisibility(View.INVISIBLE);
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
                mOtherStudentVideoBg.setVisibility(View.VISIBLE);
                mOtherTV.setVisibility(View.INVISIBLE);
                mOtherStudentVideoBg.setImageResource(R.mipmap.bg_class_room_student_close_video);
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
        ZegoSoundLevelMonitor.getInstance().stop();
        ZGPublishHelper.sharedInstance().stopPreviewView();
        ZGPublishHelper.sharedInstance().stopPublishing();
        ZGBaseHelper.sharedInstance().loginOutRoom();
        ZGBaseHelper.sharedInstance().unInitZegoSDK();
        ZGMediaSideInfoDemo.sharedInstance().unSetMediaSideInfoCallback();
        AIEngineUtils.getInstance().onDestroy();
        courseware_class.removeView(webView);
        webView.destroy();
        if (player != null) {
            player.release();
            player = null;
        }
        if (handler != null) {
            handler = null;
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPresenter.startPreviewOwn(mOwnTV);
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
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
