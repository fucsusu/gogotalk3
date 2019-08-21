package com.gogotalk.system.model.entity;

public class LevelListBean {

    private String LeveName;
    private String PhoneImgUrl;
    private String Color;
    private double Level;

    public double getLevel() {
        return Level;
    }

    public void setLevel(double level) {
        Level = level;
    }

    public String getColor() {
        return "#"+Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public String getLeveName() {
        return LeveName;
    }

    public void setLeveName(String LeveName) {
        this.LeveName = LeveName;
    }

    public String getPhoneImgUrl() {
        return PhoneImgUrl;
    }

    public void setPhoneImgUrl(String phoneImgUrl) {
        PhoneImgUrl = phoneImgUrl;
    }
}
