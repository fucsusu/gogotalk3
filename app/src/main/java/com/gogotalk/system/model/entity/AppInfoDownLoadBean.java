package com.gogotalk.system.model.entity;

/**
 * Created by fucc
 * Date: 2019-07-25 10:31
 */
public class AppInfoDownLoadBean {

    /**
     * VersionNumber : 2
     * Title : APP更新
     * UpdateDescribe : 亲爱的学员，GogoTalk少儿英语有版本更新了，邀请您来参与新版体验！
     * IsForcedUpdate : 0
     * DownloadUrl : http://coursefiles.oss-cn-beijing.aliyuncs.com/Hgogotalk/AndroidApk/gogotalk-test.apk
     */

    private int VersionNumber;
    private String Title;
    private String UpdateDescribe;
    private int IsForcedUpdate;
    private String DownloadUrl;

    public int getVersionNumber() {
        return VersionNumber;
    }

    public void setVersionNumber(int VersionNumber) {
        this.VersionNumber = VersionNumber;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getUpdateDescribe() {
        return UpdateDescribe;
    }

    public void setUpdateDescribe(String UpdateDescribe) {
        this.UpdateDescribe = UpdateDescribe;
    }

    public int getIsForcedUpdate() {
        return IsForcedUpdate;
    }

    public void setIsForcedUpdate(int IsForcedUpdate) {
        this.IsForcedUpdate = IsForcedUpdate;
    }

    public String getDownloadUrl() {
        return DownloadUrl;
    }

    public void setDownloadUrl(String DownloadUrl) {
        this.DownloadUrl = DownloadUrl;
    }

}
