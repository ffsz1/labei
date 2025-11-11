package com.erban.main.model;

import java.util.Date;

public class AppActivity {
    private Integer actId;

    private String actName;

    private Byte actStatus;

    private String actAlertVersion;

    private Byte alertWin;

    private String alertWinPic;

    private Byte alertWinLoc;

    private Byte skipType;

    private String skipUrl;

    private Date createTime;

    public Integer getActId() {
        return actId;
    }

    public void setActId(Integer actId) {
        this.actId = actId;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName == null ? null : actName.trim();
    }

    public Byte getActStatus() {
        return actStatus;
    }

    public void setActStatus(Byte actStatus) {
        this.actStatus = actStatus;
    }

    public String getActAlertVersion() {
        return actAlertVersion;
    }

    public void setActAlertVersion(String actAlertVersion) {
        this.actAlertVersion = actAlertVersion == null ? null : actAlertVersion.trim();
    }

    public Byte getAlertWin() {
        return alertWin;
    }

    public void setAlertWin(Byte alertWin) {
        this.alertWin = alertWin;
    }

    public String getAlertWinPic() {
        return alertWinPic;
    }

    public void setAlertWinPic(String alertWinPic) {
        this.alertWinPic = alertWinPic == null ? null : alertWinPic.trim();
    }

    public Byte getAlertWinLoc() {
        return alertWinLoc;
    }

    public void setAlertWinLoc(Byte alertWinLoc) {
        this.alertWinLoc = alertWinLoc;
    }

    public Byte getSkipType() {
        return skipType;
    }

    public void setSkipType(Byte skipType) {
        this.skipType = skipType;
    }

    public String getSkipUrl() {
        return skipUrl;
    }

    public void setSkipUrl(String skipUrl) {
        this.skipUrl = skipUrl == null ? null : skipUrl.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
