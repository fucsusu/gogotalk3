package com.gogotalk.view.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gogotalk.R;
import com.gogotalk.model.entity.WeekMakeBean;

import java.util.List;

public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.ViewHolder> {
    private List<WeekMakeBean> mDataList;
    private OnItemClickListener mClickListener;

    public WeekAdapter(List<WeekMakeBean> listDatas) {
        mDataList = listDatas;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mWeek, mDate;
        LinearLayout mLayout;
        OnItemClickListener mListener;// 声明自定义的接口
        String FullDate;

        public ViewHolder(View view, OnItemClickListener listener) {
            super(view);
            mWeek = view.findViewById(R.id.id_mWeek_MItem);
            mDate = view.findViewById(R.id.id_mDate_MItem);
            mLayout = view.findViewById(R.id.id_mLayout_MItem);
            mListener = listener;
            itemView.setOnClickListener(this);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_yuyue_week_list_item,
                parent, false);
        ViewHolder holder = new ViewHolder(view, mClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mWeek.setText(mDataList.get(position).getWeekName());
        holder.mDate.setText(mDataList.get(position).getDate());
        holder.FullDate = mDataList.get(position).getFullDate();
        if (mDataList.get(position).isChecked() == true) {
            holder.mLayout.setBackgroundResource(R.drawable.dot_timesize);
            holder.mWeek.setTextColor(Color.argb(255, 255, 255, 255));
            holder.mDate.setTextColor(Color.argb(255, 255, 255, 255));
        } else {
            holder.mLayout.setBackgroundResource(R.drawable.dot_timesizes);
            holder.mWeek.setTextColor(Color.argb(255, 102, 102, 102));
            holder.mDate.setTextColor(Color.argb(255, 102, 102, 102));
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
