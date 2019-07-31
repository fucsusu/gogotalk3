package com.gogotalk.system.view.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.gogotalk.system.R;
import com.gogotalk.system.model.entity.EnglishNameListBean;
import com.gogotalk.system.model.util.Constant;
import com.gogotalk.system.presenter.SelectNameContract;
import com.gogotalk.system.presenter.SelectNamePresenter;
import com.gogotalk.system.util.ScreenUtils;
import com.gogotalk.system.view.adapter.SelectNameAdapter;
import com.gogotalk.system.view.widget.SectionedSpanSizeLookup;
import com.gogotalk.system.view.widget.WaveSideBar;
import butterknife.BindView;
import butterknife.OnClick;

public class SelectNameActivity extends BaseActivity<SelectNamePresenter> implements SelectNameContract.View {
    @BindView(R.id.rv_select_name)
    RecyclerView mRecyclerView;
    @BindView(R.id.sideBar)
    WaveSideBar mSideBar;
    private SelectNameAdapter mAdapter;
    private GridLayoutManager manager;
    @BindView(R.id.layout_search)
    RelativeLayout layoutSearch;
    private int sex;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.getEnglishNameListData(sex);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_name;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        sex = getIntent().getIntExtra("sex", 0);
    }

    @Override
    public void initView() {
        //设置右侧SideBar触摸监听
        mSideBar.setOnTouchLetterChangeListener(new WaveSideBar.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(int letter) {
            //该字母首次出现的位置
            int position = mAdapter.getPositionForSection(letter);
            if (position != -1) {
                manager.scrollToPositionWithOffset(position+letter-65, 0);
            }
            }
        });
        layoutSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectNameActivity.this, SearchNameActivity.class);
                intent.putExtra(Constant.INTENT_DATA_KEY_SEX,sex);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        manager = new GridLayoutManager(this,5);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dip2px(SelectNameActivity.this,11)));
    }

    @Override
    public void updateRecelyerViewData(EnglishNameListBean bean) {
        mAdapter = new SelectNameAdapter(bean);
        SectionedSpanSizeLookup lookup = new SectionedSpanSizeLookup(mAdapter, manager);
        manager.setSpanSizeLookup(lookup);
        mRecyclerView.setAdapter(mAdapter);
    }

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int space;
        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.bottom = space;
        }

    }
    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
    }
}
