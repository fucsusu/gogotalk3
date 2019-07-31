package com.gogotalk.system.view.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

import com.gogotalk.system.R;

public abstract class SimpleSectionedAdapter<VH extends RecyclerView.ViewHolder> extends SectionedRecyclerViewAdapter<SelectNameHeaderViewHolder,
        VH, RecyclerView.ViewHolder> {

    /**
     * 是否显示每一个分组的底部
     * @param section
     * @return
     */
    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    @Override
    protected boolean hasHeaderInSection(int section) {
        return true;
    }

    /*
        创建头部header的ViewHolder
         */
    @Override
    protected SelectNameHeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = null;
        SelectNameHeaderViewHolder holder = null;
//        if(isSectionHeaderViewType01(viewType)){
//            view = inflater.inflate(getLayoutResource01(), parent, false);
//        }
        if(isSectionHeaderViewType02(viewType)){
            view = inflater.inflate(getLayoutResource02(), parent, false);
        }
        holder = new SelectNameHeaderViewHolder(view, getTitleTextID());
        return holder;
    }

    /*
    创建底部footer的ViewHolder
     */
    @Override
    protected RecyclerView.ViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(getFooterLayoutResource(), parent, false);
        FooterViewHolder holder = new FooterViewHolder(view);

        return holder;
    }

    private int getFooterLayoutResource() {
        return R.layout.rv_item_footer_01;
    }

    @Override
    protected void onBindSectionHeaderViewHolder(SelectNameHeaderViewHolder holder, int section) {
        String title = getSectionHeaderTitle(section);
//        if(section==0){
//            holder.isShow(false);
//        }else{
            holder.render(title);
//        }
    }

    @Override
    protected void onBindSectionFooterViewHolder(RecyclerView.ViewHolder holder, int section) {

    }

    /**
     * Provides a layout identifier for the header. Override it to change the appearance of the
     * header view.
     */
//    protected
//    @LayoutRes
//    int getLayoutResource01() {
//        return R.layout.rv_item_header_01;
//    }
    protected
    @LayoutRes
    int getLayoutResource02() {
        return R.layout.rv_item_header_02;
    }
    /**
     * Provides the identifier of the TextView to render the section header title. Override it if
     * you provide a custom layout for a header.
     */
    protected
    @IdRes
    int getTitleTextID() {
        return R.id.tv_header_title;
    }
    /**
     * Returns the title for a given section
     */
    protected abstract String getSectionHeaderTitle(int section);

    /**
     * 底部Footer的ViewHolder
     */
    class FooterViewHolder extends RecyclerView.ViewHolder{
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
