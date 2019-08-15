package com.gogotalk.system.view.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gogotalk.system.R;
import com.gogotalk.system.model.entity.TimeMakeBean;
import com.gogotalk.system.model.entity.WeekMakeBean;
import com.gogotalk.system.view.adapter.TimeAdapter;
import com.gogotalk.system.view.adapter.WeekAdapter;

import java.util.ArrayList;
import java.util.List;

public class YuYueDialog extends ABBaseDialog {
    public interface IyuYuClickListener {
        void yuYuClick(String date, String time);
    }

    public YuYueDialog(Context context) {
        super(context);
    }

    public YuYueDialog(Context context, int theme) {
        super(context, theme);
    }

    private static String currentDate;
    private static String currentTime;

    public static class Builder {
        private View view;
        private YuYueDialog dialog;
        private ImageView btnClose;
        private Button btnYuYue;
        private LinearLayout layout_shangwu;
        private LinearLayout layout_xiawu;
        private LinearLayout layout_wanshang;
        private RecyclerView rv_week;
        private RecyclerView rv_shangwu;
        private RecyclerView rv_xiawu;
        private RecyclerView rv_wanshang;
        private TextView tv_shangwu;
        private TextView tv_xiawu;
        private TextView tv_wanshang;
        private List<WeekMakeBean> weekBeans = new ArrayList<>();
        private List<TimeMakeBean> shangwuBeans = new ArrayList<>();
        private List<TimeMakeBean> xiawuBeans = new ArrayList<>();
        private List<TimeMakeBean> wanshangBeans = new ArrayList<>();
        private Context mContext;
        private WeekAdapter weekAdapter;
        private TimeAdapter shangwuAdapter;
        private TimeAdapter xiawuAdapter;
        private TimeAdapter wanshangAdapter;
        private IyuYuClickListener iyuYuClickListener;

        public void setIyuYuClickListener(IyuYuClickListener iyuYuClickListener) {
            this.iyuYuClickListener = iyuYuClickListener;
        }

        public Builder(Context context) {
            mContext = context;
            dialog = new YuYueDialog(context, R.style.CustemDialog);
            initView();
            dialog.addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }


        public List<WeekMakeBean> getWeekBeans() {
            return weekBeans;
        }

        public Builder setWeekBeans(List<WeekMakeBean> weekBeans) {
            if (weekBeans == null) return this;
            if (weekBeans.size() == 0) return this;
            if (weekBeans.get(0).getTimeList() == null) return this;
            if (weekBeans.get(0).getTimeList().size() == 0) return this;
            this.weekBeans.clear();
            weekBeans.get(0).setChecked(true);
            currentDate = weekBeans.get(0).getFullDate();
            this.weekBeans.addAll(weekBeans);
            weekAdapter.notifyDataSetChanged();
            fillTimeData(weekBeans.get(0).getTimeList());
            return this;
        }

        private void fillTimeData(List<TimeMakeBean> timeMakeBeans) {
            this.shangwuBeans.clear();
            this.xiawuBeans.clear();
            this.wanshangBeans.clear();
            for (TimeMakeBean timeMakeBean : timeMakeBeans) {
                if (timeMakeBean.getTimePeriod() == 1) {
                    this.shangwuBeans.add(timeMakeBean);
                } else if (timeMakeBean.getTimePeriod() == 2) {
                    this.xiawuBeans.add(timeMakeBean);
                } else {
                    this.wanshangBeans.add(timeMakeBean);
                }
            }
            showLayout();
            shangwuAdapter.notifyDataSetChanged();
            xiawuAdapter.notifyDataSetChanged();
            wanshangAdapter.notifyDataSetChanged();
        }

        /**
         * 根据上午、下午、晚上时间段的数据数量判断是否显示布局
         */
        private void showLayout() {
            if (this.shangwuBeans.size() > 0) {
                layout_shangwu.setVisibility(View.VISIBLE);
            } else {
                layout_shangwu.setVisibility(View.GONE);
            }
            if (this.xiawuBeans.size() > 0) {
                layout_xiawu.setVisibility(View.VISIBLE);
            } else {
                layout_xiawu.setVisibility(View.GONE);
            }
            if (this.wanshangBeans.size() > 0) {
                layout_wanshang.setVisibility(View.VISIBLE);
            } else {
                layout_wanshang.setVisibility(View.GONE);
            }
        }

        /**
         * 初始化view
         */
        private void initView() {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dialog_order_class, null);
            btnClose = view.findViewById(R.id.id_mGuanbi_Make);
            btnYuYue = view.findViewById(R.id.id_mYy_Make);
            layout_shangwu = view.findViewById(R.id.id_mL1);
            layout_xiawu = view.findViewById(R.id.id_mL2);
            layout_wanshang = view.findViewById(R.id.id_mL3);
            rv_week = view.findViewById(R.id.id_mRecycler_Make);
            rv_shangwu = view.findViewById(R.id.id_mR1);
            rv_xiawu = view.findViewById(R.id.id_mR2);
            rv_wanshang = view.findViewById(R.id.id_mR3);
            tv_shangwu = view.findViewById(R.id.id_mTest2_Make);//上午
            tv_xiawu = view.findViewById(R.id.id_mTest_Make);//下午
            tv_wanshang = view.findViewById(R.id.id_mTest1_Make);//晚上
            tv_shangwu.setText("上" + "\n" + "午");
            tv_xiawu.setText("下" + "\n" + "午");
            tv_wanshang.setText("晚" + "\n" + "上");
            btnYuYue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (iyuYuClickListener != null) {
                        iyuYuClickListener.yuYuClick(currentDate, currentTime);
                    }
                }
            });
            initRecyclerView();
        }

        /**
         * 初始化周、上午、下午、晚上 RecyclerView
         */
        private void initRecyclerView() {
            LinearLayoutManager weekLayoutManager = new LinearLayoutManager(mContext);
            weekLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            rv_week.setLayoutManager(weekLayoutManager);
            weekAdapter = new WeekAdapter(weekBeans);
            rv_week.setAdapter(weekAdapter);

            MyGridLayoutManager shangwuGridLayoutManager = new MyGridLayoutManager(mContext, 6);
            if (shangwuBeans.size() > 12) {
                shangwuGridLayoutManager.setCanScroll(true);
            }
            rv_shangwu.setLayoutManager(shangwuGridLayoutManager);
            shangwuAdapter = new TimeAdapter(shangwuBeans);
            rv_shangwu.setAdapter(shangwuAdapter);

            MyGridLayoutManager xiawuGridLayoutManager = new MyGridLayoutManager(mContext, 6);
            if (xiawuBeans.size() > 12) {
                xiawuGridLayoutManager.setCanScroll(true);
            }
            rv_xiawu.setLayoutManager(xiawuGridLayoutManager);
            xiawuAdapter = new TimeAdapter(xiawuBeans);
            rv_xiawu.setAdapter(xiawuAdapter);

            MyGridLayoutManager wanshangGridLayoutManager = new MyGridLayoutManager(mContext, 6);
            if (wanshangBeans.size() > 12) {
                wanshangGridLayoutManager.setCanScroll(true);
            }
            rv_wanshang.setLayoutManager(wanshangGridLayoutManager);
            wanshangAdapter = new TimeAdapter(wanshangBeans);
            rv_wanshang.setAdapter(wanshangAdapter);

            weekAdapter.setOnItemClickListener(new WeekAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int postion) {
                    weekListItemClick(view, postion);
                }
            });
            shangwuAdapter.setOnItemClickListener(new TimeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int postion) {
                    shangwuListItemClick(view, postion);
                }
            });
            xiawuAdapter.setOnItemClickListener(new TimeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int postion) {
                    xiawuListItemClick(view, postion);
                }
            });
            wanshangAdapter.setOnItemClickListener(new TimeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int postion) {
                    wanshangListItemClick(view, postion);
                }
            });
        }

        private void weekListItemClick(View view, int postion) {
            for (WeekMakeBean weekMakeBean : this.weekBeans) {
                weekMakeBean.setChecked(false);
            }
            this.weekBeans.get(postion).setChecked(true);
            currentDate = this.weekBeans.get(postion).getFullDate();
            weekAdapter.notifyDataSetChanged();
            for (TimeMakeBean timeMakeBean : this.weekBeans.get(postion).getTimeList()) {
                timeMakeBean.setChecked(false);
            }
            fillTimeData(this.weekBeans.get(postion).getTimeList());
            currentTime = "";
//            currentDate = "";
        }

        private void shangwuListItemClick(View view, int postion) {
            resetTimeState();
            shangwuBeans.get(postion).setChecked(true);
            int timeIsSelect = shangwuBeans.get(postion).getTimeIsSelect();
            if (timeIsSelect == 1) {
                currentTime = shangwuBeans.get(postion).getTime();
            }
            notifyTimeDataSetChanged();
        }

        private void xiawuListItemClick(View view, int postion) {
            resetTimeState();
            xiawuBeans.get(postion).setChecked(true);
            int timeIsSelect = xiawuBeans.get(postion).getTimeIsSelect();
            if (timeIsSelect == 1) {
                currentTime = xiawuBeans.get(postion).getTime();
            }
            notifyTimeDataSetChanged();
        }

        private void wanshangListItemClick(View view, int postion) {
            resetTimeState();
            wanshangBeans.get(postion).setChecked(true);
            int timeIsSelect = wanshangBeans.get(postion).getTimeIsSelect();
            if (timeIsSelect == 1) {
                currentTime = wanshangBeans.get(postion).getTime();
            }
            notifyTimeDataSetChanged();
        }

        private void resetTimeState() {
            for (TimeMakeBean timeMakeBean : this.shangwuBeans) {
                timeMakeBean.setChecked(false);
            }
            for (TimeMakeBean timeMakeBean : this.xiawuBeans) {
                timeMakeBean.setChecked(false);
            }
            for (TimeMakeBean timeMakeBean : this.wanshangBeans) {
                timeMakeBean.setChecked(false);
            }
        }

        public void notifyTimeDataSetChanged() {
            shangwuAdapter.notifyDataSetChanged();
            xiawuAdapter.notifyDataSetChanged();
            wanshangAdapter.notifyDataSetChanged();
        }

        public YuYueDialog create() {
            dialog.setContentView(view);
            dialog.setCancelable(true);     //用户可以点击手机Back键取消对话框显示
            dialog.setCanceledOnTouchOutside(false);        //用户不能通过点击对话框之外的地方取消对话框显示
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            return dialog;
        }

    }

    @Override
    public void show() {
        super.show();
        currentTime = "";
    }
}

