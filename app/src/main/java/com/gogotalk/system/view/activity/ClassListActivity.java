package com.gogotalk.system.view.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gogotalk.system.R;
import com.gogotalk.system.app.AppManager;
import com.gogotalk.system.model.entity.BookLevelBean;
import com.gogotalk.system.model.entity.GoGoBean;
import com.gogotalk.system.model.entity.GoItemBean;
import com.gogotalk.system.model.entity.ResponseModel;
import com.gogotalk.system.model.entity.RoomInfoBean;
import com.gogotalk.system.model.entity.WeekMakeBean;
import com.gogotalk.system.model.util.Constant;
import com.gogotalk.system.presenter.ClassListContract;
import com.gogotalk.system.presenter.ClassListPresenter;
import com.gogotalk.system.util.AppUtils;
import com.gogotalk.system.util.CoursewareDownLoadUtil;
import com.gogotalk.system.util.PermissionsUtil;
import com.gogotalk.system.util.ToastUtils;
import com.gogotalk.system.view.adapter.ClassListAdapter;
import com.gogotalk.system.view.adapter.ClassListLevelAdapter;
import com.gogotalk.system.view.adapter.ClassListUnitAdapter;
import com.gogotalk.system.view.widget.CommonDialog;
import com.gogotalk.system.view.widget.SpaceItemDecoration;
import com.gogotalk.system.view.widget.YuYueDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 预约课程列表
 */
public class ClassListActivity extends BaseActivity<ClassListPresenter> implements ClassListContract.View {

    @BindView(R.id.id_mRecycler_GoGo)
    RecyclerView recyclerView;
    @BindView(R.id.id_mRecycler1_GoGo)
    RecyclerView mRecyclerView;
    @BindView(R.id.class_list_rootview)
    LinearLayout root_view;

    @BindView(R.id.layout_level)
    LinearLayout layoutLevel;
    @BindView(R.id.tv_level)
    TextView tvLevel;
    @BindView(R.id.iv_level)
    ImageView ivLevel;
    private RecyclerView lvLevel;
    private List<BookLevelBean> levelBeans = new ArrayList<>();
    private List<GoGoBean> unitBeans = new ArrayList<>();
    private List<GoItemBean> classBeans = new ArrayList<>();
    ClassListUnitAdapter unitAdapter;
    ClassListAdapter classAdapter;
    ClassListLevelAdapter levelAdapter;
    private PopupWindow popupWindow;
    private YuYueDialog yuYueDialog;
    private YuYueDialog.Builder builder;
    private int mBookId;
    private int mChaptId;
    private int currentLevel = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.getLevelListData();
        mPresenter.getClassByLevel(currentLevel);
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        currentLevel = AppUtils.getUserInfoData().getLevel();
        int direction = getIntent().getIntExtra(Constant.INTENT_DATA_KEY_DIRECTION, 0);
        switch (direction) {
            case Constant.DIRECTION_LEVEL_TO_CLASS:
                currentLevel = getIntent().getIntExtra(Constant.INTENT_DATA_KEY_LEVEL, 1);
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_class_list;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            AppUtils.fullScreenImmersive(getWindow());
        }
    }

    /**
     * 初始化
     */
    @Override
    public void initView() {
        builder = new YuYueDialog.Builder(ClassListActivity.this);
        yuYueDialog = builder.create();
        builder.setIyuYuClickListener(new YuYueDialog.IyuYuClickListener() {
            @Override
            public void yuYuClick(String date, String time) {
                if (TextUtils.isEmpty(time)) {
                    ToastUtils.showLongToast(ClassListActivity.this, "请选择约课时间");
                    return;
                }
                String lessonTime = date + " " + time;
                mPresenter.orderClass(mBookId, mChaptId, lessonTime);
            }
        });
        View inflate = LayoutInflater.from(this).inflate(R.layout.popup_level, null, false);
        lvLevel = inflate.findViewById(R.id.lv_level);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        // 设置布局管理器
        lvLevel.setLayoutManager(manager);
        // 设置adapter
        levelAdapter = new ClassListLevelAdapter(levelBeans);
        levelAdapter.setOnItemClickListener(new ClassListLevelAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                tvLevel.setText(levelBeans.get(position).getBookName());
                mPresenter.getClassByLevel(levelBeans.get(position).getBookLevel());
                popupWindow.dismiss();
            }
        });
        lvLevel.setAdapter(levelAdapter);
        LinearLayoutManager manager1 = new LinearLayoutManager(this);
        manager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager1);
        unitAdapter = new ClassListUnitAdapter(unitBeans);
        recyclerView.setAdapter(unitAdapter);
        unitAdapter.setOnItemClickListener(new ClassListUnitAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                for (GoGoBean bean : unitBeans) {
                    bean.setChecked(false);
                }
                unitBeans.get(postion).setChecked(true);
                unitAdapter.notifyDataSetChanged();
                classBeans.clear();
                classBeans.addAll(unitBeans.get(postion).getChapterData());
                classAdapter.notifyDataSetChanged();
            }
        });
        LinearLayoutManager manager2 = new LinearLayoutManager(this);
        manager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager2);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(10, 0));
        classAdapter = new ClassListAdapter(this, classBeans);
        classAdapter.setBtnClickLisener(new ClassListAdapter.IBtnClickLisener() {

            @Override
            public void onBtnClassPreviewClick(String path) {
                Intent mIntent = new Intent(ClassListActivity.this, VideoActivity.class);
                mIntent.putExtra(Constant.INTENT_DATA_KEY_VIDEO_URL, path);
                startActivity(mIntent);
            }

            @Override
            public void onBtnGoClassRoomClick(boolean flag, GoItemBean goItemBean) {
                if (!flag) {
                    ToastUtils.showLongToast(ClassListActivity.this, "课前10分钟才可以进入教室");
                    return;
                }

//                if (PermissionsUtil.getInstance().isPermissions()) {
//                    ToastUtils.showLongToast(ClassListActivity.this, "部分功能未授权，请授权！");
//                }
                CoursewareDownLoadUtil.getCoursewareUtil().downloadCourseware(ClassListActivity.this, goItemBean.getZipDownLoadUrl(),
                        root_view, goItemBean.getZipEncrypInfo(), new CoursewareDownLoadUtil.CoursewareDownFinsh() {
                            @Override
                            public void finsh(String filePath) {
                                if (TextUtils.isEmpty(filePath)) {
                                    ToastUtils.showLongToast(ClassListActivity.this, "课件下载失败请查看网络是否连接正常！");
                                    return;
                                }
                                mPresenter.getRoomInfo(goItemBean, filePath);
                            }
                        });
            }

            @Override
            public void onYuYueClick(int bookId, int chaptId) {
                mBookId = bookId;
                mChaptId = chaptId;
                mPresenter.getWeekMakeBean();
            }
        });
        mRecyclerView.setAdapter(classAdapter);
        popupWindow = new PopupWindow(inflate, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ivLevel.setImageResource(R.mipmap.ic_class_list_drop_down);
            }
        });
    }


    @Override
    public void onRoomInfoSuccess(RoomInfoBean bean, String filePath) {
        Intent mIntent = new Intent(ClassListActivity.this, ClassRoomActivity.class);
        mIntent.putExtra(Constant.INTENT_DATA_KEY_CLASS_ID, bean.getAttendLessonID() + "");
        mIntent.putExtra(Constant.INTENT_DATA_KEY_BEGIN_TIME, bean.getLessonTime());
        mIntent.putExtra(Constant.INTENT_DATA_KEY_TEACHER_NAME, bean.getTeacherName());
        mIntent.putExtra(Constant.INTENT_DATA_KEY_DOWNLOAD_FILE_PATH, filePath);
        mIntent.putExtra(Constant.INTENT_DATA_KEY_MY, bean.getMyStudentSoundUrl());
        mIntent.putExtra(Constant.INTENT_DATA_KEY_OTHER, bean.getOtherStudentSoundUrl());
        mIntent.putExtra(Constant.INTENT_DATA_KEY_OTHER_NAME, bean.getOtherStudnetName());
        mIntent.putExtra(Constant.INTENT_DATA_KEY_OTHER_NAME_ID, bean.getOtherStudentId());
        mIntent.putExtra(Constant.INTENT_DATA_KEY_DETADIL_RECORDID, bean.getDetialRecordID());
        mIntent.putExtra(Constant.INTENT_DATA_KEY_OWNJBNUM, bean.getMyGiftCupNum());
        mIntent.putExtra(Constant.INTENT_DATA_KEY_OTHERJBNUM, bean.getOtherGiftCupNum());
        startActivity(mIntent);
    }

    /**
     * 控件点击事件
     *
     * @param v
     */
    @OnClick({R.id.id_mBack_GoGo, R.id.layout_level})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_mBack_GoGo:
                this.onBackPressed();
                break;
            case R.id.layout_level:
                Intent intent = new Intent(ClassListActivity.this, LevelActivity.class);
                intent.putExtra(Constant.INTENT_DATA_KEY_DIRECTION, Constant.DIRECTION_CLASS_TO_LEVEL);
                intent.putExtra(Constant.INTENT_DATA_KEY_LEVEL, currentLevel);
                startActivity(intent);
//                if (!popupWindow.isShowing()) {
//                    ivLevel.setImageResource(R.mipmap.ic_class_list_drop_up);
//                    popupWindow.setFocusable(false);
//                    popupWindow.update();
//                    if ("MI 8".equals(Build.MODEL)) {
//                        popupWindow.showAsDropDown(v, AppUtils.getNavigationBarHeight(ClassListActivity.this) - 25, 10);
//                    } else {
//                        popupWindow.showAsDropDown(v, 10, 10);
//                    }
//                    AppUtils.fullScreenImmersive(popupWindow.getContentView());
//                    popupWindow.setFocusable(true);
//                    popupWindow.update();
//                }
                break;
        }
    }

    /**
     * 系统返回键
     */
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }


    @Override
    public void updateLevelRecelyerViewData(List<BookLevelBean> beans) {
        levelBeans.clear();
        levelBeans.addAll(beans);
        levelAdapter.notifyDataSetChanged();
        for (BookLevelBean levelBean : levelBeans) {
            if (currentLevel == levelBean.getBookLevel()) {
                tvLevel.setText(levelBean.getBookName());
            }
        }
    }

    @Override
    public void updateUnitAndClassRecelyerViewData(List<GoGoBean> beans) {
        unitBeans.clear();
        classBeans.clear();
        beans.get(0).setChecked(true);
        unitBeans.addAll(beans);
        classBeans.addAll(beans.get(0).getChapterData());
        unitAdapter.notifyDataSetChanged();
        classAdapter.notifyDataSetChanged();
    }

    //约课成功
    @Override
    public void onOrderClassSuccess(String msg) {
        yuYueDialog.dismiss();
        ToastUtils.showShortToast(this, "约课成功！");
//        if (TextUtils.isEmpty(msg)) {
//        } else {
//            CommonDialog.Builder builder = new CommonDialog.Builder(ClassListActivity.this);
//            builder.setMessage(msg);
//            builder.setPositiveButton("知道啦", new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    builder.getDialog().dismiss();
//                }
//            });
//            builder.createOneButtonDialog().show();
//        }
    }

    @Override
    public void setDataToYuyueDialogShow(ResponseModel<List<WeekMakeBean>> beans) {
        builder.setTips(beans.getMsg());
        if (beans.getData() != null) {
            builder.setWeekBeans(beans.getData());
        }
        yuYueDialog.show();
    }
}
