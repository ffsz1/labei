package com.erban.main.service.im.dto;


import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class RoomDTO implements Serializable{
    private static final long serialVersionUID = 7586504146789054567L;
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
    /** 背景图ID */
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
    private Integer searchTagId;
    private Integer faceType;
    private List<Integer> hideFace;
    private Integer giftEffectSwitch;
    private Integer publicChatSwitch;
    private Integer factor;// 数据库没有这个字段，但是房间扩展字段要使用
    private Integer charmOpen;  // 魅力值开关 (0.隐藏 1.显示)
    /** 玩法介绍 */
    private String playInfo;
    /** 背景图url */
    private String backPicUrl;
    /** 声音质量 1 普通, 2 高音质 默认1 */
    private Integer audioLevel;
    private Byte giftDrawEnable;
    private Long erbanNo;

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
        this.roomPwd = roomPwd;
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
        this.tagPict = tagPict;
    }

    public String getRoomTag() {
        return roomTag;
    }

    public void setRoomTag(String roomTag) {
        this.roomTag = roomTag;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        this.avatar = avatar;
    }

    public String getRoomDesc() {
        return roomDesc;
    }

    public void setRoomDesc(String roomDesc) {
        this.roomDesc = roomDesc;
    }

    public String getRoomNotice() {
        return roomNotice;
    }

    public void setRoomNotice(String roomNotice) {
        this.roomNotice = roomNotice;
    }

    public String getBackPic() {
        return backPic;
    }

    public void setBackPic(String backPic) {
        this.backPic = backPic;
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

    public Boolean getExceptionClose() {
        return isExceptionClose;
    }

    public void setExceptionClose(Boolean exceptionClose) {
        isExceptionClose = exceptionClose;
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
        this.defBackpic = defBackpic;
    }

    public Integer getSearchTagId() {
        return searchTagId;
    }

    public void setSearchTagId(Integer searchTagId) {
        this.searchTagId = searchTagId;
    }

    public Integer getFaceType() {
        return faceType;
    }

    public void setFaceType(Integer faceType) {
        this.faceType = faceType;
    }

    public List<Integer> getHideFace() {
        return hideFace;
    }

    public void setHideFace(List<Integer> hideFace) {
        this.hideFace = hideFace;
    }

    public Integer getGiftEffectSwitch() {
        return giftEffectSwitch;
    }

    public void setGiftEffectSwitch(Integer giftEffectSwitch) {
        this.giftEffectSwitch = giftEffectSwitch;
    }

    public Integer getPublicChatSwitch() {
        return publicChatSwitch;
    }

    public void setPublicChatSwitch(Integer publicChatSwitch) {
        this.publicChatSwitch = publicChatSwitch;
    }

    public Integer getFactor() {
        return factor;
    }

    public void setFactor(Integer factor) {
        this.factor = factor;
    }

    public String getPlayInfo() {
        return playInfo;
    }

    public void setPlayInfo(String playInfo) {
        this.playInfo = playInfo;
    }

    public String getBackPicUrl() {
        return backPicUrl;
    }

    public void setBackPicUrl(String backPicUrl) {
        this.backPicUrl = backPicUrl;
    }

    public Integer getAudioLevel() {
        return audioLevel;
    }

    public void setAudioLevel(Integer audioLevel) {
        this.audioLevel = audioLevel;
    }

    public Byte getGiftDrawEnable() {
        return giftDrawEnable;
    }

    public void setGiftDrawEnable(Byte giftDrawEnable) {
        this.giftDrawEnable = giftDrawEnable;
    }

    public Long getErbanNo() {
        return erbanNo;
    }

    public void setErbanNo(Long erbanNo) {
        this.erbanNo = erbanNo;
    }

    public Integer getCharmOpen() {
        return charmOpen;
    }

    public void setCharmOpen(Integer charmOpen) {
        this.charmOpen = charmOpen;
    }
}
