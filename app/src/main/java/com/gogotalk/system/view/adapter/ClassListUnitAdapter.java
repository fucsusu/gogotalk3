package com.gogotalk.system.view.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gogotalk.system.R;
import com.gogotalk.system.model.entity.GoGoBean;
import com.gogotalk.system.model.entity.GoItemBean;

import java.util.List;

public class ClassListUnitAdapter extends RecyclerView.Adapter<ClassListUnitAdapter.ViewHolder> {
    private List<GoGoBean> list;
    private OnItemClickListener mClickListener;

    public ClassListUnitAdapter(List<GoGoBean> list) {
        this.list = list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;
        LinearLayout mBtn;
        OnItemClickListener mListener;// 声明自定义的接口
        List<GoItemBean> mList;

        public ViewHolder(View view, OnItemClickListener listener) {
            super(view);
            textView = view.findViewById(R.id.id_mName_RecyItem);
            mBtn = view.findViewById(R.id.id_mBtn_RecyItem);
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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.class_list_unit_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view, mClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.textView.setText(list.get(i).getUnitEnglishName());
        holder.mList = list.get(i).getChapterData();
        if (list.get(i).getChecked() == true) {
            holder.mBtn.setBackgroundResource(R.mipmap.ic_class_list_unit_item_seleceted);
            holder.textView.setTextColor(Color.argb(255, 255, 96, 122));
        } else {
            holder.mBtn.setBackgroundResource(R.mipmap.ic_class_list_unit_item);
            holder.textView.setTextColor(Color.argb(255, 255, 255, 255));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mClickListener = listener;
    }
}
