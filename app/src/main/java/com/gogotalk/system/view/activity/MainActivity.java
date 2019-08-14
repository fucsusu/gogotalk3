package com.gogotalk.system.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gogotalk.system.BuildConfig;
import com.gogotalk.system.R;
import com.gogotalk.system.model.entity.CoursesBean;
import com.gogotalk.system.model.entity.RoomInfoBean;
import com.gogotalk.system.model.util.Constant;
import com.gogotalk.system.presenter.MainContract;
import com.gogotalk.system.presenter.MainPresenter;
import com.gogotalk.system.util.AppUtils;
import com.gogotalk.system.util.AutoUpdateUtil;
import com.gogotalk.system.util.CoursewareDownLoadUtil;
import com.gogotalk.system.util.DataCleanManager;
import com.gogotalk.system.util.DelectFileUtil;
import com.gogotalk.system.util.PermissionsUtil;
import com.gogotalk.system.util.SPUtils;
import com.gogotalk.system.util.ScreenUtils;
import com.gogotalk.system.util.ToastUtils;
import com.gogotalk.system.view.adapter.MainRecyclerAdapter;
import com.gogotalk.system.view.widget.AboutDialog;
import com.gogotalk.system.view.widget.CheckDeviceDialog;
import com.gogotalk.system.view.widget.CommonDialog;
import com.gogotalk.system.view.widget.SpaceItemDecoration;
import com.gogotalk.system.view.widget.UserInfoDialogV2;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * 课程界面主界面
 */
public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {
    @BindView(R.id.id_mRecyclerView)
    RecyclerView mRecyclerView;//课单列表
    @BindView(R.id.mian_root_view)
    ConstraintLayout root_view;
    @BindView(R.id.id_mPersonalSettings)
    ImageView mImg;
    @BindView(R.id.id_mUserName_Home)
    TextView mUsreName;
    @BindView(R.id.id_mKeshi_HomePage)
    TextView mKeshi;
    @BindView(R.id.tv_youxiaoqi)
    TextView tvYouXiaoQi;
    @BindView(R.id.id_mQYK_HomePage)
    RelativeLayout relativeLayout;
    @BindView(R.id.id_mRecord)
    Button idMRecord;
    @BindView(R.id.btn_settingg)
    RadioButton btnSettingg;
    @BindView(R.id.id_mBJ_Homepage)
    ImageView idMBJHomepage;
    @BindView(R.id.id_mStr_HomePage)
    TextView idMStrHomePage;
    @BindView(R.id.id_mBtn_HomePage)
    Button idMBtnHomePage;
    @BindView(R.id.id_GoGoTalk_Home)
    LinearLayout idGoGoTalkHome;
    @BindView(R.id.id_refresh)
    Button btnRefresh;
    private List<CoursesBean> list = new ArrayList<>();
    private long exitTime = 0;
    MainRecyclerAdapter recyclerAdapter;
    private PopupWindow popupWindow;
    TextView btn_check_device, btn_clear_cache, btn_about_us, btn_out_login;
    RadioButton btn_setting;
    UserInfoDialogV2.Builder userInfoDialogBuilder;
    UserInfoDialogV2 userInfoDialog;
    boolean isFirstLoadData;
    Disposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermissionsUtil.getInstance().requestPermissions(this);
        initEvent();
        isFirstLoadData = false;
        intervalUpdateData();
        AutoUpdateUtil.getInstance().checkForUpdates(this);
        delectCoursewareFile();
    }

    /**
     * 复用activity实例回调刷新数据并重新启动轮训
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            switch (intent.getIntExtra(Constant.INTENT_DATA_KEY_DIRECTION, 0)) {
                case Constant.DIRECTION_SELECTNAME_TO_HOME:
                    userInfoDialogBuilder.setName(intent.getStringExtra(Constant.INTENT_DATA_KEY_NAME));
                    return;
            }
        }
        isFirstLoadData = false;
        intervalUpdateData();
    }

    /**
     * 轮训请求操作
     */
    private void intervalUpdateData() {
        Observable.interval(0, 3 * 60, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.e("wuhongjie", "我轮训了=========");
                        if (!isFirstLoadData) {
                            isFirstLoadData = true;
                            mPresenter.getUserInfoData(true, false);
                            mPresenter.getClassListData(false, true);
                        } else {
                            mPresenter.getUserInfoData(false, false);
                            mPresenter.getClassListData(false, false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void cancelIntervalUpdateData() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable = null;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_v2;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initView() {
        super.initView();
        userInfoDialogBuilder = new UserInfoDialogV2.Builder(this);
        userInfoDialog = userInfoDialogBuilder.create();
        final View inflate = LayoutInflater.from(this).inflate(R.layout.popup_shezhi, null, false);
        popupWindow = new PopupWindow(inflate, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.mipmap.bg_main_popu_setting));
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);
        btn_check_device = inflate.findViewById(R.id.btn_check_device);
        btn_setting = findViewById(R.id.btn_settingg);
        btn_clear_cache = inflate.findViewById(R.id.btn_clear_cache);
        btn_about_us = inflate.findViewById(R.id.btn_about_us);
        btn_out_login = inflate.findViewById(R.id.btn_out_login);
        View contentView = popupWindow.getContentView();
        contentView.measure(makeDropDownMeasureSpec(popupWindow.getWidth()),
                makeDropDownMeasureSpec(popupWindow.getHeight()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new MainRecyclerAdapter(this, list);
        recyclerAdapter.setBtnClickLisener(new MainRecyclerAdapter.IBtnClickLisener() {
            @Override
            public void onBtnClassPreviewClick(String path) {
                Intent mIntent = new Intent(MainActivity.this, VideoActivity.class);
                mIntent.putExtra(Constant.INTENT_DATA_KEY_VIDEO_URL, path);
                startActivity(mIntent);
            }

            @Override
            public void onBtnGoClassRoomClick(boolean flag, CoursesBean coursesBean) {
                if (!BuildConfig.DEBUG&&!flag) {
                    ToastUtils.showLongToast(MainActivity.this, "课前10分钟才可以进入教室");
                    return;
                }
                if (!PermissionsUtil.getInstance().isPermissions()) {
                    ToastUtils.showLongToast(MainActivity.this, "部分功能未授权，请授权后再试！");
                }
                String path = "http://coursefiles.oss-cn-beijing.aliyuncs.com/Hgogotalk/CourseZip/L1/L1Lesson21.zip";
                CoursewareDownLoadUtil.getCoursewareUtil().downloadCourseware(MainActivity.this,  coursesBean.getZipDownLoadUrl(),
                        root_view, coursesBean.getZipEncrypInfo(), new CoursewareDownLoadUtil.CoursewareDownFinsh() {
                            @Override
                            public void finsh(String filePath) {
                                if (TextUtils.isEmpty(filePath)) {
                                    ToastUtils.showLongToast(MainActivity.this, "课件下载失败请查看网络是否连接正常！");
                                    return;
                                }
                                mPresenter.getRoomInfo(coursesBean, filePath);
                            }
                        });
            }

            @Override
            public void onBtnCancelOrderClass(int demandId) {
                String str = "是否取消约课？";
                int s = 3;
                Map<String, String> data = new HashMap<>();
                data.put("demandId", String.valueOf(demandId));
                dialogCommon(str, s, data);
            }
        });
        mRecyclerView.setAdapter(recyclerAdapter);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dip2px(getApplicationContext(), 10), 0));
    }

    @Override
    public void onRoomInfoSuccess(RoomInfoBean bean, String filePath) {
        Log.e("TAG", "onRoomInfoSucces: " + bean.toString());
        Intent mIntent = new Intent(MainActivity.this, ClassRoomActivity.class);
        mIntent.putExtra(Constant.INTENT_DATA_KEY_CLASS_ID, bean.getAttendLessonID() + "");
        mIntent.putExtra(Constant.INTENT_DATA_KEY_BEGIN_TIME, bean.getLessonTime());
        mIntent.putExtra(Constant.INTENT_DATA_KEY_TEACHER_NAME, bean.getTeacherName());
        mIntent.putExtra(Constant.INTENT_DATA_KEY_DOWNLOAD_FILE_PATH, filePath);
        mIntent.putExtra(Constant.INTENT_DATA_KEY_MY, bean.getMyStudentSoundUrl());
        mIntent.putExtra(Constant.INTENT_DATA_KEY_OTHER, bean.getOtherStudentSoundUrl());
        mIntent.putExtra(Constant.INTENT_DATA_KEY_OTHER_NAME, bean.getOtherStudnetName());
        mIntent.putExtra(Constant.INTENT_DATA_KEY_OTHER_NAME_ID, bean.getOtherStudentId());
        startActivity(mIntent);
    }

    @Override
    public void updateRecelyerViewData(List<CoursesBean> data) {
        list.clear();
        list.addAll(data);
        recyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void setUserInfoDataToView(String imageUrl, String name, String classTime, String expiry) {
        mUsreName.setText(name);
        mKeshi.setText(classTime);
        tvYouXiaoQi.setText(expiry);
        Glide.with(this).load(imageUrl).placeholder(R.mipmap.ic_main_user_info_header_default)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE).into(mImg);
    }

    @Override
    public void onCanelOrderClassSuccess() {
        mPresenter.getClassListData(true, true);
    }

    @Override
    public void onUpdateUserInfoSuceess() {
        userInfoDialog.dismiss();
        mPresenter.getUserInfoData(false, true);
    }


    @Override
    public void showRecelyerViewOrEmptyViewByFlag(boolean flag) {
        mRecyclerView.setVisibility(flag ? View.VISIBLE : View.GONE);
        relativeLayout.setVisibility(!flag ? View.VISIBLE : View.GONE);
    }

    /**
     * 计算popupwindow的弹窗
     *
     * @param measureSpec
     * @return
     */
    private static int makeDropDownMeasureSpec(int measureSpec) {
        int mode;
        if (measureSpec == ViewGroup.LayoutParams.WRAP_CONTENT) {
            mode = View.MeasureSpec.UNSPECIFIED;
        } else {
            mode = View.MeasureSpec.EXACTLY;
        }
        return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measureSpec), mode);
    }

    private void initEvent() {
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                btn_setting.setChecked(false);
            }
        });
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.setFocusable(false);
                popupWindow.update();
                if ("MI 8".equals(Build.MODEL)) {
                    popupWindow.showAsDropDown(view, -(popupWindow.getContentView().getMeasuredWidth() - view.getWidth() - 100), 10);
                } else {
                    popupWindow.showAsDropDown(view, -(popupWindow.getContentView().getMeasuredWidth() - view.getWidth()), 10);
                }
                AppUtils.fullScreenImmersive(popupWindow.getContentView());
                popupWindow.setFocusable(true);
                popupWindow.update();
            }
        });
        btn_check_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermissionsUtil.getInstance().isPermissions()) {
                    popupWindow.dismiss();
                    showCheckDeviceDialog();
                } else {
                    PermissionsUtil.getInstance().requestPermissions(MainActivity.this);
                    ToastUtils.showLongToast(MainActivity.this, "部分功能未授权，请授权后再试！");
                }
            }
        });
        btn_clear_cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                String str = "是否需要清除缓存？";
                int s = 1;
                dialogCommon(str, s, null);
            }
        });
        btn_about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                showAboutDialog();
            }
        });
        btn_out_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                String str1 = "是否退出登录？";
                int s1 = 2;
                dialogCommon(str1, s1, null);
            }
        });
    }

    /**
     * 检测设备对话框
     */
    private void showCheckDeviceDialog() {
        final CheckDeviceDialog.Builder builder = new CheckDeviceDialog.Builder(MainActivity.this);
        builder.setPositiveButton("", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setState(false);
                if (builder.getCurrentStep() == 0) {
                    builder.setCurrentStep(1);
                } else if (builder.getCurrentStep() == 1) {
                    builder.setCurrentStep(2);
                }
            }
        });
        builder.setNegativeButton("", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setState(true);
                if (builder.getCurrentStep() == 0) {
                    builder.setCurrentStep(1);
                } else if (builder.getCurrentStep() == 1) {
                    builder.setCurrentStep(2);
                }
            }
        });
        builder.setSingleButton("", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.reset();
            }
        });
        CheckDeviceDialog twoButtonDialog = builder.createTwoButtonDialog();
        twoButtonDialog.show();
//        WindowManager.LayoutParams params = twoButtonDialog.getWindow().getAttributes();
//        params.width = ScreenUtils.dip2px(getApplicationContext(), 366);
//        params.height = ScreenUtils.dip2px(getApplicationContext(), 297);
//        twoButtonDialog.getWindow().setAttributes(params);
    }

    private void showUserInfoDialogV2() {
        if (AppUtils.getUserInfoData() == null) return;
        userInfoDialogBuilder.setSex(AppUtils.getUserInfoData().getSex()).setName(AppUtils.getUserInfoData().getNameEn());
        userInfoDialog.setSaveClickLisener(new UserInfoDialogV2.OnSaveClickLisener() {
            @Override
            public void onClick(int sex, String name) {
                mPresenter.updateUserInfo(name, sex);
            }
        });
        userInfoDialog.show();
    }


    @Override
    protected void onStop() {
        super.onStop();
        cancelIntervalUpdateData();
    }

    /**
     * 清除缓存，退出登录通用对话框
     *
     * @param msg
     * @param type
     */
    private void dialogCommon(String msg, final int type, Map<String, String> data) {
        CommonDialog.Builder builder = new CommonDialog.Builder(this);
        builder.setMessage(msg);
        final CommonDialog twoButtonDialog = builder.createTwoButtonDialog();
        builder.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                twoButtonDialog.dismiss();
                if (type == 1) {
                    showLoading("清除中...");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            DataCleanManager.clearAllCache(MainActivity.this);
                            ToastUtils.showLongToast(MainActivity.this, "清除成功");
                            hideLoading();
                        }
                    }, 1000);
                    return;
                }
                if (type == 2) {
                    SPUtils.remove(Constant.SP_KEY_PASSWORD);
                    SPUtils.remove(Constant.SP_KEY_USERTOKEN);
                    SPUtils.remove(Constant.SP_KEY_USERINFO);
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                    return;
                }
                if (type == 3) {
                    if (data == null) return;
                    mPresenter.canelOrderClass(Integer.parseInt(data.get("demandId")));
                    return;
                }
            }
        });
        builder.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                twoButtonDialog.dismiss();
            }
        });
        twoButtonDialog.show();
    }

    /**
     * 关于弹窗
     */
    private void showAboutDialog() {
        new AboutDialog.Builder(MainActivity.this).create(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent3 = new Intent(MainActivity.this, InteractiveGamesActivity.class);
                mIntent3.putExtra(Constant.INTENT_DATA_KEY_GAME_URL, "https://hbr.gogo-talk.com/Privacy/index.html");
                startActivity(mIntent3);
            }
        }).show();
    }

    /**
     * 关闭轮训释放资源
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelIntervalUpdateData();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > 2000)  //System.currentTimeMillis()无论何时调用，肯定大于2000
            {
                ToastUtils.showLongToast(getApplicationContext(), "再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.id_mPersonalSettings, R.id.id_mRecord, R.id.id_GoGoTalk_Home, R.id.id_mBtn_HomePage, R.id.id_refresh})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_mPersonalSettings:
                showUserInfoDialogV2();
                break;
            case R.id.id_mRecord:
                startActivity(new Intent(MainActivity.this, RecordActivity.class));
                break;
            case R.id.id_GoGoTalk_Home:
                startActivity(new Intent(MainActivity.this, ClassListActivity.class));
                break;
            case R.id.id_mBtn_HomePage:
                startActivity(new Intent(MainActivity.this, ClassListActivity.class));
                break;
            case R.id.id_refresh:
                cancelIntervalUpdateData();
                isFirstLoadData = false;
                intervalUpdateData();
                break;
        }
    }


    //删除过期文件夹
    private void delectCoursewareFile() {
        File filesDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File[] files = filesDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (!files[i].getName().contains("apk")) {
                //创建超出一天删除
                if (System.currentTimeMillis() - files[i].lastModified() > 24 * 60 * 60 * 1000) {
                    DelectFileUtil.DeleteFolder(files[i]);
                }
            }
        }
    }

}
