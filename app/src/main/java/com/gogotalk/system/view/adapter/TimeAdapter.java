package com.gogotalk.system.view.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gogotalk.system.R;
import com.gogotalk.system.model.entity.TimeMakeBean;

import java.util.List;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ViewHolder> {
    private List<TimeMakeBean> mDataList;
    private OnItemClickListener mClickListener;

    public TimeAdapter(List<TimeMakeBean> listDatas) {
        mDataList = listDatas;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView box;
        OnItemClickListener mListener;// 声明自定义的接口
        int TimeIsSelect;

        public ViewHolder(View view, OnItemClickListener listener) {
            super(view);
            box = view.findViewById(R.id.id_mCheckBox_MakesItem);
            mListener = listener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClick(v, getPosition());
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int postion);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_yuyue_time_list_item,
                parent, false);
        ViewHolder holder = new ViewHolder(view, mClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.box.setText(mDataList.get(position).getTime());
        holder.TimeIsSelect = mDataList.get(position).getTimeIsSelect();
        if (holder.TimeIsSelect == 0) {
            holder.box.setBackgroundResource(R.mipmap.bg_dialog_yuyue_btn_time);
//            holder.box.setTextColor(Color.argb(255, 153, 153, 153));
            holder.box.setTextColor(Color.parseColor("#ffd6d6d6"));
        } else {
            if (mDataList.get(position).isChecked() == true) {
                holder.box.setBackgroundResource(R.mipmap.bg_dialog_yuyue_btn_time_selected);
                holder.box.setTextColor(Color.argb(255, 255, 255, 255));
            } else {
                holder.box.setBackgroundResource(R.mipmap.bg_dialog_yuyue_btn_time);
//                holder.box.setTextColor(Color.argb(255, 102, 102, 102));
                holder.box.setTextColor(Color.parseColor("#ff666666"));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mClickListener = listener;
    }
}
