package com.gogotalk.view.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gogotalk.R;
import com.gogotalk.model.entity.CoursesBean;
import com.gogotalk.util.DateUtils;
import com.gogotalk.view.activity.VideoActivity;
import com.gogotalk.view.widget.CommonDialog;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C)
 * <p>
 * FileName: RecyclerListAdapter
 * <p>
 * Author: 赵小钧
 * <p>
 * Date: 2019\6\12 0012 18:37
 */
public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder> {
    private List<CoursesBean> mDataList;
    private Activity context;

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
//        if (holder.LessonStatus == 0) {
//            holder.contentsText.setText("未开始");
//        }
//        if (holder.LessonStatus == 1) {
//            holder.contentsText.setText("上课中");
//        }
//        if (holder.LessonStatus == 2) {
//            holder.contentsText.setText("即将开始");
//        }
//        if (holder.LessonStatus == 3) {
//            holder.contentsText.setText("已结束");
//        }
            holder.AttendLessonID = coursesBean.getAttendLessonID();
            holder.ChapterFilePath = coursesBean.getChapterFilePath();
            holder.mTitle.setText(coursesBean.getChapterEnglishName());
            holder.mTime.setText("上课时间 " + coursesBean.getLessonTimeApp());
            holder.BeforeFilePath = coursesBean.getBeforeFilePath();
            holder.AfterFilePath = coursesBean.getAfterFilePath();
            String url = coursesBean.getChapterCoverImgUrl();
            Glide.with(context).load(url).placeholder(R.mipmap.ic_main_list_item_header_default)
                    .diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.mImg);
            holder.mPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent(context, VideoActivity.class);
                    mIntent.putExtra("url", holder.BeforeFilePath);
                    context.startActivity(mIntent);
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
//                        if (HomePageActivity.mShowRequestPermission) {
//                            Coustant.TeacherName = holder.TeacherName;
//                            Log.e("TAG", "剩下：" + min + "分钟");
//                            Intent mIntent = new Intent(context, MyClassRoomActivity.class);
//                            mIntent.putExtra("AttendLessonID", holder.AttendLessonID);
//                            mIntent.putExtra("ChapterFilePath", holder.ChapterFilePath);
//                            mIntent.putExtra("LessonTime", endDateTime);
//                            context.startActivity(mIntent);
//                        }else {
//                            Toast.makeText(context, "部分功能未授权，请授权后再试！", Toast.LENGTH_SHORT).show();
//                        }
                    }
                });
            } else {
                holder.mEnterClassroom.setBackgroundResource(R.mipmap.bg_main_list_item_btn_disabled);
                holder.mEnterClassroom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "课前10分钟才可以进入教室", Toast.LENGTH_LONG).show();
                    }
                });
            }

            holder.mTime1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (XClickUtil.isFastDoubleClick(v, 1000)) {
//                        return;
//                    }
//                    LayoutInflater inflater = LayoutInflater.from(context);
//                    View view = inflater.inflate(R.layout.dialog_cancellation, null, false);// 得到加载view
//                    LinearLayout layout = view.findViewById(R.id.id_mLayout_Cancellation);// 加载布局
//                    final Dialog loadingDialog = new Dialog(context, R.style.MyDialogStyle);// 创建自定义样式dialog
//                    loadingDialog.setCancelable(true); // 是否可以按“返回键”消失
//                    loadingDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
//                    loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
//                            LinearLayout.LayoutParams.MATCH_PARENT,
//                            LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
//                    CommonDialog.Builder builder = new CommonDialog.Builder(context);
//                    builder.setMessage(context.getResources().getString(R.string.ask_cancel_class));
//                    final CommonDialog twoButtonDialog = builder.createTwoButtonDialog();
//                    builder.setPositiveButton("确定", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
////                        final Dialog dialog = LoadingDialogUtils.createLoadingDialog(context, "请稍等...");
//                            Map<String, Object> params = new HashMap<>();
//                            params.put("DemandId", holder.DetailRecordID);
//                            XutilsHttp.getInstance().get(CountUri.CANCELLATION_APPOINTMENT, params, new XutilsHttp.XCallBack() {
//                                @Override
//                                public void onResponse(String result) {
//                                    Log.e("TAG", result);
//                                    try {
//                                        JSONObject object = new JSONObject(result);
//                                        int result1 = object.getInt("result");
//                                        String msg = object.getString("msg");
//                                        if (result1 == 1) {
//                                            Toast.makeText(context, "" + msg, Toast.LENGTH_LONG).show();
//                                            context.startActivity(new Intent(context, HomePageActivity.class));
//                                            twoButtonDialog.dismiss();
//                                        } else if (result1 == 0) {
//                                            Toast.makeText(context, "" + msg, Toast.LENGTH_LONG).show();
//                                        }
//                                        if (result1 == 1002) {
//                                            context.startActivity(new Intent(context, MainActivity.class));
//                                            twoButtonDialog.dismiss();
//                                        }
////                                    LoadingDialogUtils.closeDialog(dialog);
////                                    loadingDialog.dismiss();
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            });
                        }
                    });
//                    builder.setNegativeButton("取消", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
////                        loadingDialog.dismiss();
//                            twoButtonDialog.dismiss();
//                        }
//                    });
//                    twoButtonDialog.show();
//                }
//            });
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
}
