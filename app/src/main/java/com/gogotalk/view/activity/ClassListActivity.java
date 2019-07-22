package com.gogotalk.view.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import com.gogotalk.R;
import com.gogotalk.model.entity.BookLevelBean;
import com.gogotalk.model.entity.GoGoBean;
import com.gogotalk.model.entity.GoItemBean;
import com.gogotalk.presenter.ClassListContract;
import com.gogotalk.presenter.ClassListPresenter;
import com.gogotalk.util.AppUtils;
import com.gogotalk.view.adapter.ClassListAdapter;
import com.gogotalk.view.adapter.ClassListLevelAdapter;
import com.gogotalk.view.adapter.ClassListUnitAdapter;
import com.gogotalk.view.widget.SpaceItemDecoration;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;


public class ClassListActivity extends BaseActivity<ClassListPresenter> implements ClassListContract.View {

    @BindView(R.id.id_mRecycler_GoGo)
    RecyclerView recyclerView;
    @BindView(R.id.id_mRecycler1_GoGo)
    RecyclerView mRecyclerView;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.getClassByLevel(AppUtils.getUserInfoData().getLevel());
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
                if (!popupWindow.isShowing()) {
                    ivLevel.setImageResource(R.mipmap.ic_class_list_drop_up);
                    popupWindow.setFocusable(false);
                    popupWindow.update();
                    if("MI 8".equals(Build.MODEL)){
                        popupWindow.showAsDropDown(v, AppUtils.getNavigationBarHeight(ClassListActivity.this)-25, 10);
                    }else{
                        popupWindow.showAsDropDown(v, 10, 10);
                    }
                    AppUtils.fullScreenImmersive(popupWindow.getContentView());
                    popupWindow.setFocusable(true);
                    popupWindow.update();
                    mPresenter.getLevelListData();
                }
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
    public void onDestroy() {
        super.onDestroy();
        if(classAdapter!=null){
            classAdapter.destoryDialog();
        }
    }

    @Override
    public void updateLevelRecelyerViewData(List<BookLevelBean> beans) {
        levelBeans.clear();
        levelBeans.addAll(beans);
        levelAdapter.notifyDataSetChanged();
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
}
