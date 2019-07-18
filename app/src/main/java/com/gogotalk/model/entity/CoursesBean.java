package com.gogotalk.model.entity;

/**
 * Copyright (C)
 * <p>
 * FileName: CoursesBean
 * <p>
 * Author: 赵小钧
 * <p>
 * Date: 2019\6\17 0017 18:52
 */
public class CoursesBean {

    int AttendLessonID;// --班级ID
    int DetailRecordID;// --约课详情ID
    String LessonTime;//--H5页面课程时间格式
    String LessonTimeApp;//--移动端用的上课时间格式
    String LevelName;//--级别
    String TeacherName;//--外教名称
    int ChapterID;//  --教材ID
    String ChapterCoverImgUrl;//--教材封面
    String ChapterName;//  --中文名
    String ChapterFilePath;//--对应的H5课件地址
    String ChapterEnglishName;//--英文名
    String BeforeFilePath;// --课前练习视频地址
    String AfterFilePath;// --课后互动练习地址
    String EnIntroduction;// --中文描述
    String ChIntroduction;// --英文名描述
    int LessonStatus;// --状态 0 是未开始  1 上课中 2 即将开始 3 已结束
    String Serial;//    -- 房间号 目前没用
    String SystemTime;// 系统时间，跟上课时间比较，判断什么时候进入教室按钮可点击

    public String getChapterFilePath() {
        return ChapterFilePath;
    }

    public void setChapterFilePath(String chapterFilePath) {
        ChapterFilePath = chapterFilePath;
    }

    public String getSystemTime() {
        return SystemTime;
    }

    public void setSystemTime(String systemTime) {
        SystemTime = systemTime;
    }

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

    public String getLessonTimeApp() {
        return LessonTimeApp;
    }

    public void setLessonTimeApp(String lessonTimeApp) {
        LessonTimeApp = lessonTimeApp;
    }

    public String getLevelName() {
        return LevelName;
    }

    public void setLevelName(String levelName) {
        LevelName = levelName;
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

    public String getChapterCoverImgUrl() {
        return ChapterCoverImgUrl;
    }

    public void setChapterCoverImgUrl(String chapterCoverImgUrl) {
        ChapterCoverImgUrl = chapterCoverImgUrl;
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

    public int getLessonStatus() {
        return LessonStatus;
    }

    public void setLessonStatus(int lessonStatus) {
        LessonStatus = lessonStatus;
    }

    public String getSerial() {
        return Serial;
    }

    public void setSerial(String serial) {
        Serial = serial;
    }
}
