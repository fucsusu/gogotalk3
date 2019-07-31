package com.gogotalk.system.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gogotalk.system.R;
import com.gogotalk.system.model.entity.EnglishNameListBean;
import com.gogotalk.system.model.util.Constant;
import com.gogotalk.system.view.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class SelectNameAdapter extends SimpleSectionedAdapter<SelectNameAdapter.SelectNameHodler> {
    private Context context;
    private EnglishNameListBean mNameListBean;
    List<String> list=new ArrayList<>();
    public SelectNameAdapter(EnglishNameListBean nameListBean){
        if(nameListBean==null)return;
        this.mNameListBean = nameListBean;
        list.clear();
        list.addAll(mNameListBean.getRecommendData());
        for(EnglishNameListBean.GroupDataBean groupDataBean:mNameListBean.getGroupData()){
            list.addAll(groupDataBean.getEnglishNames());
        }
    }
    @Override
    protected String getSectionHeaderTitle(int section) {
//        if(section==0){
//            if(mNameListBean.getRecommendData().size()>0){
//                return "推荐";
//            }else{
//                return null;
//            }
//        }
        return mNameListBean.getGroupData().get(section).getFirstWord();
    }

    /**
     * 获取分组数
     * @return
     */
    @Override
    protected int getSectionCount() {
        return mNameListBean.getGroupData().size();
    }

    /**
     * 获取指定组的个数
     * @param section
     * @return
     */
    @Override
    protected int getItemCountForSection(int section) {
//        if(section==0){
//            return mNameListBean.getRecommendData().size();
//        }
        return mNameListBean.getGroupData().get(section).getEnglishNames().size();
    }

    @Override
    protected SelectNameHodler onCreateItemViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.select_name_list_item_name, parent, false);
        return new SelectNameHodler(itemView);
    }

    @Override
    protected void onBindItemViewHolder(final SelectNameHodler holder, int section, final int position) {
//        if(section==0){
//            holder.tv_name.setText(mNameListBean.getRecommendData().get(position));
//        }else{
            holder.tv_name.setText(mNameListBean.getGroupData().get(section).getEnglishNames().get(position));
//        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra(Constant.INTENT_DATA_KEY_NAME,holder.tv_name.getText());
                intent.putExtra(Constant.INTENT_DATA_KEY_DIRECTION,Constant.DIRECTION_SELECTNAME_TO_HOME);
                context.startActivity(intent);
            }
        });

    }

    class SelectNameHodler extends RecyclerView.ViewHolder {
        TextView tv_name;

        public SelectNameHodler(View itemView) {
            super(itemView);
            tv_name =  itemView.findViewById(R.id.tvName);
        }
    }

    public int getPositionForSection(int letter) {
        int i1 = getItemCount() - getSectionCount();
        for (int i = 0; i < i1; i++) {
            String sortStr = list.get(i);
            if(TextUtils.isEmpty(sortStr))continue;
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == letter) {
                return i;
            }
        }
        return -1;
    }

}