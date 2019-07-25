package com.gogotalk.system.view.adapter;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SelectNameHeaderViewHolder extends RecyclerView.ViewHolder {
    TextView tv_title;
    public SelectNameHeaderViewHolder(@NonNull View itemView, int resId) {
        super(itemView);
         tv_title = itemView.findViewById(resId);
    }
    public void render(String title){
        tv_title.setText(title);
    }
}
