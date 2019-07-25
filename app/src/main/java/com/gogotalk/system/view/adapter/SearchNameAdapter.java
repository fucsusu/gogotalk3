package com.gogotalk.system.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gogotalk.system.R;
import com.gogotalk.system.model.util.Constant;
import com.gogotalk.system.view.activity.MainActivity;

import java.util.List;

public class SearchNameAdapter extends RecyclerView.Adapter<SearchNameAdapter.SearchNameHodler> {
    private Context context;
    List<String> list;
    public SearchNameAdapter(List<String> list){
        this.list = list;
    }


    @NonNull
    @Override
    public SearchNameHodler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.select_name_list_item_name, viewGroup, false);
        return new SearchNameHodler(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchNameHodler searchNameHodler, int i) {
        searchNameHodler.tv_name.setText(list.get(i));
        searchNameHodler.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra(Constant.INTENT_DATA_KEY_NAME,searchNameHodler.tv_name.getText());
                intent.putExtra(Constant.INTENT_DATA_KEY_DIRECTION,Constant.DIRECTION_SELECTNAME_TO_HOME);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class SearchNameHodler extends RecyclerView.ViewHolder {
        TextView tv_name;

        public SearchNameHodler(View itemView) {
            super(itemView);
            tv_name =  itemView.findViewById(R.id.tvName);
        }
    }


}