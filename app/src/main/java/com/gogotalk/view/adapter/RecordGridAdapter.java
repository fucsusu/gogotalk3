package com.gogotalk.view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gogotalk.R;
import com.gogotalk.model.entity.RecordBean;
import com.gogotalk.model.util.Constant;
import com.gogotalk.view.activity.ClassDetailActivity;

import java.util.List;

public class RecordGridAdapter extends BaseAdapter {
    private List<RecordBean> list;
    private Activity context;
    private LayoutInflater minflater;

    public RecordGridAdapter(Activity context, List<RecordBean> list) {
        this.context = context;
        this.list = list;
        this.minflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        TextView mLl, mUserName, mTime, mSize, mExercises;
        ImageView mImg;
        LinearLayout mLayout;
        int state;
        String AfterFilePath;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = minflater.inflate(R.layout.record_grid_item, parent, false);
            mHolder.mLl = convertView.findViewById(R.id.id_mLl_RecordItem);
            mHolder.mUserName = convertView.findViewById(R.id.id_mUserName_RecordItem);
            mHolder.mTime = convertView.findViewById(R.id.id_mTime_RecordItem);
            mHolder.mSize = convertView.findViewById(R.id.id_mSize_RecordItem);
            mHolder.mImg = convertView.findViewById(R.id.id_mImg_RecordItem);
            mHolder.mLayout = convertView.findViewById(R.id.id_mLayout_RecordItem);
            mHolder.mExercises = convertView.findViewById(R.id.id_mExercises_RecordItem);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        mHolder.mLl.setText(list.get(position).getLevelName());
        mHolder.mUserName.setText(list.get(position).getChapterEnglishName());
        mHolder.mSize.setText(list.get(position).getGiftCount() + "");
        mHolder.mTime.setText(list.get(position).getLessonTime());
        mHolder.AfterFilePath = list.get(position).getAfterFilePath();//课后链接
        final int DetailRecordID = list.get(position).getDetailRecordID();//约课记录ID
        final String BeforeFilePath = list.get(position).getBeforeFilePath();//课前链接
        String url = list.get(position).getChapterCoverImgUrl();
        Glide.with(context).load(url).placeholder(R.mipmap.ic_main_list_item_header_default)
                .diskCacheStrategy(DiskCacheStrategy.NONE).into(mHolder.mImg);
        mHolder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(context, ClassDetailActivity.class);
                mIntent.putExtra(Constant.INTENT_DATA_KEY_RECORD_ID, DetailRecordID);
                context.startActivity(mIntent);
            }
        });
        final ViewHolder finalMHolder = mHolder;
        mHolder.mExercises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//课后练习
//                String afterFilePath = finalMHolder.AfterFilePath;
//                if (TextUtils.isEmpty(afterFilePath)) {
//                    Toast.makeText(context, "未存在课后练习！", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                Intent mIntent = new Intent(context, InteractiveGamesActivity.class);
//                mIntent.putExtra("AfterFilePath", afterFilePath);
//                context.startActivity(mIntent);
            }
        });
        return convertView;
    }
}
