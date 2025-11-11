package com.tongdaxing.xchat_core.activity.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Function:
 * Author: Edward on 2019/5/27
 */
public class TeenagerModelInfo implements Parcelable {

    private long uid;
    private long erbanNo;
    private String nick;
    private String cipherCode;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getErbanNo() {
        return erbanNo;
    }

    public void setErbanNo(long erbanNo) {
        this.erbanNo = erbanNo;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getCipherCode() {
        return cipherCode;
    }

    public void setCipherCode(String cipherCode) {
        this.cipherCode = cipherCode;
    }

    @Override
    public String toString() {
        return "TeenagerModelInfo{" +
                "uid=" + uid +
                ", erbanNo=" + erbanNo +
                ", nick='" + nick + '\'' +
                ", cipherCode='" + cipherCode + '\'' +
                '}';
    }

    protected TeenagerModelInfo(Parcel in) {
    }

    public static final Creator<TeenagerModelInfo> CREATOR = new Creator<TeenagerModelInfo>() {
        @Override
        public TeenagerModelInfo createFromParcel(Parcel in) {
            return new TeenagerModelInfo(in);
        }

        @Override
        public TeenagerModelInfo[] newArray(int size) {
            return new TeenagerModelInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
