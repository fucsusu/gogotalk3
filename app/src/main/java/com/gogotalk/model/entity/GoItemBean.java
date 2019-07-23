package com.gogotalk.model.entity;

/**
 * Copyright (C)
 * <p>
 * FileName: GoItemBean
 * <p>
 * Author: 赵小钧
 * <p>
 * Date: 2019\6\18 0018 11:24
 */
public class GoItemBean {
    private String BeforeFilePath;//课前练习
    private int ChapterID;// --教材ID
    private int BookID;// --书本ID
    private String ChapterName;//--教材名称
    private String ChapterEnglishtName;//--教材英文名
    private String ChapterImagePath;//--教材封面
    private String ChapterFilePath;//--对应的H5课件地址
    private String EnIntroduction;// --中文描述
    private String ChIntroduction;//--英文描述
    private int UnitID;//--对应的单元ID
    private int IsLock;//--是否锁定|0=未锁定,1=锁定
    private int ChapterStatus;//--状态|0:未约课 1:已预约 2:已完成
    private String LessonTime;//--课程时间  (状态>0时有时间)
    private String AttendLessonID;//--课程ID（目前作为房间推流ID使用）
    private int LessonStatus;//--状态 0 是未开始  1 上课中 2 即将开始 3 已结
    private String SystemTime;//-- 系统时间，跟上课时间比较，判断什么时候进入教


    public String getChapterFilePath() {
        return ChapterFilePath;
    }

    public void setChapterFilePath(String chapterFilePath) {
        ChapterFilePath = chapterFilePath;
    }

    public String getAttendLessonID() {
        return AttendLessonID;
    }

    public void setAttendLessonID(String attendLessonID) {
        AttendLessonID = attendLessonID;
    }

    public int getLessonStatus() {
        return LessonStatus;
    }

    public void setLessonStatus(int lessonStatus) {
        LessonStatus = lessonStatus;
    }

    public String getSystemTime() {
        return SystemTime;
    }

    public void setSystemTime(String systemTime) {
        SystemTime = systemTime;
    }

    public int getChapterID() {
        return ChapterID;
    }

    public String getBeforeFilePath() {
        return BeforeFilePath;
    }

    public void setBeforeFilePath(String beforeFilePath) {
        BeforeFilePath = beforeFilePath;
    }

    public void setChapterID(int chapterID) {
        ChapterID = chapterID;
    }

    public int getBookID() {
        return BookID;
    }

    public void setBookID(int bookID) {
        BookID = bookID;
    }

    public String getChapterName() {
        return ChapterName;
    }

    public void setChapterName(String chapterName) {
        ChapterName = chapterName;
    }

    public String getChapterEnglishtName() {
        return ChapterEnglishtName;
    }

    public void setChapterEnglishtName(String chapterEnglishtName) {
        ChapterEnglishtName = chapterEnglishtName;
    }

    public String getChapterImagePath() {
        return ChapterImagePath;
    }

    public void setChapterImagePath(String chapterImagePath) {
        ChapterImagePath = chapterImagePath;
    }

    public String getEnIntroduction() {
        return EnIntroduction;
    }

    public void setEnIntroduction(String enIntroduction) {
        EnIntroduction = enIntroduction;
    }

    public String getChIntroduction() {
        return ChIntroduction;
    }

    public void setChIntroduction(String chIntroduction) {
        ChIntroduction = chIntroduction;
    }

    public int getUnitID() {
        return UnitID;
    }

    public void setUnitID(int unitID) {
        UnitID = unitID;
    }

    public int getIsLock() {
        return IsLock;
    }

    public void setIsLock(int isLock) {
        IsLock = isLock;
    }

    public int getChapterStatus() {
        return ChapterStatus;
    }

    public void setChapterStatus(int chapterStatus) {
        ChapterStatus = chapterStatus;
    }

    public String getLessonTime() {
        return LessonTime;
    }

    public void setLessonTime(String lessonTime) {
        LessonTime = lessonTime;
    }

    @Override
    public String toString() {
        return "{\"ChapterID\":" + ChapterID + ",\"BookID\":" + BookID + ",\"ChapterName\":" + "\"" + ChapterName + "\""
                + ",\"AttendLessonID\":" + "\"" + AttendLessonID + "\"" + ",\"ChapterFilePath\":" + "\"" + ChapterFilePath + "\"" + ",\"ChapterEnglishtName\":" + "\"" + ChapterEnglishtName + "\"" + ",\"ChapterImagePath\":" + "\"" + ChapterImagePath + "\""
                + ",\"SystemTime\":" + "\"" + SystemTime + "\"" + ",\"EnIntroduction\":" + "\"" + EnIntroduction + "\"" + ",\"ChIntroduction\":" + "\"" + ChIntroduction + "\""
                + ",\"LessonStatus\":" + LessonStatus + ",\"UnitID\":" + UnitID + ",\"IsLock\":" + IsLock + ",\"ChapterStatus\":" + ChapterStatus + ",\"LessonTime\":" + "\"" + LessonTime + "\"" + ",\"BeforeFilePath\":" + "\"" + BeforeFilePath + "\"" + "}";
    }
}
