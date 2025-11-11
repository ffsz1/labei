package com.tongdaxing.xchat_core.find;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * 创建者      Created by dell
 * 创建时间    2019/1/3
 * 描述        获取广告活动
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class AlertInfo implements MultiItemEntity {

    private String actAlertVersion;
    private int actId;
    private String actName;
    private boolean alertWin;
    private int alertWinLoc;
    private String alertWinPic;
    private int skipType;
    private String skipUrl;
    private int itemType;

    public String getActAlertVersion() {
        return actAlertVersion;
    }

    public void setActAlertVersion(String actAlertVersion) {
        this.actAlertVersion = actAlertVersion;
    }

    public int getActId() {
        return actId;
    }

    public void setActId(int actId) {
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

    public int getAlertWinLoc() {
        return alertWinLoc;
    }

    public void setAlertWinLoc(int alertWinLoc) {
        this.alertWinLoc = alertWinLoc;
    }

    public String getAlertWinPic() {
        return alertWinPic;
    }

    public void setAlertWinPic(String alertWinPic) {
        this.alertWinPic = alertWinPic;
    }

    public int getSkipType() {
        return skipType;
    }

    public void setSkipType(int skipType) {
        this.skipType = skipType;
    }

    public String getSkipUrl() {
        return skipUrl;
    }

    public void setSkipUrl(String skipUrl) {
        this.skipUrl = skipUrl;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public String toString() {
        return "AlertInfo{" +
                "actAlertVersion='" + actAlertVersion + '\'' +
                ", actId=" + actId +
                ", actName='" + actName + '\'' +
                ", alertWin=" + alertWin +
                ", alertWinLoc=" + alertWinLoc +
                ", alertWinPic='" + alertWinPic + '\'' +
                ", skipType=" + skipType +
                ", skipUrl='" + skipUrl + '\'' +
                ", itemType=" + itemType +
                '}';
    }
}
