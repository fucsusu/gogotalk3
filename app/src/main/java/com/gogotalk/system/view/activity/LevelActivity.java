package com.gogotalk.system.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gogotalk.system.R;
import com.gogotalk.system.app.AppManager;
import com.gogotalk.system.model.entity.LevelListBean;
import com.gogotalk.system.model.entity.LevelResultBean;
import com.gogotalk.system.model.entity.QuestionsBean;
import com.gogotalk.system.model.entity.UserInfoBean;
import com.gogotalk.system.model.util.Constant;
import com.gogotalk.system.presenter.LevelContract;
import com.gogotalk.system.presenter.LevelPresenter;
import com.gogotalk.system.util.AppUtils;
import com.gogotalk.system.util.ToastUtils;
import com.gogotalk.system.view.widget.DateTimeBottomDialog;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import jsc.kit.wheel.dialog.DateTimeWheelDialog;

/**
 * 登录页
 */
public class LevelActivity extends BaseActivity<LevelPresenter> implements LevelContract.View {


    @BindView(R.id.tv_level_title)
    TextView tvLevelTitle;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.rb_man)
    RadioButton rbMan;
    @BindView(R.id.rb_women)
    RadioButton rbWomen;
    @BindView(R.id.rg_sex)
    RadioGroup rgSex;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.layout_select_name)
    LinearLayout layoutSelectName;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;
    @BindView(R.id.layout_birthday)
    LinearLayout layoutBirthday;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.layout_step_01)
    View layoutStep01;
    @BindView(R.id.layout_step_02)
    View layoutStep02;
    @BindView(R.id.layout_step_03)
    View layoutStep03;
    DateTimeBottomDialog dialog = null;
    @BindView(R.id.tv_question_title)
    TextView tvQuestionTitle;
    @BindView(R.id.rg_question_item)
    RadioGroup rgQuestionItem;
    @BindView(R.id.rg_tab)
    RadioGroup rgTab;
    @BindView(R.id.btn_next_repeat)
    Button btnNextRepeat;
    @BindView(R.id.btn_enter)
    Button btnEnter;
    private int currentSex = 1;
    private int currentQuestionIndex = 0;
    private int currentStep = 1;
    private Map<String,String> questionResult=new HashMap<>();
    private QuestionsBean questionsBean;
    private LevelResultBean resultBean;
    private List<LevelListBean> levelListBeans=new ArrayList<>();
    private int currentLevel = 1;
    private Map<String,String> requestParams=new HashMap<>();
    private int direction;
    private long exitTime = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_level;
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        showBtnBack(false);
        direction = getIntent().getIntExtra(Constant.INTENT_DATA_KEY_DIRECTION, 0);
        switch (direction){
            case Constant.DIRECTION_CLASS_TO_LEVEL:
                currentLevel = getIntent().getIntExtra(Constant.INTENT_DATA_KEY_LEVEL, 1);
                showBtnBack(true);
                switchLayout(3);
                mPresenter.getLeveInfoList();
                break;
        }
    }

    @Override
    protected void initView() {
        super.initView();
        UserInfoBean userInfoData = AppUtils.getUserInfoData();
        int sex = userInfoData.getSex();
        if(sex==0){
            rgSex.check(R.id.rb_women);
            currentSex = 0;
        }else if(sex==0){
            rgSex.check(R.id.rb_man);
            currentSex = 1;
        }
        tvName.setText(TextUtils.isEmpty(userInfoData.getNameEn())?"":userInfoData.getNameEn());
        tvBirthday.setText(TextUtils.isEmpty(userInfoData.getAge())?"":userInfoData.getAge());
        checkData();
        rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rb_man) {
                    currentSex = 1;
                } else {
                    currentSex = 0;
                }
                tvName.setText("");
                checkData();
            }
        });
        rgQuestionItem.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radiobutton = (RadioButton) radioGroup.getChildAt(i);
                if(radiobutton ==null)return;
                for(int j=0;j<radioGroup.getChildCount();j++){
                    ((RadioButton)radioGroup.getChildAt(j)).setTextColor(Color.parseColor("#333333"));
                }
                radiobutton.setTextColor(Color.WHITE);
                QuestionsBean.ListBean listBean = questionsBean.getList().get(currentQuestionIndex);
                questionResult.put(""+(int)listBean.getKey(),""+(int)listBean.getQuestionValue().get(i).getValue());
                btnNextRepeat.setEnabled(true);
                btnNextRepeat.setBackgroundResource(R.mipmap.bg_level_btn_next_active);
            }
        });
        rgTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radiobutton = (RadioButton) radioGroup.getChildAt(i);
                if(radiobutton ==null)return;
                for(int j=0;j<radioGroup.getChildCount();j++){
                    ((RadioButton)radioGroup.getChildAt(j)).setTextColor(Color.parseColor("#949494"));
                }
                currentLevel = (int)levelListBeans.get(i).getLevel();
                setTabState(levelListBeans.get(i), radiobutton);
                radiobutton.setTextColor(Color.WHITE);
                AppUtils.bindImageToView(LevelActivity.this,levelListBeans.get(i).getPhoneImgUrl(),0,ivBg,null,false,0);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            switch (intent.getIntExtra(Constant.INTENT_DATA_KEY_DIRECTION, 0)) {
                case Constant.DIRECTION_SELECTNAME_TO_LEVEL:
                    tvName.setText(intent.getStringExtra(Constant.INTENT_DATA_KEY_NAME));
                    checkData();
                    return;
            }
        }
    }

    private boolean checkData() {
        if (!TextUtils.isEmpty(tvName.getText()) && !TextUtils.isEmpty(tvBirthday.getText())) {
            btnNext.setBackgroundResource(R.mipmap.bg_level_btn_next_active);
            btnNext.setEnabled(true);
            return true;
        }
        btnNext.setEnabled(false);
        btnNext.setBackgroundResource(R.mipmap.bg_level_btn_next_gray);
        return false;
    }

    @OnClick({R.id.btn_back, R.id.btn_next, R.id.layout_select_name, R.id.layout_birthday,R.id.btn_next_repeat,R.id.btn_enter})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_enter:
                if(direction!=Constant.DIRECTION_CLASS_TO_LEVEL){
                    mPresenter.updateStudentLevel(currentLevel);
                }
                Intent intent1 = new Intent(LevelActivity.this, ClassListActivity.class);
                intent1.putExtra(Constant.INTENT_DATA_KEY_DIRECTION,Constant.DIRECTION_LEVEL_TO_CLASS);
                intent1.putExtra(Constant.INTENT_DATA_KEY_LEVEL,currentLevel);
                startActivity(intent1);
                finish();
                break;
            case R.id.btn_back:
                if(direction==Constant.DIRECTION_CLASS_TO_LEVEL){
                    finish();
                    return;
                }
                currentStep--;
                switchLayout(currentStep);
                tvLevelTitle.setText("完善宝贝信息");
                if(currentStep==1){
                    currentQuestionIndex--;
                    if(currentQuestionIndex<0){
                        currentQuestionIndex = 0;
                        switchLayout(currentStep);
                        showBtnBack(false);
                    }else{
                        setQuestionUI();
                    }
                }else{
                    switchLayout(currentStep);
                }
                break;
            case R.id.btn_next:
                currentStep++;
                showBtnBack(true);
                requestParams.clear();
                requestParams.put("EName",tvName.getText().toString());
                requestParams.put("Birthday",tvBirthday.getText().toString());
                requestParams.put("Gender",currentSex+"");
                mPresenter.getSurveyQuestion();
                break;
            case R.id.layout_select_name:
                Intent intent = new Intent(LevelActivity.this, SelectNameActivity.class);
                intent.putExtra(Constant.INTENT_DATA_KEY_SEX, currentSex);
                intent.putExtra(Constant.INTENT_DATA_KEY_DIRECTION, Constant.DIRECTION_LEVEL_TO_SELECTNAME);
                startActivity(intent);
                break;
            case R.id.layout_birthday:
                if (dialog == null) {
                    dialog = createDialog();
                } else {
                    dialog.show();
                }
                break;
            case R.id.btn_next_repeat:
                currentStep++;
                showBtnBack(true);
                ++currentQuestionIndex;
                //当前题下标小于题目总数说明还有下一题
                if(currentQuestionIndex<questionsBean.getList().size()){
                    //展示下一题
                    setQuestionUI();
                }else{  //没有题目了提交数据
                    currentQuestionIndex = 0;
                    requestParams.put("StudyTime",questionResult.get("1"));
                    mPresenter.gradeInvestigation(requestParams);
                }
                break;
        }
    }

    private void showBtnBack(boolean flag){
        btnBack.setVisibility(flag?View.VISIBLE:View.GONE);
    }
    /**
     * 切换步骤布局
     * @param step
     */
    private void switchLayout(int step) {
        if (step == 1) {
            layoutStep01.setVisibility(View.VISIBLE);
            layoutStep02.setVisibility(View.GONE);
            layoutStep03.setVisibility(View.GONE);
            return;
        }
        if (step == 2) {
            layoutStep01.setVisibility(View.GONE);
            layoutStep02.setVisibility(View.VISIBLE);
            layoutStep03.setVisibility(View.GONE);
            return;
        }
        if (step == 3) {
            layoutStep01.setVisibility(View.GONE);
            layoutStep02.setVisibility(View.GONE);
            layoutStep03.setVisibility(View.VISIBLE);
            return;
        }
    }

    /**
     * 生成时间选择底部弹出框
     * @return
     */
    private DateTimeBottomDialog createDialog() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 1900);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = calendar.getTime();
        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) + 3);
        Date endDate = calendar.getTime();

        DateTimeBottomDialog dialog = new DateTimeBottomDialog(getActivity());
        dialog.show();
        dialog.setTitle("选择出生日期");
        int config = DateTimeWheelDialog.SHOW_YEAR_MONTH_DAY;
        dialog.configShowUI(config);
        dialog.setCancelButton("取消", null);
        dialog.setOKButton("确定", new DateTimeWheelDialog.OnClickCallBack() {
            @Override
            public boolean callBack(View v, @NonNull Date selectedDate) {
                if (selectedDate.after(new Date())) {
                    dialog.updateSelectedDate(new Date());
                    return true;
                }
                tvBirthday.setText(new SimpleDateFormat("yyyy-MM-dd").format(selectedDate));
                checkData();
                return false;
            }
        });
        dialog.setDateArea(startDate, endDate, true);
        dialog.updateSelectedDate(new Date());
        return dialog;
    }

    /**
     * 获取调查问题回调
     * @param bean
     */
    @Override
    public void showQuestions(QuestionsBean bean) {
        if(bean==null)return;
        if(bean.getList()==null)return;
        if(bean.getList().size()==0)return;
        this.questionsBean = bean;
        setQuestionUI();
        switchLayout(2);
    }

    /**
     * 分级返回结果回调
     * @param bean
     */
    @Override
    public void showLevelResult(LevelResultBean bean) {
        if(bean==null)return;
        mPresenter.getLeveInfoList();
        this.resultBean = bean;
        currentLevel = (int)bean.getLevel();

    }

    @Override
    public void onGetLevelListSuccess(List<LevelListBean> beans) {
        if(beans==null)return;
        if(beans.size()==0)return;
        levelListBeans.clear();
        levelListBeans.addAll(beans);
        String color="";
        double tempLevel;
        if(direction==Constant.DIRECTION_CLASS_TO_LEVEL){
            tempLevel= Double.valueOf(currentLevel);
        }else{
            tempLevel = resultBean.getLevel();
        }
        for(LevelListBean bean1:beans){
            if(direction!=Constant.DIRECTION_CLASS_TO_LEVEL){
                if(tempLevel==bean1.getLevel()){
                    color = bean1.getColor();
                    AppUtils.bindImageToView(this,bean1.getPhoneImgUrl(),0,ivBg,null,false,0);
                }
            }else{
                if(AppUtils.getUserInfoData().getLevel()==bean1.getLevel()){
                    color = bean1.getColor();
                    AppUtils.bindImageToView(this,bean1.getPhoneImgUrl(),0,ivBg,null,false,0);
                }
            }

        }
        switchLayout(3);
        String source="";
        if(direction!=Constant.DIRECTION_CLASS_TO_LEVEL){
             source = resultBean.getTitle() + resultBean.getLevelName();
        }else{
            source="根据宝贝当前情况，建议学习起点为L"+AppUtils.getUserInfoData().getLevel();
        }
        SpannableString spannableString = new SpannableString(source);
        @SuppressLint("Range") ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor(color));
        spannableString.setSpan(colorSpan, source.lastIndexOf("L"), spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvLevelTitle.setText(spannableString);
        setTabUI(tempLevel);
    }

    /**
     * 动态生成问题选项按钮
     */
    private void setQuestionUI() {
        int count = 0;
        rgQuestionItem.removeAllViewsInLayout();
        tvQuestionTitle.setText(questionsBean.getList().get(currentQuestionIndex).getQuestion());
        for(QuestionsBean.ListBean.QuestionValueBean bean1:questionsBean.getList().get(currentQuestionIndex).getQuestionValue()){
            RadioButton radioButton = (RadioButton) LayoutInflater.from(this).inflate(R.layout.layout_level_question_item, null);
            radioButton.setText(bean1.getText());
            radioButton.setId(count);
            RadioGroup.MarginLayoutParams params = new RadioGroup.MarginLayoutParams((int) (getResources().getDimension(R.dimen.qb_px_361)), (int) (getResources().getDimension(R.dimen.qb_px_30)));
            if(count>0){
                params.topMargin = (int) (getResources().getDimension(R.dimen.qb_px_14));
            }
            if(questionResult!=null
                    &&questionResult.size()>0
                    &&questionResult.get(""+(int)questionsBean.getList().get(currentQuestionIndex).getKey()).equals(""+(int)bean1.getValue())){
                radioButton.setChecked(true);
                radioButton.setTextColor(Color.WHITE);
            }
            rgQuestionItem.addView(radioButton,count,params);
            count++;
        }
    }

    /**
     * 动态生成tab按钮
     */
    private void setTabUI(double tempLevel) {
        int count = 0;
        rgTab.removeAllViewsInLayout();
        for(LevelListBean bean1:levelListBeans){
            RadioButton radioButton = (RadioButton) LayoutInflater.from(this).inflate(R.layout.layout_level_tab_item, null);
            radioButton.setText(bean1.getLeveName());
            radioButton.setId(count);
            if(tempLevel==bean1.getLevel()){
                setTabState(bean1, radioButton);
                radioButton.setChecked(true);
                radioButton.setTextColor(Color.WHITE);
            }
            RadioGroup.MarginLayoutParams params = new RadioGroup.MarginLayoutParams((int) (getResources().getDimension(R.dimen.qb_px_123)), (int) (getResources().getDimension(R.dimen.qb_px_35)));
            rgTab.addView(radioButton,count,params);
            count++;
        }
    }

    /**
     * 根据后台返回的颜色，设置tab选中和未选中状态颜色
     * @param bean1
     * @param radioButton
     */
    private void setTabState(LevelListBean bean1, RadioButton radioButton) {
        StateListDrawable sld = new StateListDrawable();
        GradientDrawable shapeDrawable= (GradientDrawable) getResources().getDrawable(R.drawable.bg_level_step_tab_item_active);
        shapeDrawable.setColor(Color.parseColor(bean1.getColor()));
        sld.addState(new int[]{android.R.attr.state_checked},shapeDrawable);
        sld.addState(new int[]{-android.R.attr.state_checked},getResources().getDrawable(R.drawable.bg_level_step_tab_item));
        radioButton.setBackground(sld);
    }

    @Override
    public void onBackPressed() {
        if(direction==Constant.DIRECTION_CLASS_TO_LEVEL){
            super.onBackPressed();
        }else{
            if ((System.currentTimeMillis() - exitTime) > 2000)  //System.currentTimeMillis()无论何时调用，肯定大于2000
            {
                ToastUtils.showLongToast(getApplicationContext(), "再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                AppManager.getAppManager().finishAllActivity();
                System.exit(0);
            }
        }
    }
}
