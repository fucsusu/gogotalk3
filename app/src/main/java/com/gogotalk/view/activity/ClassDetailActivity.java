package com.gogotalk.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import com.gogotalk.R;
import com.gogotalk.model.entity.ClassDetailBean;
import com.gogotalk.model.entity.WeekMakeBean;
import com.gogotalk.model.util.Constant;
import com.gogotalk.presenter.ClassDetailContract;
import com.gogotalk.presenter.ClassDetailPresenter;
import com.gogotalk.util.AppUtils;
import com.gogotalk.util.ToastUtils;
import com.gogotalk.view.widget.YuYueDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


public class ClassDetailActivity extends BaseActivity<ClassDetailPresenter> implements ClassDetailContract.View {
    private int id;
    private int chapterId;
    private int bookId;
    @BindView(R.id.id_mNumberTrophies_Details)
    TextView mNumberTrophies;
    @BindView(R.id.id_mImge_Details)
    ImageView mImg;
    @BindView(R.id.id_mUserName_Details)
    TextView mUserName;
    @BindView(R.id.id_mEnglishName_Details)
    TextView mEnglishName;
    @BindView(R.id.id_mTime_Details)
    TextView mTime;

    private String videoUrl;//主视频地址
    private String gameUrl;//课后互动练习地址
    private String previewUrl;//课前联系
    private YuYueDialog yuYueDialog;
    private YuYueDialog.Builder builder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.getClassDetailData(String.valueOf(id));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_class_detail;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void initView() {
        builder = new YuYueDialog.Builder(ClassDetailActivity.this);
        yuYueDialog = builder.create();
        builder.setIyuYuClickListener(new YuYueDialog.IyuYuClickListener() {
            @Override
            public void yuYuClick(String date, String time) {
                if (TextUtils.isEmpty(time)) {
                    ToastUtils.showShortToast(ClassDetailActivity.this, "请选择约课时间");
                    return;
                }
                String lessonTime = date + " " + time;
                mPresenter.orderClass(bookId,chapterId,lessonTime);
            }
        });
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        id = getIntent().getIntExtra(Constant.INTENT_DATA_KEY_RECORD_ID, 0);
    }

    @OnClick({R.id.id_mBack_Details, R.id.id_mMainCourseView_Details, R.id.id_mGame_Details
            , R.id.id_mPreview_Details, R.id.id_mMakeAppointment_Details})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_mBack_Details://返回键
                finish();
                break;
            case R.id.id_mMainCourseView_Details://主课视频
                if (!TextUtils.isEmpty(videoUrl)) {
                    Intent mIntent1 = new Intent(this, VideoActivity.class);
                    mIntent1.putExtra(Constant.INTENT_DATA_KEY_VIDEO_URL, videoUrl);
                    startActivity(mIntent1);
                }
                break;
            case R.id.id_mGame_Details://互动游戏
                if (!TextUtils.isEmpty(gameUrl)) {
                    Intent mIntent3 = new Intent(this, InteractiveGamesActivity.class);
                    mIntent3.putExtra(Constant.INTENT_DATA_KEY_GAME_URL, gameUrl);
                    startActivity(mIntent3);
                }
                break;
            case R.id.id_mPreview_Details://课前预习
                if (!TextUtils.isEmpty(previewUrl)) {
                    Intent mIntent = new Intent(this, VideoActivity.class);
                    mIntent.putExtra(Constant.INTENT_DATA_KEY_VIDEO_URL, previewUrl);
                    startActivity(mIntent);
                }
                break;
            case R.id.id_mMakeAppointment_Details://立即预约
                mPresenter.getWeekMakeBean();
                break;

        }
    }

    @Override
    public void setClassDetailDataToView(ClassDetailBean bean) {
        mNumberTrophies.setText(String.valueOf(bean.getGiftCupNum()));
        AppUtils.bindImageToView(ClassDetailActivity.this
        ,bean.getCoverImgUrl(),R.mipmap.ic_main_list_item_header_default
        ,mImg,null,false);
        mUserName.setText(bean.getChapterEnglishName());
        mEnglishName.setText(bean.getChapterName());
        mTime.setText(bean.getLessonTime());
        videoUrl = bean.getChapterVidelUrl();
        gameUrl = bean.getAfterFilePath();
        previewUrl = bean.getBeforeVideoUrl();
        chapterId = bean.getChapterID();
        bookId = bean.getBookID();
    }

    @Override
    public void setDataToYuyueDialogShow(List<WeekMakeBean> beans) {
        builder.setWeekBeans(beans);
        yuYueDialog.show();
    }

    @Override
    public void onOrderClassSuccess() {
        yuYueDialog.dismiss();
    }

}
