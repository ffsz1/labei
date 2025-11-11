package com.erban.main.model;

import java.util.Date;

public class Room {
    private Long uid;

    private Long roomId;

    private String roomPwd;

    private Integer tagId;

    private String tagPict;

    private String roomTag;

    private String badge;

    private String meetingName;

    private String title;

    private Boolean valid;

    private Byte type;

    private Byte officialRoom;

    private Byte abChannelType;

    private Long rewardId;

    private Long rewardMoney;

    private Integer servDura;

    private Byte operatorStatus;

    private String avatar;

    private String roomDesc;

    private String roomNotice;

    private String backPic;

    private Date openTime;

    private Byte isPermitRoom;

    private Integer onlineNum;

    private Date createTime;

    private Date updateTime;

    private Boolean isExceptionClose;

    private Date exceptionCloseTime;

    private Long recomSeq;

    private Byte canShow;

    private String defBackpic;

    private Byte giftEffectSwitch;

    private Byte publicChatSwitch;

    private Integer audioLevel;

    private Integer factor;// 数据库没有这个字段，但是房间扩展字段要使用

    private Integer charmOpen;

    private Integer faceType;

    /**
     * 座驾特效，默认关闭过滤（0关1开）
     */
    private Integer giftCardSwitch;

    public Integer getFactor() {
        return factor;
    }

    public void setFactor(Integer factor) {
        this.factor = factor;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getRoomPwd() {
        return roomPwd;
    }

    public void setRoomPwd(String roomPwd) {
        this.roomPwd = roomPwd == null ? null : roomPwd.trim();
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public String getTagPict() {
        return tagPict;
    }

    public void setTagPict(String tagPict) {
        this.tagPict = tagPict == null ? null : tagPict.trim();
    }

    public String getRoomTag() {
        return roomTag;
    }

    public void setRoomTag(String roomTag) {
        this.roomTag = roomTag == null ? null : roomTag.trim();
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge == null ? null : badge.trim();
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName == null ? null : meetingName.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Byte getOfficialRoom() {
        return officialRoom;
    }

    public void setOfficialRoom(Byte officialRoom) {
        this.officialRoom = officialRoom;
    }

    public Byte getAbChannelType() {
        return abChannelType;
    }

    public void setAbChannelType(Byte abChannelType) {
        this.abChannelType = abChannelType;
    }

    public Long getRewardId() {
        return rewardId;
    }

    public void setRewardId(Long rewardId) {
        this.rewardId = rewardId;
    }

    public Long getRewardMoney() {
        return rewardMoney;
    }

    public void setRewardMoney(Long rewardMoney) {
        this.rewardMoney = rewardMoney;
    }

    public Integer getServDura() {
        return servDura;
    }

    public void setServDura(Integer servDura) {
        this.servDura = servDura;
    }

    public Byte getOperatorStatus() {
        return operatorStatus;
    }

    public void setOperatorStatus(Byte operatorStatus) {
        this.operatorStatus = operatorStatus;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar == null ? null : avatar.trim();
    }

    public String getRoomDesc() {
        return roomDesc;
    }

    public void setRoomDesc(String roomDesc) {
        this.roomDesc = roomDesc == null ? null : roomDesc.trim();
    }

    public String getRoomNotice() {
        return roomNotice;
    }

    public void setRoomNotice(String roomNotice) {
        this.roomNotice = roomNotice == null ? null : roomNotice.trim();
    }

    public String getBackPic() {
        return backPic;
    }

    public void setBackPic(String backPic) {
        this.backPic = backPic == null ? null : backPic.trim();
    }

    public Date getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    public Byte getIsPermitRoom() {
        return isPermitRoom;
    }

    public void setIsPermitRoom(Byte isPermitRoom) {
        this.isPermitRoom = isPermitRoom;
    }

    public Integer getOnlineNum() {
        return onlineNum;
    }

    public void setOnlineNum(Integer onlineNum) {
        this.onlineNum = onlineNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getIsExceptionClose() {
        return isExceptionClose;
    }

    public void setIsExceptionClose(Boolean isExceptionClose) {
        this.isExceptionClose = isExceptionClose;
    }

    public Date getExceptionCloseTime() {
        return exceptionCloseTime;
    }

    public void setExceptionCloseTime(Date exceptionCloseTime) {
        this.exceptionCloseTime = exceptionCloseTime;
    }

    public Long getRecomSeq() {
        return recomSeq;
    }

    public void setRecomSeq(Long recomSeq) {
        this.recomSeq = recomSeq;
    }

    public Byte getCanShow() {
        return canShow;
    }

    public void setCanShow(Byte canShow) {
        this.canShow = canShow;
    }

    public String getDefBackpic() {
        return defBackpic;
    }

    public void setDefBackpic(String defBackpic) {
        this.defBackpic = defBackpic == null ? null : defBackpic.trim();
    }

    public Byte getGiftEffectSwitch() {
        return giftEffectSwitch;
    }

    public void setGiftEffectSwitch(Byte giftEffectSwitch) {
        this.giftEffectSwitch = giftEffectSwitch;
    }

    public Byte getPublicChatSwitch() {
        return publicChatSwitch;
    }

    public void setPublicChatSwitch(Byte publicChatSwitch) {
        this.publicChatSwitch = publicChatSwitch;
    }

    public Integer getAudioLevel() {
        return audioLevel;
    }

    public void setAudioLevel(Integer audioLevel) {
        this.audioLevel = audioLevel;
    }

    public Boolean getExceptionClose() {
        return isExceptionClose;
    }

    public void setExceptionClose(Boolean exceptionClose) {
        isExceptionClose = exceptionClose;
    }

    public Integer getCharmOpen() {
        return charmOpen;
    }

    public void setCharmOpen(Integer charmOpen) {
        this.charmOpen = charmOpen;
    }


    public Integer getFaceType() {
        return faceType;
    }

    public void setFaceType(Integer faceType) {
        this.faceType = faceType;
    }

    public Integer getGiftCardSwitch() {
        return giftCardSwitch;
    }

    public void setGiftCardSwitch(Integer giftCardSwitch) {
        this.giftCardSwitch = giftCardSwitch;
    }
}
