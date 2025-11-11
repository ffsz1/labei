package com.erban.main.vo;

import java.util.List;

/**
 * Created by liuguofu on 2017/7/14.
 */
public class SendGiftVo {
    private Integer giftId;
    private Long uid;
    private Long targetUid;
    private List<Long> targetUids;//全麦发礼物，收礼物uids
    private String nick;
    private String avatar;
    private Long userNo;
    private String targetNick;
    private String targetAvatar;
    private Integer experLevel;
    private Integer roomId;

    public void setTargetNick(String targetNick) {
        this.targetNick = targetNick;
    }

    public void setTargetAvatar(String targetAvatar) {
        this.targetAvatar = targetAvatar;
    }

    public String getTargetNick() {

        return targetNick;
    }

    public List<Long> getTargetUids() {
        return targetUids;
    }

    public void setTargetUids(List<Long> targetUids) {
        this.targetUids = targetUids;
    }

    public String getTargetAvatar() {
        return targetAvatar;
    }

    private Integer giftNum;

    public void setGiftNum(Integer giftNum) {
        this.giftNum = giftNum;
    }

    public Integer getGiftNum() {

        return giftNum;
    }

    public Long getUserNo() {
        return userNo;
    }

    public void setUserNo(Long userNo) {
        this.userNo = userNo;
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

    public Integer getGiftId() {
        return giftId;
    }

    public void setGiftId(Integer giftId) {
        this.giftId = giftId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getTargetUid() {
        return targetUid;
    }

    public void setTargetUid(Long targetUid) {
        this.targetUid = targetUid;
    }

    public Integer getExperLevel() {
        return experLevel;
    }

    public void setExperLevel(Integer experLevel) {
        this.experLevel = experLevel;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }
}
