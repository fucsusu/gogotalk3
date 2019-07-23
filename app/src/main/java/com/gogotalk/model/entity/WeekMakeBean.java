package com.gogotalk.model.entity;

import java.util.ArrayList;
import java.util.List;

public class WeekMakeBean {

    private String WeekName;
    private String FullDate;
    private String Date;
    private int IsDefaultDateActive;
    private boolean checked;
    private List<TimeMakeBean> TimeList = new ArrayList<>();

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getWeekName() {
        return WeekName;
    }

    public void setWeekName(String WeekName) {
        this.WeekName = WeekName;
    }

    public String getFullDate() {
        return FullDate;
    }

    public void setFullDate(String FullDate) {
        this.FullDate = FullDate;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public int getIsDefaultDateActive() {
        return IsDefaultDateActive;
    }

    public void setIsDefaultDateActive(int IsDefaultDateActive) {
        this.IsDefaultDateActive = IsDefaultDateActive;
    }

    public List<TimeMakeBean> getTimeList() {
        return TimeList;
    }

    public void setTimeList(List<TimeMakeBean> TimeList) {
        this.TimeList = TimeList;
    }
}
