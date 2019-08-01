package com.gogotalk.system.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import com.gogotalk.system.R;
import com.gogotalk.system.model.entity.RecordBean;
import com.gogotalk.system.presenter.RecordContract;
import com.gogotalk.system.presenter.RecordPresenter;
import com.gogotalk.system.view.adapter.RecordGridAdapter;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 课后练习
 */
public class RecordActivity extends BaseActivity<RecordPresenter> implements RecordContract.View {

    @BindView(R.id.id_mGridView_Record)
    GridView gridView;
    @BindView(R.id.id_mWUJL_Record)
    LinearLayout mLayout;
    private List<RecordBean> list = new ArrayList<>();
    private RecordGridAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.getClassRecordData();
    }

    @Override
    protected void initView() {
        super.initView();
        mAdapter = new RecordGridAdapter(RecordActivity.this, list);
        gridView.setAdapter(mAdapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_record;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @OnClick({R.id.id_mBack_Record, R.id.aboutclass_record})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_mBack_Record:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.aboutclass_record:
                startActivity(new Intent(RecordActivity.this, ClassListActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void showGridViewOrEmptyViewByFlag(boolean flag) {
        gridView.setVisibility(flag?View.VISIBLE:View.GONE);
        mLayout.setVisibility(!flag?View.VISIBLE:View.GONE);
    }

    @Override
    public void updateGridViewData(List<RecordBean> data) {
        list.clear();
        list.addAll(data);
        mAdapter.notifyDataSetChanged();
    }
}
