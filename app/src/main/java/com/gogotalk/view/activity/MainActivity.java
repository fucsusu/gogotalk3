package com.gogotalk.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gogotalk.R;
import com.gogotalk.model.entity.CoursesBean;
import com.gogotalk.model.util.Constant;
import com.gogotalk.presenter.MainContract;
import com.gogotalk.presenter.MainPresenter;
import com.gogotalk.util.AppUtils;
import com.gogotalk.util.DataCleanManager;
import com.gogotalk.util.SPUtils;
import com.gogotalk.util.ScreenUtils;
import com.gogotalk.util.ToastUtils;
import com.gogotalk.view.adapter.MainRecyclerAdapter;
import com.gogotalk.view.widget.AboutDialog;
import com.gogotalk.view.widget.CommonDialog;
import com.gogotalk.view.widget.SpaceItemDecoration;
import com.gogotalk.view.widget.UserInfoDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {
    @BindView(R.id.id_mRecyclerView)
    RecyclerView mRecyclerView;//课单列表
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
    @BindView(R.id.id_mLayout)
    LinearLayout idMLayout;
    private List<CoursesBean> list = new ArrayList<>();
    private Dialog dialog;
    private long exitTime = 0;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    MainRecyclerAdapter recyclerAdapter;
    private PopupWindow popupWindow;
    TextView btn_check_device, btn_clear_cache, btn_about_us, btn_out_login;
    RadioButton btn_setting;
    UserInfoDialog.Builder userInfoDialogBuilder;
    UserInfoDialog userInfoDialog;
    /**
     * 轮训刷新数据
     */
    Handler mHandler = new Handler();
    Runnable r = new Runnable() {
        @Override
        public void run() {
            mPresenter.getUserInfoData(false, false);
            mPresenter.getClassListData(false, false);
            mHandler.postDelayed(this, 1000 * 60 * 3);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEvent();
        mPresenter.getUserInfoData(true, false);
        mPresenter.getClassListData(false, true);
        mHandler.postDelayed(r, 1000 * 60 * 3);
    }

    /**
     * 复用activity实例回调刷新数据并重新启动轮训
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mPresenter.getUserInfoData(true, false);
        mPresenter.getClassListData(false, true);
        mHandler.postDelayed(r, 1000 * 60 * 3);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initView() {
        super.initView();
        userInfoDialogBuilder = new UserInfoDialog.Builder(this);
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
        mRecyclerView.setAdapter(recyclerAdapter);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dip2px(getApplicationContext(), 10), 0));
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


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            AppUtils.fullScreenImmersive(getWindow());
        }
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
                popupWindow.showAsDropDown(view, ScreenUtils.dip2px(MainActivity.this, -20), 10);
                AppUtils.fullScreenImmersive(popupWindow.getContentView());
                popupWindow.setFocusable(true);
                popupWindow.update();
            }
        });
        btn_check_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (mShowRequestPermission) {
                popupWindow.dismiss();
//                    showCheckDeviceDialog();
//                } else {
//                    Toast.makeText(HomePageActivity.this, "部分功能未授权，请授权后再试！", Toast.LENGTH_SHORT).show();
//                }
            }
        });
        btn_clear_cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                String str = "是否需要清除缓存？";
                int s = 1;
                dialogCommon(str, s);
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
                dialogCommon(str1, s1);
            }
        });
    }

    /**
     * 个人信息对话框
     */
    private void showUserInfoDialog() {
        userInfoDialogBuilder = new UserInfoDialog.Builder(this);
        userInfoDialog = userInfoDialogBuilder.setName(AppUtils.getUserInfoData().getName())
                .setSex(AppUtils.getUserInfoData().getSex())
                .setDate(AppUtils.getUserInfoData().getAge())
                .setHeader(AppUtils.getUserInfoData().getImageUrl()).create();
        userInfoDialog.show();
//        userInfoDialog.setOnNameClickLisener(new UserInfoDialog.OnNameClickLisener() {
//            @Override
//            public void onClick(int sex) {
//                Intent intent = new Intent(HomePageActivity.this, SelectNameActivity.class);
//                intent.putExtra(INTENT_SEX,sex);
//                startActivity(intent);
//                overridePendingTransition(0, 0);
//            }
//        });
    }

    /**
     * 暂停轮训
     */
    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(r);
    }

    /**
     * 清除缓存，退出登录通用对话框
     *
     * @param msg
     * @param type
     */
    private void dialogCommon(String msg, final int type) {
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
                            ToastUtils.showShortToast(MainActivity.this, "清除成功");
                            hideLoading();
                        }
                    }, 1000);

                }
                if (type == 2) {
                    SPUtils.remove(Constant.SP_KEY_PASSWORD);
                    SPUtils.remove(Constant.SP_KEY_USERTOKEN);
                    SPUtils.remove(Constant.SP_KEY_USERINFO);
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
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
        new AboutDialog.Builder(MainActivity.this).create().show();
    }

    /**
     * 关闭轮训释放资源
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(r);
        mHandler = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > 2000)  //System.currentTimeMillis()无论何时调用，肯定大于2000
            {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.id_mPersonalSettings, R.id.id_mRecord})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_mPersonalSettings:
                showUserInfoDialog();
                break;
            case R.id.id_mRecord:
                startActivity(new Intent(MainActivity.this, RecordActivity.class));
                break;
        }
    }
}
