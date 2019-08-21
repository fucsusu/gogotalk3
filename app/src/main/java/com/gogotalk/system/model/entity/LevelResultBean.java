package com.gogotalk.system.model.entity;

import java.util.List;

public class LevelResultBean {

    /**
     * Level : 1.0
     * LevelName : L1
     * Title : 根据宝贝当前情况，建议学习起点为L1
     * LevelList : [{"LeveName":"L1","ImgUrl":"http://coursefiles.oss-cn-beijing.aliyuncs.com/Hgogotalk/CommenFiles/LevelSuccessImg/L1.jpg"},{"LeveName":"L2","ImgUrl":"http://coursefiles.oss-cn-beijing.aliyuncs.com/Hgogotalk/CommenFiles/LevelSuccessImg/L2.jpg"},{"LeveName":"L3","ImgUrl":"http://coursefiles.oss-cn-beijing.aliyuncs.com/Hgogotalk/CommenFiles/LevelSuccessImg/L3.jpg"}]
     */

    private double Level;
    private String LevelName;
    private String Title;

    public double getLevel() {
        return Level;
    }

    public void setLevel(double Level) {
        this.Level = Level;
    }

    public String getLevelName() {
        return LevelName;
    }

    public void setLevelName(String LevelName) {
        this.LevelName = LevelName;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

}
