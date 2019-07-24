package com.gogotalk.system.model.entity;

public class TimeMakeBean {
    private String Time;
    private int IsDefaultTimeActive;
    private int TimePeriod;
    private int TimeIsSelect;
    private boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String Time) {
        this.Time = Time;
    }

    public int getIsDefaultTimeActive() {
        return IsDefaultTimeActive;
    }

    public void setIsDefaultTimeActive(int IsDefaultTimeActive) {
        this.IsDefaultTimeActive = IsDefaultTimeActive;
    }

    public int getTimePeriod() {
        return TimePeriod;
    }

    public void setTimePeriod(int TimePeriod) {
        this.TimePeriod = TimePeriod;
    }

    public int getTimeIsSelect() {
        return TimeIsSelect;
    }

    public void setTimeIsSelect(int TimeIsSelect) {
        this.TimeIsSelect = TimeIsSelect;
    }
}
