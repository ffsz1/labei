package com.tongdaxing.xchat_core.pay.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Function:
 * Author: Edward on 2019/5/27
 */
public class DianDianCoinInfo implements Parcelable {
    /**
     * mcoinNum : 75
     * uid : 10316
     */

    private double mcoinNum;
    private int uid;

    public double getMcoinNum() {
        return mcoinNum;
    }

    public void setMcoinNum(double mcoinNum) {
        this.mcoinNum = mcoinNum;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "DianDianCoinInfo{" +
                "mcoinNum=" + mcoinNum +
                ", uid=" + uid +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.mcoinNum);
        dest.writeInt(this.uid);
    }

    public DianDianCoinInfo() {
    }

    protected DianDianCoinInfo(Parcel in) {
        this.mcoinNum = in.readInt();
        this.uid = in.readInt();
    }

    public static final Creator<DianDianCoinInfo> CREATOR = new Creator<DianDianCoinInfo>() {
        @Override
        public DianDianCoinInfo createFromParcel(Parcel source) {
            return new DianDianCoinInfo(source);
        }

        @Override
        public DianDianCoinInfo[] newArray(int size) {
            return new DianDianCoinInfo[size];
        }
    };
}
