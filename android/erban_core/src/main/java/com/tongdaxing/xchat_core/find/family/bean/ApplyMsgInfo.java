package com.tongdaxing.xchat_core.find.family.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 创建者      Created by dell
 * 创建时间    2018/11/27
 * 描述        家族申请消息实体
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class ApplyMsgInfo implements Parcelable {

    private long createTime;
    private String nick;
    private long erbanNo;
    private long uid;

    /**
     * 魅力等级
     */
    private int charm;

    /**
     * 等级
     */
    private int level;

    /**
     * 类型(1.加入 2.退出)
     */
    private int type;
    private String avatar;

    /**
     * 处理状态  0.审核中,1.审核成功,2.审核失败(审核失败7天自动退出）3.处理自动退出成功
     */
    private int status;

    protected ApplyMsgInfo(Parcel in) {
        createTime = in.readLong();
        nick = in.readString();
        erbanNo = in.readLong();
        uid = in.readLong();
        charm = in.readInt();
        level = in.readInt();
        type = in.readInt();
        avatar = in.readString();
        status = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(createTime);
        dest.writeString(nick);
        dest.writeLong(erbanNo);
        dest.writeLong(uid);
        dest.writeInt(charm);
        dest.writeInt(level);
        dest.writeInt(type);
        dest.writeString(avatar);
        dest.writeInt(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ApplyMsgInfo> CREATOR = new Creator<ApplyMsgInfo>() {
        @Override
        public ApplyMsgInfo createFromParcel(Parcel in) {
            return new ApplyMsgInfo(in);
        }

        @Override
        public ApplyMsgInfo[] newArray(int size) {
            return new ApplyMsgInfo[size];
        }
    };

    public long getCreateTime() {
        return createTime;
    }

    public String getNick() {
        return nick;
    }

    public long getErbanNo() {
        return erbanNo;
    }

    public long getUid() {
        return uid;
    }

    public int getCharm() {
        return charm;
    }

    public int getLevel() {
        return level;
    }

    public int getType() {
        return type;
    }

    public String getAvatar() {
        return avatar;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ApplyMsgInfo{" +
                "createTime=" + createTime +
                ", nick='" + nick + '\'' +
                ", erbanNo=" + erbanNo +
                ", uid=" + uid +
                ", charm=" + charm +
                ", level=" + level +
                ", type=" + type +
                ", avatar='" + avatar + '\'' +
                ", status=" + status +
                '}';
    }
}
