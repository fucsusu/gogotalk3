package com.gogotalk.system.view.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gogotalk.system.R;
import com.gogotalk.system.presenter.SearchNameContract;
import com.gogotalk.system.presenter.SearchNamePresenter;
import com.gogotalk.system.util.ScreenUtils;
import com.gogotalk.system.view.adapter.SearchNameAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchNameActivity extends BaseActivity<SearchNamePresenter> implements SearchNameContract.View {
    @BindView(R.id.rv_select_name)
    RecyclerView mRecyclerView;
    @BindView(R.id.et_search_name)
    EditText etSearchName;
    private SearchNameAdapter mAdapter;
    private GridLayoutManager manager;
    private int sex;
    private List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.searchEnglishNameListData(sex, "");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_name;
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
        etSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(charSequence)) {
                    mPresenter.searchEnglishNameListData(sex, "");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etSearchName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    mPresenter.searchEnglishNameListData(sex, textView.getText().toString());
                }
                return false;
            }
        });
        manager = new GridLayoutManager(this, 5);
        mAdapter = new SearchNameAdapter(list);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dip2px(SearchNameActivity.this, 11)));
    }

    @Override
    public void updateRecelyerViewData(List<String> beans) {
        list.clear();
        if (beans == null) {
            mAdapter.notifyDataSetChanged();
            return;
        }
        if (beans.size() == 0) {
            mAdapter.notifyDataSetChanged();
            return;
        }
        list.addAll(beans);
        mAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
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
}
