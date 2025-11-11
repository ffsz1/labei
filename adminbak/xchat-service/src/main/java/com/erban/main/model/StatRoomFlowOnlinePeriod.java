package com.erban.main.model;

public class StatRoomFlowOnlinePeriod {
    private Long uid;

    private Long roomId;

    private String roomPwd;

    private Boolean valid;

    private String title;

    private Integer tagId;

    private String tagPict;

    private String roomTag;

    private String badge;

    private Byte gender;

    private String nick;

    private String avatar;

    private String roomDesc;

    private String backPic;

    private Byte operatorStatus;

    private Byte officialRoom;

    private Byte isPermitRoom;

    private Byte type;

    private Long recomSeq;

    private Integer onlineNum;

    private Long flowSumTotal;

    private Double score;

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

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
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

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick == null ? null : nick.trim();
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

    public String getBackPic() {
        return backPic;
    }

    public void setBackPic(String backPic) {
        this.backPic = backPic == null ? null : backPic.trim();
    }

    public Byte getOperatorStatus() {
        return operatorStatus;
    }

    public void setOperatorStatus(Byte operatorStatus) {
        this.operatorStatus = operatorStatus;
    }

    public Byte getOfficialRoom() {
        return officialRoom;
    }

    public void setOfficialRoom(Byte officialRoom) {
        this.officialRoom = officialRoom;
    }

    public Byte getIsPermitRoom() {
        return isPermitRoom;
    }

    public void setIsPermitRoom(Byte isPermitRoom) {
        this.isPermitRoom = isPermitRoom;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Long getRecomSeq() {
        return recomSeq;
    }

    public void setRecomSeq(Long recomSeq) {
        this.recomSeq = recomSeq;
    }

    public Integer getOnlineNum() {
        return onlineNum;
    }

    public void setOnlineNum(Integer onlineNum) {
        this.onlineNum = onlineNum;
    }

    public Long getFlowSumTotal() {
        return flowSumTotal;
    }

    public void setFlowSumTotal(Long flowSumTotal) {
        this.flowSumTotal = flowSumTotal;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
