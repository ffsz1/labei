package com.tongdaxing.xchat_core.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 创建者      Created by dell
 * 创建时间    2018/12/3
 * 描述
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class UserLevelInfo implements Parcelable {


    /**
     * uid : 100812
     * avatar : https://pic.hulelive.com/FiytzKbH2nxrHC8iDxlYX-nwpQFh?imageslim
     * levelName : LV19
     * levelPercent : 0.9999
     * leftGoldNum : 1
     */

    private int uid;
    private int level;
    private String avatar;
    private String levelName;
    private double levelPercent;
    private int leftGoldNum;

    protected UserLevelInfo(Parcel in) {
        uid = in.readInt();
        level = in.readInt();
        avatar = in.readString();
        levelName = in.readString();
        levelPercent = in.readDouble();
        leftGoldNum = in.readInt();
    }

    public static final Creator<UserLevelInfo> CREATOR = new Creator<UserLevelInfo>() {
        @Override
        public UserLevelInfo createFromParcel(Parcel in) {
            return new UserLevelInfo(in);
        }

        @Override
        public UserLevelInfo[] newArray(int size) {
            return new UserLevelInfo[size];
        }
    };

    public int getUid() {
        return uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public double getLevelPercent() {
        return levelPercent;
    }

    public int getLeftGoldNum() {
        return leftGoldNum;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(uid);
        parcel.writeInt(level);
        parcel.writeString(avatar);
        parcel.writeString(levelName);
        parcel.writeDouble(levelPercent);
        parcel.writeInt(leftGoldNum);
    }

    @Override
    public String toString() {
        return "UserLevelInfo{" +
                "uid=" + uid +
                ", level=" + level +
                ", avatar='" + avatar + '\'' +
                ", levelName='" + levelName + '\'' +
                ", levelPercent=" + levelPercent +
                ", leftGoldNum=" + leftGoldNum +
                '}';
    }
}
