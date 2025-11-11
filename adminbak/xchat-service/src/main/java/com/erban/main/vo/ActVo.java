package com.erban.main.vo;

import java.util.Date;

/**
 * Created by liuguofu on 2017/9/18.
 */
public class ActVo {
    private Integer actId;

    private String actName;

    private Boolean alertWin;

    private String alertWinPic;

    private Byte alertWinLoc;

    private Byte skipType;

    private String skipUrl;

    private String actAlertVersion;

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
        this.actName = actName;
    }

    public Boolean getAlertWin() {
        return alertWin;
    }

    public void setAlertWin(Boolean alertWin) {
        this.alertWin = alertWin;
    }

    public String getAlertWinPic() {
        return alertWinPic;
    }

    public void setAlertWinPic(String alertWinPic) {
        this.alertWinPic = alertWinPic;
    }

    public Byte getAlertWinLoc() {
        return alertWinLoc;
    }

    public void setAlertWinLoc(Byte alertWinLoc) {
        this.alertWinLoc = alertWinLoc;
    }

    public String getActAlertVersion() {
        return actAlertVersion;
    }

    public void setActAlertVersion(String actAlertVersion) {
        this.actAlertVersion = actAlertVersion;
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
        this.skipUrl = skipUrl;
    }
}
