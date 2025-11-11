package com.tongdaxing.xchat_core.redpacket.bean;

import java.io.Serializable;

/**
 * Created by ${Seven} on 2017/9/26.
 */

public class ActionDialogInfo implements Serializable {
   /* "actId":1,
            "actName":"红包小能手",
            "alertWin":true,
            "alertWinPic":"https://nos.netease.com/nim/NDI3OTA4NQ==/bmltYV83OTIxMjk1MTZfMTUwNTgxMDAyNTQ5NV8zNDZhNTFkYS04YTQwLTQ0NDktYWZlMi00Y2QzNWQ4MTc2M2Q",
            "alertWinLoc":1,
            "skipType":1,
            "actAlertVersion":"1.0.0"*/
   private String actId;
    private String actName;
    private boolean alertWin;
    private String alertWinPic;
    private String alertWinLoc;
    private String skipType;
    private String skipUrl;
    private String actAlertVersion;

    public String getActId() {
        return actId;
    }

    public void setActId(String actId) {
        this.actId = actId;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public boolean isAlertWin() {
        return alertWin;
    }

    public void setAlertWin(boolean alertWin) {
        this.alertWin = alertWin;
    }

    public String getAlertWinPic() {
        return alertWinPic;
    }

    public void setAlertWinPic(String alertWinPic) {
        this.alertWinPic = alertWinPic;
    }

    public String getAlertWinLoc() {
        return alertWinLoc;
    }

    public void setAlertWinLoc(String alertWinLoc) {
        this.alertWinLoc = alertWinLoc;
    }

    public String getSkipType() {
        return skipType;
    }

    public void setSkipType(String skipType) {
        this.skipType = skipType;
    }

    public String getActAlertVersion() {
        return actAlertVersion;
    }

    public void setActAlertVersion(String actAlertVersion) {
        this.actAlertVersion = actAlertVersion;
    }

    public String getSkipUrl() {
        return skipUrl;
    }

    public void setSkipUrl(String skipUrl) {
        this.skipUrl = skipUrl;
    }
}
