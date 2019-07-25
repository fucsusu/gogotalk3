package com.gogotalk.system.view.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gogotalk.system.R;
import com.gogotalk.system.model.entity.CoursesBean;
import com.gogotalk.system.util.AppUtils;
import com.gogotalk.system.util.DateUtils;
import com.gogotalk.system.util.XClickUtil;

import java.util.List;


public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder> {
    private List<CoursesBean> mDataList;
    private Activity context;
    private IBtnClickLisener btnClickLisener;

    public void setBtnClickLisener(IBtnClickLisener btnClickLisener) {
        this.btnClickLisener = btnClickLisener;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout mLayout;
        TextView contentsText, mTitle, mTime, mTime1;
        Button mPreview, mEnterClassroom;
        ImageView mImg;
        String BeforeFilePath;
        String AfterFilePath;
        int AttendLessonID;
        int LessonStatus;
        String ChapterFilePath;
        int DetailRecordID;
        String LessonTime;
        String TeacherName;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType == 1) {
                contentsText = itemView.findViewById(R.id.id_mTest_HomeItem);
                mTitle = itemView.findViewById(R.id.id_mTitleName_HomeItem);
                mTime = itemView.findViewById(R.id.id_mTime_HomeItem);
                mTime1 = itemView.findViewById(R.id.id_mTime1_HomeItem);
                mImg = itemView.findViewById(R.id.id_mImg_XX);
                mLayout = itemView.findViewById(R.id.id_mImg_BJ);
                mPreview = itemView.findViewById(R.id.id_mPreview_BtnItem);
                mEnterClassroom = itemView.findViewById(R.id.id_mEnterClassroom_BtnItem);
            }
        }
    }

    public MainRecyclerAdapter(Activity context, List<CoursesBean> listDatas) {
        this.mDataList = listDatas;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = new View(parent.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(20, 40));
            view.setBackgroundColor(Color.TRANSPARENT);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_list_item,
                    parent, false);
        }
        ViewHolder holder = new ViewHolder(view, viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (position > 0 && position < mDataList.size() + 1) {
            CoursesBean coursesBean = mDataList.get(position - 1);
            holder.LessonStatus = coursesBean.getLessonStatus();
            holder.DetailRecordID = coursesBean.getDetailRecordID();
            holder.LessonTime = coursesBean.getLessonTime();
            holder.contentsText.setText(coursesBean.getChapterName());
            holder.TeacherName = coursesBean.getTeacherName();
            holder.AttendLessonID = coursesBean.getAttendLessonID();
            holder.ChapterFilePath = coursesBean.getChapterFilePath();
            holder.mTitle.setText(coursesBean.getChapterEnglishName());
            holder.mTime.setText("上课时间 " + coursesBean.getLessonTimeApp());
            holder.BeforeFilePath = coursesBean.getBeforeFilePath();
            holder.AfterFilePath = coursesBean.getAfterFilePath();
            String url = coursesBean.getChapterCoverImgUrl();
            AppUtils.bindImageToView(context
                    ,url,R.mipmap.ic_main_list_item_header_default
                    ,holder.mImg,null,9,137,77);
            holder.mPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (btnClickLisener != null) {
                        btnClickLisener.onBtnClassPreviewClick(holder.BeforeFilePath);
                    }
                }
            });
            final String endDateTime;
            if (holder.LessonTime.indexOf("今天") == -1) {//没有
                endDateTime = holder.LessonTime;
            } else {//有
                String str = holder.LessonTime.substring(2);
                String date = DateUtils.StringData();
                endDateTime = date + str;
            }
            String startDateTime = DateUtils.getCurrentTime();
            final long min = DateUtils.getTimeDifferences(startDateTime, endDateTime);
            if (min < 11) {
                holder.mEnterClassroom.setBackgroundResource(R.mipmap.bg_main_list_item_btn_go_room);
                holder.mEnterClassroom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (btnClickLisener != null) {
                            btnClickLisener.onBtnGoClassRoomClick(true, coursesBean);
                        }
                    }
                });
            } else {
                holder.mEnterClassroom.setBackgroundResource(R.mipmap.bg_main_list_item_btn_disabled);
                holder.mEnterClassroom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (btnClickLisener != null) {
                            btnClickLisener.onBtnGoClassRoomClick(false, coursesBean);
                        }
                    }
                });
            }

            holder.mTime1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (XClickUtil.isFastDoubleClick(v, 1000)) {
                        return;
                    }
                    if (btnClickLisener != null) {
                        btnClickLisener.onBtnCancelOrderClass(holder.DetailRecordID);
                    }
                }
            });
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == mDataList.size() + 1) {
            return 0;
        }
        return 1;
    }

    @Override
    public int getItemCount() {
        if (mDataList.size() > 0) {
            return mDataList.size() + 2;
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
        void onBtnGoClassRoomClick(boolean flag, CoursesBean coursesBean);

        /**
         * 取消预约课程事件
         */
        void onBtnCancelOrderClass(int demandId);
    }
}
