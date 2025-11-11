package com.tongdaxing.xchat_core.find.family.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * 创建者      Created by dell
 * 创建时间    2018/11/27
 * 描述
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class MemberInfo implements Parcelable {

    /** 编号 */
    private long id;
    /** uid */
    private long uid;
    /** 果果号 */
    private long erbanNo;
    /** 昵称 */
    private String nike;
    /***等级*/
    private int level;
    /***头像*/
    private String avatar;
    /** 威望 */
    private int prestige;
    /** 个性签名 */
    private String userDesc;
    /** 角色状态 1.族长 2.管理员 3. 普通成员 */
    private int roleStatus;
    /** 0不不需要被邀请人同意加入群，1需要被邀请人同意才可以加入群 */
    private int magree;
    /** 1-禁⾔言，0-解禁 */
    private int mute;
    /** 1：关闭消息提醒，2：打开消息提醒，其他值无效 */
    private int ope;

    /**
     * 自定义字段  保存当前的checkBox状态
     */
    private boolean isCheck;

    protected MemberInfo(Parcel in) {
        id = in.readLong();
        uid = in.readLong();
        erbanNo = in.readLong();
        nike = in.readString();
        level = in.readInt();
        avatar = in.readString();
        prestige = in.readInt();
        userDesc = in.readString();
        roleStatus = in.readInt();
        magree = in.readInt();
        mute = in.readInt();
        ope = in.readInt();
        isCheck = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(uid);
        dest.writeLong(erbanNo);
        dest.writeString(nike);
        dest.writeInt(level);
        dest.writeString(avatar);
        dest.writeInt(prestige);
        dest.writeString(userDesc);
        dest.writeInt(roleStatus);
        dest.writeInt(magree);
        dest.writeInt(mute);
        dest.writeInt(ope);
        dest.writeByte((byte) (isCheck ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MemberInfo> CREATOR = new Creator<MemberInfo>() {
        @Override
        public MemberInfo createFromParcel(Parcel in) {
            return new MemberInfo(in);
        }

        @Override
        public MemberInfo[] newArray(int size) {
            return new MemberInfo[size];
        }
    };

    public long getUid() {
        return uid;
    }

    public long getErbanNo() {
        return erbanNo;
    }

    public String getNike() {
        return nike;
    }

    public int getLevel() {
        return level;
    }

    public String getAvatar() {
        return avatar;
    }

    public int getPrestige() {
        return prestige;
    }

    public String getUserDesc() {
        return userDesc;
    }

    public int getRoleStatus() {
        return roleStatus;
    }

    public void setRoleStatus(int roleStatus) {
        this.roleStatus = roleStatus;
    }

    public int getMagree() {
        return magree;
    }

    public int getMute() {
        return mute;
    }

    public int getOpe() {
        return ope;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemberInfo that = (MemberInfo) o;
        return id == that.id &&
                uid == that.uid &&
                erbanNo == that.erbanNo &&
                level == that.level &&
                prestige == that.prestige &&
                roleStatus == that.roleStatus &&
                magree == that.magree &&
                mute == that.mute &&
                ope == that.ope &&
                isCheck == that.isCheck &&
                Objects.equals(nike, that.nike) &&
                Objects.equals(userDesc, that.userDesc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uid, erbanNo, nike, level, prestige, userDesc, roleStatus, magree, mute, ope, isCheck);
    }

    @Override
    public String toString() {
        return "MemberInfo{" +
                "id=" + id +
                ", uid=" + uid +
                ", erbanNo=" + erbanNo +
                ", nike='" + nike + '\'' +
                ", level=" + level +
                ", prestige=" + prestige +
                ", userDesc='" + userDesc + '\'' +
                ", roleStatus=" + roleStatus +
                ", magree=" + magree +
                ", mute=" + mute +
                ", ope=" + ope +
                '}';
    }
}
