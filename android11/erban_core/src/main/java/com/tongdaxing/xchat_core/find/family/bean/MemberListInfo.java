package com.tongdaxing.xchat_core.find.family.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 创建者      Created by dell
 * 创建时间    2018/11/27
 * 描述        家族列表info
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 */
public class MemberListInfo implements Parcelable {

    /** 家族ID,群聊tid */
    private long familyId;
    /** 家族logo */
    private String familyLogo;
    /** 家族名称 */
    private String familyName;
    /** 家族公告 */
    private String familyNotice;
    /** 家族威望 */
    private int prestige;
    /** 家族成员 */
    private int member;
    /** 时间戳 */
    private long times;
    /** 排名 */
    private int ranking;
    /** 群聊tid */
    private long roomId;
    /** 申请加⼊入验证 0不不⽤用验证，1需要验证,2不不允许任何⼈人加⼊入 */
    private int verification;
    /** 禁⾔言类型 0:解除禁⾔言，1:禁⾔言普通成员 3:禁⾔言整个群(包括群主) */
    private int muteType;
    /** 被邀请⼈人同意⽅方式，0-需要同意(默认),1-不不需要同意 */
    private int beinvitemode;
    /** 谁可以邀请他⼈人⼊入群，0-管理理员(默认),1-所有⼈人 */
    private int invitemode;
    /** 谁可以修改群资料料，0-管理理员(默认),1-所有⼈人 备注:只有群主才能修改 */
    private int uptinfomode;

    private List<MemberInfo> familyTeamJoinDTOS;


    protected MemberListInfo(Parcel in) {
        familyId = in.readLong();
        familyLogo = in.readString();
        familyName = in.readString();
        familyNotice = in.readString();
        prestige = in.readInt();
        member = in.readInt();
        times = in.readLong();
        ranking = in.readInt();
        roomId = in.readLong();
        verification = in.readInt();
        muteType = in.readInt();
        beinvitemode = in.readInt();
        invitemode = in.readInt();
        uptinfomode = in.readInt();
        familyTeamJoinDTOS = in.createTypedArrayList(MemberInfo.CREATOR);
    }

    public static final Creator<MemberListInfo> CREATOR = new Creator<MemberListInfo>() {
        @Override
        public MemberListInfo createFromParcel(Parcel in) {
            return new MemberListInfo(in);
        }

        @Override
        public MemberListInfo[] newArray(int size) {
            return new MemberListInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(familyId);
        parcel.writeString(familyLogo);
        parcel.writeString(familyName);
        parcel.writeString(familyNotice);
        parcel.writeInt(prestige);
        parcel.writeInt(member);
        parcel.writeLong(times);
        parcel.writeInt(ranking);
        parcel.writeLong(roomId);
        parcel.writeInt(verification);
        parcel.writeInt(muteType);
        parcel.writeInt(beinvitemode);
        parcel.writeInt(invitemode);
        parcel.writeInt(uptinfomode);
        parcel.writeTypedList(familyTeamJoinDTOS);
    }

    public long getFamilyId() {
        return familyId;
    }

    public String getFamilyLogo() {
        return familyLogo;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getFamilyNotice() {
        return familyNotice;
    }

    public int getPrestige() {
        return prestige;
    }

    public int getMember() {
        return member;
    }

    public long getTimes() {
        return times;
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

    public List<MemberInfo> getFamilyTeamJoinDTOS() {
        return familyTeamJoinDTOS;
    }

    @Override
    public String toString() {
        return "MemberListInfo{" +
                "familyId=" + familyId +
                ", familyLogo='" + familyLogo + '\'' +
                ", familyName='" + familyName + '\'' +
                ", familyNotice='" + familyNotice + '\'' +
                ", prestige=" + prestige +
                ", member=" + member +
                ", times=" + times +
                ", ranking=" + ranking +
                ", roomId=" + roomId +
                ", verification=" + verification +
                ", muteType=" + muteType +
                ", beinvitemode=" + beinvitemode +
                ", invitemode=" + invitemode +
                ", uptinfomode=" + uptinfomode +
                ", familyTeamJoinDTOS=" + familyTeamJoinDTOS +
                '}';
    }
}
