package com.tongdaxing.xchat_core.room.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Function:
 * Author: Edward on 2019/6/21
 */
public class PkFingerGuessingGameInfo implements Parcelable {

    /**
     * avatar : https://pic.hnyueqiang.com/FtPDUcf119fbwCYsKiollKTspkTR?imageslim
     * erBanNo : 3862990
     * giftId : 129
     * giftName : 小王子
     * giftNum : 9
     * giftUrl : https://pic.hnyueqiang.com/Fpo74E2N0GRDhX-88uXBvJPQ9I8s?imageslim
     * nick : Edward
     * opponentAvatar : https://pic.mjiawl.com/default_head_nan.png
     * opponentErBanNo : 8875301
     * opponentNick : 图
     * opponentUid : 10316
     * recordId : 59
     * uid : 10241
     */

    private String avatar;
    private int erBanNo;
    private int giftId;
    private String giftName;
    private int giftNum;
    private String giftUrl;
    private String nick;
    private String opponentAvatar;
    private int opponentErBanNo;
    private String opponentNick;
    private int opponentUid;
    private String recordId;
    private int uid;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getErBanNo() {
        return erBanNo;
    }

    public void setErBanNo(int erBanNo) {
        this.erBanNo = erBanNo;
    }

    public int getGiftId() {
        return giftId;
    }

    public void setGiftId(int giftId) {
        this.giftId = giftId;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public int getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(int giftNum) {
        this.giftNum = giftNum;
    }

    public String getGiftUrl() {
        return giftUrl;
    }

    public void setGiftUrl(String giftUrl) {
        this.giftUrl = giftUrl;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getOpponentAvatar() {
        return opponentAvatar;
    }

    public void setOpponentAvatar(String opponentAvatar) {
        this.opponentAvatar = opponentAvatar;
    }

    public int getOpponentErBanNo() {
        return opponentErBanNo;
    }

    public void setOpponentErBanNo(int opponentErBanNo) {
        this.opponentErBanNo = opponentErBanNo;
    }

    public String getOpponentNick() {
        return opponentNick;
    }

    public void setOpponentNick(String opponentNick) {
        this.opponentNick = opponentNick;
    }

    public int getOpponentUid() {
        return opponentUid;
    }

    public void setOpponentUid(int opponentUid) {
        this.opponentUid = opponentUid;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "PkFingerGuessingGameInfo{" +
                "avatar='" + avatar + '\'' +
                ", erBanNo=" + erBanNo +
                ", giftId=" + giftId +
                ", giftName='" + giftName + '\'' +
                ", giftNum=" + giftNum +
                ", giftUrl='" + giftUrl + '\'' +
                ", nick='" + nick + '\'' +
                ", opponentAvatar='" + opponentAvatar + '\'' +
                ", opponentErBanNo=" + opponentErBanNo +
                ", opponentNick='" + opponentNick + '\'' +
                ", opponentUid=" + opponentUid +
                ", recordId='" + recordId + '\'' +
                ", uid=" + uid +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.avatar);
        dest.writeInt(this.erBanNo);
        dest.writeInt(this.giftId);
        dest.writeString(this.giftName);
        dest.writeInt(this.giftNum);
        dest.writeString(this.giftUrl);
        dest.writeString(this.nick);
        dest.writeString(this.opponentAvatar);
        dest.writeInt(this.opponentErBanNo);
        dest.writeString(this.opponentNick);
        dest.writeInt(this.opponentUid);
        dest.writeString(this.recordId);
        dest.writeInt(this.uid);
    }

    public PkFingerGuessingGameInfo() {
    }

    protected PkFingerGuessingGameInfo(Parcel in) {
        this.avatar = in.readString();
        this.erBanNo = in.readInt();
        this.giftId = in.readInt();
        this.giftName = in.readString();
        this.giftNum = in.readInt();
        this.giftUrl = in.readString();
        this.nick = in.readString();
        this.opponentAvatar = in.readString();
        this.opponentErBanNo = in.readInt();
        this.opponentNick = in.readString();
        this.opponentUid = in.readInt();
        this.recordId = in.readString();
        this.uid = in.readInt();
    }

    public static final Creator<PkFingerGuessingGameInfo> CREATOR = new Creator<PkFingerGuessingGameInfo>() {
        @Override
        public PkFingerGuessingGameInfo createFromParcel(Parcel source) {
            return new PkFingerGuessingGameInfo(source);
        }

        @Override
        public PkFingerGuessingGameInfo[] newArray(int size) {
            return new PkFingerGuessingGameInfo[size];
        }
    };
}
