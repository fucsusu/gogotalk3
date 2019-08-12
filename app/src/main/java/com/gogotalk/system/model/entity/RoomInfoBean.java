package com.gogotalk.system.model.entity;

import androidx.annotation.NonNull;

/**
 * @author whj
 * @Description:
 * @date 2019/8/1 16:56
 */
public class RoomInfoBean {

    /**
     * AttendLessonID : 1341
     * ClassRoomNum : 1341
     * TeacherName : Mio
     * ChapterData : {"ChapterID":15,"ChapterName":"海滩时光","ChapterFilePath":"http://coursefiles.oss-cn-beijing.aliyuncs.com/Hgogotalk/Courseware/L1/Lesson6/preview.html"}
     * MyStudentId : 32
     * MyStudentName : Eugene
     * OtherStudentId :
     * OtherStudnetName :
     * SystemTime : 2019-08-01 16:54:05
     * LessonTime : 2019-08-01 16:30:00
     */

    private int AttendLessonID;
    private String ClassRoomNum;
    private String TeacherName;
    private ChapterDataBean ChapterData;
    private String MyStudentId;
    private String MyStudentName;
    private String OtherStudentId;
    private String OtherStudnetName;
    private String SystemTime;
    private String LessonTime;
    private int timeToPage;
    private String MyStudentSoundUrl;
    private String OtherStudentSoundUrl;

    public String getMyStudentSoundUrl() {
        return MyStudentSoundUrl;
    }

    public void setMyStudentSoundUrl(String myStudentSoundUrl) {
        MyStudentSoundUrl = myStudentSoundUrl;
    }

    public String getOtherStudentSoundUrl() {
        return OtherStudentSoundUrl;
    }

    public void setOtherStudentSoundUrl(String otherStudentSoundUrl) {
        OtherStudentSoundUrl = otherStudentSoundUrl;
    }

    public int getTimeToPage() {
        return timeToPage;
    }

    public void setTimeToPage(int timeToPage) {
        this.timeToPage = timeToPage;
    }

    public int getAttendLessonID() {
        return AttendLessonID;
    }

    public void setAttendLessonID(int AttendLessonID) {
        this.AttendLessonID = AttendLessonID;
    }

    public String getClassRoomNum() {
        return ClassRoomNum;
    }

    public void setClassRoomNum(String ClassRoomNum) {
        this.ClassRoomNum = ClassRoomNum;
    }

    public String getTeacherName() {
        return TeacherName;
    }

    public void setTeacherName(String TeacherName) {
        this.TeacherName = TeacherName;
    }

    public ChapterDataBean getChapterData() {
        return ChapterData;
    }

    public void setChapterData(ChapterDataBean ChapterData) {
        this.ChapterData = ChapterData;
    }

    public String getMyStudentId() {
        return MyStudentId;
    }

    public void setMyStudentId(String MyStudentId) {
        this.MyStudentId = MyStudentId;
    }

    public String getMyStudentName() {
        return MyStudentName;
    }

    public void setMyStudentName(String MyStudentName) {
        this.MyStudentName = MyStudentName;
    }

    public String getOtherStudentId() {
        return OtherStudentId;
    }

    public void setOtherStudentId(String OtherStudentId) {
        this.OtherStudentId = OtherStudentId;
    }

    public String getOtherStudnetName() {
        return OtherStudnetName;
    }

    public void setOtherStudnetName(String OtherStudnetName) {
        this.OtherStudnetName = OtherStudnetName;
    }

    public String getSystemTime() {
        return SystemTime;
    }

    public void setSystemTime(String SystemTime) {
        this.SystemTime = SystemTime;
    }

    public String getLessonTime() {
        return LessonTime;
    }

    public void setLessonTime(String LessonTime) {
        this.LessonTime = LessonTime;
    }

    public static class ChapterDataBean {
        /**
         * ChapterID : 15
         * ChapterName : 海滩时光
         * ChapterFilePath : http://coursefiles.oss-cn-beijing.aliyuncs.com/Hgogotalk/Courseware/L1/Lesson6/preview.html
         */

        private int ChapterID;
        private String ChapterName;
        private String ChapterFilePath;

        public int getChapterID() {
            return ChapterID;
        }

        public void setChapterID(int ChapterID) {
            this.ChapterID = ChapterID;
        }

        public String getChapterName() {
            return ChapterName;
        }

        public void setChapterName(String ChapterName) {
            this.ChapterName = ChapterName;
        }

        public String getChapterFilePath() {
            return ChapterFilePath;
        }

        public void setChapterFilePath(String ChapterFilePath) {
            this.ChapterFilePath = ChapterFilePath;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}

