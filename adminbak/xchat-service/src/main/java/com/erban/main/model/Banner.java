package com.erban.main.model;

import java.util.Date;

public class Banner {
    private Integer bannerId;

    private String bannerName;

    private String bannerPic;

    private Byte skipType;

    private String skipUri;

    private Integer seqNo;

    private Byte osType;

    private Byte isNewUser;

    private Byte appType;

    private Byte bannerStatus;

    private Date startTime;

    private Date endTime;

    private Date createTiem;

    /*显示位置:1.首页顶部 2.节目预告 3.连麦互动 4.交友扩列 5.活动中心*/
    private Byte viewType;

    private String addShowTag;

    public Integer getBannerId() {
        return bannerId;
    }

    public void setBannerId(Integer bannerId) {
        this.bannerId = bannerId;
    }

    public String getBannerName() {
        return bannerName;
    }

    public void setBannerName(String bannerName) {
        this.bannerName = bannerName == null ? null : bannerName.trim();
    }

    public String getBannerPic() {
        return bannerPic;
    }

    public void setBannerPic(String bannerPic) {
        this.bannerPic = bannerPic == null ? null : bannerPic.trim();
    }

    public Byte getSkipType() {
        return skipType;
    }

    public void setSkipType(Byte skipType) {
        this.skipType = skipType;
    }

    public String getSkipUri() {
        return skipUri;
    }

    public void setSkipUri(String skipUri) {
        this.skipUri = skipUri == null ? null : skipUri.trim();
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public Byte getOsType() {
        return osType;
    }

    public void setOsType(Byte osType) {
        this.osType = osType;
    }

    public Byte getIsNewUser() {
        return isNewUser;
    }

    public void setIsNewUser(Byte isNewUser) {
        this.isNewUser = isNewUser;
    }

    public Byte getAppType() {
        return appType;
    }

    public void setAppType(Byte appType) {
        this.appType = appType;
    }

    public Byte getBannerStatus() {
        return bannerStatus;
    }

    public void setBannerStatus(Byte bannerStatus) {
        this.bannerStatus = bannerStatus;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreateTiem() {
        return createTiem;
    }

    public void setCreateTiem(Date createTiem) {
        this.createTiem = createTiem;
    }

    public Byte getViewType() {
        return viewType;
    }

    public void setViewType(Byte viewType) {
        this.viewType = viewType;
    }

    public String getAddShowTag() {
        return addShowTag;
    }

    public void setAddShowTag(String addShowTag) {
        this.addShowTag = addShowTag;
    }
}
