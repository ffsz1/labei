package com.tongdaxing.xchat_core.home;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Function:
 * Author: Edward on 2019/4/15
 */
public class RecommendBannerInfo implements Parcelable{

    /**
     * actAlertVersion : 1.0.0
     * actId : 7
     * actName : 推荐位
     * alertWin : true
     * alertWinLoc : 3
     * alertWinPic : https://pic.hnyueqiang.com/FjqLWJREWNquIQTmvM1FMqq6R7fX?imageslim
     * skipType : 3
     * skipUrl : https://47huyu.cn/front/newgift/index.html
     */

    private String actAlertVersion;
    private int actId;
    private String actName;
    private boolean alertWin;
    private int alertWinLoc;
    private String alertWinPic;
    private int skipType;
    private String skipUrl;

    @Override
    public String toString() {
        return "RecommendBannerInfo{" +
                "actAlertVersion='" + actAlertVersion + '\'' +
                ", actId=" + actId +
                ", actName='" + actName + '\'' +
                ", alertWin=" + alertWin +
                ", alertWinLoc=" + alertWinLoc +
                ", alertWinPic='" + alertWinPic + '\'' +
                ", skipType=" + skipType +
                ", skipUrl='" + skipUrl + '\'' +
                '}';
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.actAlertVersion);
        dest.writeInt(this.actId);
        dest.writeString(this.actName);
        dest.writeByte(this.alertWin ? (byte) 1 : (byte) 0);
        dest.writeInt(this.alertWinLoc);
        dest.writeString(this.alertWinPic);
        dest.writeInt(this.skipType);
        dest.writeString(this.skipUrl);
    }

    public RecommendBannerInfo() {
    }

    protected RecommendBannerInfo(Parcel in) {
        this.actAlertVersion = in.readString();
        this.actId = in.readInt();
        this.actName = in.readString();
        this.alertWin = in.readByte() != 0;
        this.alertWinLoc = in.readInt();
        this.alertWinPic = in.readString();
        this.skipType = in.readInt();
        this.skipUrl = in.readString();
    }

    public static final Creator<RecommendBannerInfo> CREATOR = new Creator<RecommendBannerInfo>() {
        @Override
        public RecommendBannerInfo createFromParcel(Parcel source) {
            return new RecommendBannerInfo(source);
        }

        @Override
        public RecommendBannerInfo[] newArray(int size) {
            return new RecommendBannerInfo[size];
        }
    };
}
