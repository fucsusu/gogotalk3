package com.gogotalk.model.entity;

/**
 * Copyright (C)
 * <p>
 * FileName: RecordBean
 * <p>
 * Author: 赵小钧
 * <p>
 * Date: 2019\6\19 0019 10:03
 */
public class RecordBean {
    private int AttendLessonID;// --班级ID
    private int DetailRecordID;// --约课记录ID
    private String LessonTime;// --课程时间
    private String TeacherName;// --外交名称
    private int ChapterID;//  --教材ID
    private String ChapterName;// --教材中文名
    private String ChapterEnglishName;// --教材英文名
    private String LevelName;//  --级别名称
    private String ChapterCoverImgUrl;//  --封面图
    private String BeforeFilePath;//  --课前预习链接
    private String AfterFilePath;//  --课后预习链接
    private String EnIntroduction;// --英文介绍
    private String ChIntroduction;// --中文介绍
    private int GiftCount;//  --奖杯数

    public int getAttendLessonID() {
        return AttendLessonID;
    }

    public void setAttendLessonID(int attendLessonID) {
        AttendLessonID = attendLessonID;
    }

    public int getDetailRecordID() {
        return DetailRecordID;
    }

    public void setDetailRecordID(int detailRecordID) {
        DetailRecordID = detailRecordID;
    }

    public String getLessonTime() {
        return LessonTime;
    }

    public void setLessonTime(String lessonTime) {
        LessonTime = lessonTime;
    }

    public String getTeacherName() {
        return TeacherName;
    }

    public void setTeacherName(String teacherName) {
        TeacherName = teacherName;
    }

    public int getChapterID() {
        return ChapterID;
    }

    public void setChapterID(int chapterID) {
        ChapterID = chapterID;
    }

    public String getChapterName() {
        return ChapterName;
    }

    public void setChapterName(String chapterName) {
        ChapterName = chapterName;
    }

    public String getChapterEnglishName() {
        return ChapterEnglishName;
    }

    public void setChapterEnglishName(String chapterEnglishName) {
        ChapterEnglishName = chapterEnglishName;
    }

    public String getLevelName() {
        return LevelName;
    }

    public void setLevelName(String levelName) {
        LevelName = levelName;
    }

    public String getChapterCoverImgUrl() {
        return ChapterCoverImgUrl;
    }

    public void setChapterCoverImgUrl(String chapterCoverImgUrl) {
        ChapterCoverImgUrl = chapterCoverImgUrl;
    }

    public String getBeforeFilePath() {
        return BeforeFilePath;
    }

    public void setBeforeFilePath(String beforeFilePath) {
        BeforeFilePath = beforeFilePath;
    }

    public String getAfterFilePath() {
        return AfterFilePath;
    }

    public void setAfterFilePath(String afterFilePath) {
        AfterFilePath = afterFilePath;
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

    public int getGiftCount() {
        return GiftCount;
    }

    public void setGiftCount(int giftCount) {
        GiftCount = giftCount;
    }
}
