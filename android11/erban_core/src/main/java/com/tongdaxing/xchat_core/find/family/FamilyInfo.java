package com.tongdaxing.xchat_core.find.family;

import android.os.Parcel;
import android.os.Parcelable;

import com.tongdaxing.xchat_core.find.family.bean.MemberInfo;

import java.util.List;

/**
 * 创建者      Created by dell
 * 创建时间    2018/11/26
 * 描述        家族信息item
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class FamilyInfo implements Parcelable {

    /**
     * familyId : 1943
     * familyLogo : https://pic.mjiawl.com/Fvd5RGWY96AuEFnPtOFsN2jDfi7B?imageslim
     * familyName : 1222232
     * familyNotice : 城管局对对对
     * prestige : 0
     * member : 0
     * times : 1536147958000
     * ranking : 2
     * roomId : 55560198
     * verification : 0
     * updateTime : 1536147958000
     */

    private int familyId;
    private String familyLogo;
    private String bgimg;
    private String familyName;
    private String familyNotice;
    private String nick;
    private int prestige;
    /** 角色状态 1.族长 2.管理员 3. 普通成员 */
    private int roleStatus;
    private int member;
    private long times;
    private long createTime;
    private int ranking;
    private long roomId;
    /** 申请加⼊入验证 0不用验证，1需要验证,2不不允许任何人加入 */
    private int verification;
    private long updateTime;
    /** 禁言类型 0:解除禁言，1:禁⾔言普通成员 3:禁言整个群(包括群主) */
    private int muteType;
    /** 被邀请⼈人同意⽅方式，0-需要同意(默认),1-不需要同意 */
    private int beinvitemode;
    /** 谁可以邀请他人入群，0-管理员(默认),1-所有⼈人 */
    private int invitemode;
    /** 谁可以修改群资料，0-管理员(默认),1-所有人 备注:只有群主才能修改 */
    private int uptinfomode;
    /** 0.不需要被邀请人同意加⼊入群 1.需要被邀请⼈人同意才可以加⼊入群 */
    private int magree;
    /** 1.-禁言 0.-解禁 */
    private int mute;
    /** 1：关闭消息提醒 2：打开消息提醒，其他值无效 */
    private int ope;

    private List<MemberInfo> familyUsersDTOS;

    public FamilyInfo() {

    }

    protected FamilyInfo(Parcel in) {
        familyId = in.readInt();
        familyLogo = in.readString();
        bgimg = in.readString();
        familyName = in.readString();
        familyNotice = in.readString();
        nick = in.readString();
        prestige = in.readInt();
        roleStatus = in.readInt();
        member = in.readInt();
        times = in.readLong();
        createTime = in.readLong();
        ranking = in.readInt();
        roomId = in.readLong();
        verification = in.readInt();
        updateTime = in.readLong();
        muteType = in.readInt();
        beinvitemode = in.readInt();
        invitemode = in.readInt();
        uptinfomode = in.readInt();
        magree = in.readInt();
        mute = in.readInt();
        ope = in.readInt();
        familyUsersDTOS = in.createTypedArrayList(MemberInfo.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(familyId);
        dest.writeString(familyLogo);
        dest.writeString(bgimg);
        dest.writeString(familyName);
        dest.writeString(familyNotice);
        dest.writeString(nick);
        dest.writeInt(prestige);
        dest.writeInt(roleStatus);
        dest.writeInt(member);
        dest.writeLong(times);
        dest.writeLong(createTime);
        dest.writeInt(ranking);
        dest.writeLong(roomId);
        dest.writeInt(verification);
        dest.writeLong(updateTime);
        dest.writeInt(muteType);
        dest.writeInt(beinvitemode);
        dest.writeInt(invitemode);
        dest.writeInt(uptinfomode);
        dest.writeInt(magree);
        dest.writeInt(mute);
        dest.writeInt(ope);
        dest.writeTypedList(familyUsersDTOS);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FamilyInfo> CREATOR = new Creator<FamilyInfo>() {
        @Override
        public FamilyInfo createFromParcel(Parcel in) {
            return new FamilyInfo(in);
        }

        @Override
        public FamilyInfo[] newArray(int size) {
            return new FamilyInfo[size];
        }
    };

    public int getFamilyId() {
        return familyId;
    }

    public String getFamilyLogo() {
        return familyLogo;
    }

    public void setFamilyLogo(String familyLogo) {
        this.familyLogo = familyLogo;
    }

    public String getFamilyBg() {
        return bgimg;
    }

    public void setFamilyBg(String familyBg) {
        this.bgimg = familyBg;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getFamilyNotice() {
        return familyNotice;
    }

    public void setFamilyNotice(String familyNotice) {
        this.familyNotice = familyNotice;
    }

    public String getNick() {
        return nick;
    }

    public int getPrestige() {
        return prestige;
    }

    public int getRoleStatus() {
        return roleStatus;
    }

    public int getMember() {
        return member;
    }

    public long getTimes() {
        return times;
    }

    public long getCreateTime() {
        return createTime;
    }

    public int getRanking() {
        return ranking;
    }

    public long getRoomId() {
        return roomId;
    }

    public int getVerification() {
        return verification;
    }

    public void setVerification(int verification) {
        this.verification = verification;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public int getMuteType() {
        return muteType;
    }

    public int getBeinvitemode() {
        return beinvitemode;
    }

    public int getInvitemode() {
        return invitemode;
    }

    public int getUptinfomode() {
        return uptinfomode;
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

    public void setOpe(int ope) {
        this.ope = ope;
    }

    public List<MemberInfo> getFamilyUsersDTOS() {
        return familyUsersDTOS;
    }

    @Override
    public String toString() {
        return "FamilyInfo{" +
                "familyId=" + familyId +
                ", familyLogo='" + familyLogo + '\'' +
                ", bgimg='" + bgimg + '\'' +
                ", familyName='" + familyName + '\'' +
                ", familyNotice='" + familyNotice + '\'' +
                ", nick='" + nick + '\'' +
                ", prestige=" + prestige +
                ", roleStatus=" + roleStatus +
                ", member=" + member +
                ", times=" + times +
                ", ranking=" + ranking +
                ", roomId=" + roomId +
                ", verification=" + verification +
                ", updateTime=" + updateTime +
                ", muteType=" + muteType +
                ", beinvitemode=" + beinvitemode +
                ", invitemode=" + invitemode +
                ", uptinfomode=" + uptinfomode +
                ", magree=" + magree +
                ", mute=" + mute +
                ", ope=" + ope +
                '}';
    }
}
