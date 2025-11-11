package com.tongdaxing.xchat_core.gift;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chenran on 2017/10/25.
 */

public class MultiGiftReceiveInfo implements Serializable {
    private long uid;
    private List<Long> targetUids;
    private String roomUid;
    private int giftId;
    private int giftNum;
    private String nick;
    private String avatar;
    private int userGiftPurseNum;
    private int useGiftPurseGold;
    private long giftSendTime;
    private int experLevel;

    public String getRoomUid() {
        return roomUid;
    }

    public void setRoomUid(String roomUid) {
        this.roomUid = roomUid;
    }

    public int getExperLevel() {
        return experLevel;
    }

    public void setExperLevel(int experLevel) {
        this.experLevel = experLevel;
    }

    public int getUseGiftPurseGold() {
        return useGiftPurseGold;
    }

    public void setUseGiftPurseGold(int useGiftPurseGold) {
        this.useGiftPurseGold = useGiftPurseGold;
    }

    public int getUserGiftPurseNum() {
        return userGiftPurseNum;
    }

    public void setUserGiftPurseNum(int userGiftPurseNum) {
        this.userGiftPurseNum = userGiftPurseNum;
    }

    private List<Integer> roomIdList;

    public List<Integer> getRoomIdList() {
        return roomIdList;
    }

    public void setRoomIdList(List<Integer> roomIdList) {
        this.roomIdList = roomIdList;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public List<Long> getTargetUids() {
        return targetUids;
    }

    public void setTargetUids(List<Long> targetUids) {
        this.targetUids = targetUids;
    }

    public int getGiftId() {
        return giftId;
    }

    public void setGiftId(int giftId) {
        this.giftId = giftId;
    }

    public int getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(int giftNum) {
        this.giftNum = giftNum;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getGiftSendTime() {
        return giftSendTime;
    }

    public void setGiftSendTime(long giftSendTime) {
        this.giftSendTime = giftSendTime;
    }

    @Override
    public String toString() {
        return "MultiGiftReceiveInfo{" +
                "uid=" + uid +
                ", targetUids=" + targetUids +
                ", giftId=" + giftId +
                ", giftNum=" + giftNum +
                ", nick='" + nick + '\'' +
                ", avatar='" + avatar + '\'' +
                ", userGiftPurseNum=" + userGiftPurseNum +
                ", useGiftPurseGold=" + useGiftPurseGold +
                ", giftSendTime=" + giftSendTime +
                ", experLevel=" + experLevel +
                ", roomIdList=" + roomIdList +
                '}';
    }
}
