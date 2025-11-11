package com.erban.main.vo;

import java.util.List;

public class GiftSendVo {
    private Long uid;
    private String avatar;
    private String nick;
    private Long targetUid;
    private List<Long> targetUids;//全麦送使用
    private String targetAvatar;
    private String targetNick;
    private Integer giftId;
    private int giftNum;
    private Integer experLevel;
    private Long userGiftPurseNum;// 剩余多少用户礼物
    private Long useGiftPurseGold;// 使用了多少用户礼物（金币值）

    public Long getUserGiftPurseNum() {
        return userGiftPurseNum;
    }

    public void setUserGiftPurseNum(Long userGiftPurseNum) {
        this.userGiftPurseNum = userGiftPurseNum;
    }

    public Long getUseGiftPurseGold() {
        return useGiftPurseGold;
    }

    public void setUseGiftPurseGold(Long useGiftPurseGold) {
        this.useGiftPurseGold = useGiftPurseGold;
    }

    public Long getUid() {
        return uid;
    }

    public List<Long> getTargetUids() {
        return targetUids;
    }

    public void setTargetUids(List<Long> targetUids) {
        this.targetUids = targetUids;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Long getTargetUid() {
        return targetUid;
    }

    public void setTargetUid(Long targetUid) {
        this.targetUid = targetUid;
    }

    public String getTargetAvatar() {
        return targetAvatar;
    }

    public void setTargetAvatar(String targetAvatar) {
        this.targetAvatar = targetAvatar;
    }

    public String getTargetNick() {
        return targetNick;
    }

    public void setTargetNick(String targetNick) {
        this.targetNick = targetNick;
    }

    public Integer getGiftId() {
        return giftId;
    }

    public void setGiftId(Integer giftId) {
        this.giftId = giftId;
    }

    public int getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(int giftNum) {
        this.giftNum = giftNum;
    }

    public Integer getExperLevel() {
        return experLevel;
    }

    public void setExperLevel(Integer experLevel) {
        this.experLevel = experLevel;
    }
}
