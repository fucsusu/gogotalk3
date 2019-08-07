package com.gogotalk.system.view.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gogotalk.system.R;
import com.gogotalk.system.model.entity.GoItemBean;
import com.gogotalk.system.util.AppUtils;
import com.gogotalk.system.util.DateUtils;
import com.gogotalk.system.util.ToastUtils;
import com.gogotalk.system.view.activity.ClassDetailActivity;

import java.util.List;

public class ClassListAdapter extends RecyclerView.Adapter<ClassListAdapter.ViewHolder> {
    private List<GoItemBean> list;
    private Activity context;
    IBtnClickLisener btnClickLisener;

    public void setBtnClickLisener(IBtnClickLisener btnClickLisener) {
        this.btnClickLisener = btnClickLisener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        String BeforeFilePath;
        TextView contentsText, mTime, mStey;
        RelativeLayout mLayout1;//背景
        ImageView mImg, mImg1;
        ImageView mImgs;
        LinearLayout mLayout2;
        FrameLayout mLayout3;
        ImageView ivSuoBg;
        Button mBtn, mPreview, mEnterClassroom;
        int stye;
        int ChapterStatus;
        int ChapterID;
        int BookID;
        String ChapterFilePath;
        String AttendLessonID;
        String LessonTime;
        int AttendLessonIDs;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType == 1) {
                //注意这里可能需要import com.example.lenovo.myrecyclerview.R; 才能使用R.id
                contentsText = itemView.findViewById(R.id.id_mTitleName_GoItem);
                mLayout1 = itemView.findViewById(R.id.id_mImg_BJItem);
                mImg = itemView.findViewById(R.id.id_mShou_GoItem);
                mLayout2 = itemView.findViewById(R.id.id_mLayouts_GoItem);
                mBtn = itemView.findViewById(R.id.id_mMakeAppointment);
                mImg1 = itemView.findViewById(R.id.id_mOkVer_GoItem);
                mImgs = itemView.findViewById(R.id.id_mImg_XXItem);
                mTime = itemView.findViewById(R.id.id_mTime_GoItem);
                mLayout3 = itemView.findViewById(R.id.id_mSuo_GoItem);
                ivSuoBg = itemView.findViewById(R.id.iv_suo_bg);
                mPreview = itemView.findViewById(R.id.id_mPreview_GoItem);
                mStey = itemView.findViewById(R.id.id_mStey_GoItem);
                mEnterClassroom = itemView.findViewById(R.id.id_mEnterClassroom_GoItem);
            }
        }
    }

    public ClassListAdapter(Activity context, List<GoItemBean> listDatas) {
        this.context = context;
        this.list = listDatas;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = new View(parent.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(40, 40));
            view.setBackgroundColor(Color.TRANSPARENT);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_list_item,
                    parent, false);
        }
        ViewHolder holder = new ViewHolder(view, viewType);
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == list.size() + 1) {
            return 0;
        }
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (position > 0 && position < list.size() + 1) {
            GoItemBean goItemBean1 = list.get(position - 1);
            holder.ChapterFilePath = goItemBean1.getChapterFilePath();
            holder.contentsText.setText(goItemBean1.getChapterEnglishtName());
            holder.stye = goItemBean1.getIsLock();
            holder.ChapterStatus = goItemBean1.getChapterStatus();
            holder.ChapterID = goItemBean1.getChapterID();
            holder.BookID = goItemBean1.getBookID();
            holder.mStey.setText(goItemBean1.getChapterName());
            holder.BeforeFilePath = goItemBean1.getBeforeFilePath();
            holder.LessonTime = goItemBean1.getLessonTime();
            String url = goItemBean1.getChapterImagePath();
            AppUtils.bindImageToView(context
                    , url, R.mipmap.ic_main_list_item_header_default
                    , holder.mImgs, DiskCacheStrategy.ALL,true,9);
            if (holder.stye == 0) {
                holder.mLayout3.setVisibility(View.GONE);//锁的背景Layout
                holder.mBtn.setBackgroundResource(R.mipmap.bg_main_list_item_btn_go_room);//立即预约按钮颜色
                if (holder.ChapterStatus == 2) {
                    holder.mLayout1.setBackgroundResource(R.mipmap.bg_class_list_item);//背景
                    holder.mImg.setImageResource(R.mipmap.ic_class_list_item);//小手
                    holder.mLayout2.setVisibility(View.GONE);//预习、教师Layout
                    holder.mBtn.setVisibility(View.VISIBLE);//立即预约按钮
                    holder.mImg1.setVisibility(View.VISIBLE);//已完成图片
                    holder.mTime.setVisibility(View.GONE);//左上角时间显示
                }
                if (holder.ChapterStatus == 1) {
                    holder.mLayout1.setBackgroundResource(R.mipmap.bg_class_list_item_selected);
                    holder.mImg.setImageResource(R.mipmap.ic_class_list_item_selected);
                    holder.mLayout2.setVisibility(View.VISIBLE);
                    holder.mBtn.setVisibility(View.GONE);
                    holder.mImg1.setVisibility(View.GONE);
                    holder.mTime.setVisibility(View.VISIBLE);
                    holder.mTime.setText("" + holder.LessonTime);
                    holder.AttendLessonID = goItemBean1.getAttendLessonID();
                    holder.AttendLessonIDs = Integer.parseInt(holder.AttendLessonID);
                }
                if (holder.ChapterStatus == 0) {
                    holder.mLayout1.setBackgroundResource(R.mipmap.bg_class_list_item);
                    holder.mImg.setImageResource(R.mipmap.ic_class_list_item);
                    holder.mLayout2.setVisibility(View.GONE);
                    holder.mBtn.setVisibility(View.VISIBLE);
                    holder.mImg1.setVisibility(View.GONE);
                    holder.mTime.setVisibility(View.GONE);
                }
                holder.mBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (btnClickLisener != null) {
                            btnClickLisener.onYuYueClick(holder.BookID, holder.ChapterID);
                        }
                    }
                });
            }
            if (holder.stye == 1) {
                holder.mLayout3.setVisibility(View.VISIBLE);
                holder.mBtn.setBackgroundResource(R.mipmap.bg_main_list_item_btn_disabled);
                holder.mLayout1.setBackgroundResource(R.mipmap.bg_class_list_item);//背景
                holder.mImg.setImageResource(R.mipmap.ic_class_list_item);//小手
                holder.mLayout2.setVisibility(View.GONE);//预习、教师Layout
                holder.mBtn.setVisibility(View.VISIBLE);//立即预约按钮
                holder.mImg1.setVisibility(View.GONE);//已完成图片
                holder.mTime.setVisibility(View.GONE);//左上角时间显示
                holder.mBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showLongToast(context, "学完前面的课时才可以解锁学习这节课哦~");
                    }
                });
            }

            holder.mPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//课前预习
                    if (btnClickLisener != null) {
                        btnClickLisener.onBtnClassPreviewClick(holder.BeforeFilePath);
                    }
                }
            });
            if (!TextUtils.isEmpty(holder.LessonTime)) {
                String mYear = DateUtils.StringDatas();
                final String LessonTimes = mYear + "-" + holder.LessonTime;
                String startDateTime = DateUtils.getCurrentTime();
                Log.e("TAG", "预约时间：" + LessonTimes);
                final long min = DateUtils.getTimeDifferences(startDateTime, LessonTimes);
                if (min < 11) {
                    holder.mEnterClassroom.setBackgroundResource(R.mipmap.bg_main_list_item_btn_go_room);
                    holder.mEnterClassroom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {//进入教师
                            if (btnClickLisener != null) {
                                btnClickLisener.onBtnGoClassRoomClick(true, goItemBean1);
                            }
                        }
                    });
                } else {
                    holder.mEnterClassroom.setBackgroundResource(R.mipmap.bg_main_list_item_btn_disabled);
                    holder.mEnterClassroom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {//进入教师
                            if (btnClickLisener != null) {
                                btnClickLisener.onBtnGoClassRoomClick(false, goItemBean1);
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if (list.size() > 0) {
            return list.size() + 2;
        }
        return 0;
    }

    public interface IBtnClickLisener {
        /**
         * 课程预览事件
         */
        void onBtnClassPreviewClick(String path);

        /**
         * 进入教室事件
         */
        void onBtnGoClassRoomClick(boolean flag, GoItemBean goItemBean);

        void onYuYueClick(int bookId, int chaptId);


    }
}
