package com.gogotalk.system.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C)
 * <p>
 * FileName: GoGoBean
 * <p>
 * Author: 赵小钧
 * <p>
 * Date: 2019\6\13 0013 14:36
 */
public class GoGoBean {

    private boolean isChecked = false;
    private int UnitID;//--单元ID
    private String UnitName;//--单元名称
    private String UnitEnglishName;//--单元英文名
    private List<GoItemBean> ChapterData = new ArrayList<>();//--单元对应的教材数据集合

    public GoGoBean(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public boolean getChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getUnitID() {
        return UnitID;
    }

    public void setUnitID(int unitID) {
        UnitID = unitID;
    }

    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName(String unitName) {
        UnitName = unitName;
    }

    public String getUnitEnglishName() {
        return UnitEnglishName;
    }

    public void setUnitEnglishName(String unitEnglishName) {
        UnitEnglishName = unitEnglishName;
    }

    public List<GoItemBean> getChapterData() {
        return ChapterData;
    }

    public void setChapterData(List<GoItemBean> chapterData) {
        ChapterData = chapterData;
    }
}
