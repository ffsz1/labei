package com.erban.main.vo;

import java.util.Date;
import java.util.List;

public class RoomVo implements Comparable<RoomVo> {
    private Long uid;

    private Byte officeUser;// 1官方人员 其他非官方人员 用于判断是否有竞拍权限

    private Long roomId;

    private String title;

    private Byte type;

    private String meetingName;

    private Boolean valid;

    private Byte operatorStatus;

    private String avatar;

    private String roomDesc;

    private String roomNotice;

    private String backPic;

    private Date openTime;

    private Integer onlineNum;

    private Integer seqNo;

    private Byte abChannelType;

    private Byte gender;

    private String nick;

    private Long erbanNo;

    private String roomPwd;

    private String roomTag;

    private Byte officialRoom;

    private int calcSumDataIndex;//综合人气+流水值

    private Integer tagId;

    private String tagPict;

    private Long recomSeq;

    private String badge;

    private Byte isPermitRoom;

    private Double score;

    private byte isRecom;  // 是否皇帝推荐

    private int count;  //计数器

    private Integer factor;

    private List<Integer> hideFace;

    public List<Integer> getHideFace() {
        return hideFace;
    }

    public void setHideFace(List<Integer> hideFace) {
        this.hideFace = hideFace;
    }

    public Integer getFactor() {
        return factor;
    }

    public void setFactor(Integer factor) {
        this.factor = factor;
    }

    public byte getIsRecom() {
        return isRecom;
    }

    public void setIsRecom(byte isRecom) {
        this.isRecom = isRecom;
    }

    public String getRoomNotice() {
        return roomNotice;
    }

    public void setRoomNotice(String roomNotice) {
        this.roomNotice = roomNotice;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public Byte getIsPermitRoom() {
        return isPermitRoom;
    }

    public void setIsPermitRoom(Byte isPermitRoom) {
        this.isPermitRoom = isPermitRoom;
    }

    public Long getRecomSeq() {
        return recomSeq;
    }

    public void setRecomSeq(Long recomSeq) {
        this.recomSeq = recomSeq;
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

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Byte getGender() {

        return gender;
    }



    public Byte getOfficialRoom() {
        return officialRoom;
    }

    public void setOfficialRoom(Byte officialRoom) {
        this.officialRoom = officialRoom;
    }

    public String getRoomPwd() {
        return roomPwd;
    }

    public void setRoomPwd(String roomPwd) {
        this.roomPwd = roomPwd;
    }

    public String getNick() {
        return nick;
    }

    public void setAbChannelType(Byte abChannelType) {
        this.abChannelType = abChannelType;
    }

    public void setExceptionClose(Boolean exceptionClose) {
        isExceptionClose = exceptionClose;
    }

    public Byte getAbChannelType() {

        return abChannelType;
    }

    public Long getErbanNo() {
        return erbanNo;
    }

    public void setErbanNo(Long erbanNo) {
        this.erbanNo = erbanNo;
    }

    public int getCalcSumDataIndex() {
        return calcSumDataIndex;
    }

    public void setCalcSumDataIndex(int calcSumDataIndex) {
        this.calcSumDataIndex = calcSumDataIndex;
    }

    public Boolean getExceptionClose() {
        return isExceptionClose;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public Integer getOnlineNum() {
        return onlineNum;
    }

    public void setOnlineNum(Integer onlineNum) {
        this.onlineNum = onlineNum;
    }

    private Boolean isExceptionClose;

    public Boolean getIsExceptionClose() {
        return isExceptionClose;
    }

    public void setIsExceptionClose(Boolean isExceptionClose) {
        this.isExceptionClose = isExceptionClose;
    }

    private Date exceptionCloseTime;

    public Date getExceptionCloseTime() {
        return exceptionCloseTime;
    }

    public void setExceptionCloseTime(Date exceptionCloseTime) {
        this.exceptionCloseTime = exceptionCloseTime;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Byte getOfficeUser() {
        return officeUser;
    }

    public void setOfficeUser(Byte officeUser) {
        this.officeUser = officeUser;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
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

    @Override
    public int compareTo(RoomVo roomVo) {

        int calcSumDataIndexVo = roomVo.calcSumDataIndex;
        int calcSumDataIndexThis = this.calcSumDataIndex;

        if (calcSumDataIndexThis < calcSumDataIndexVo) {
            return -1;
        } else{
            return 1;
        }

//        Integer seqNoVo = roomVo.seqNo;
//        Integer seqNoThis = this.seqNo;
//        if (seqNoVo == null) {
//            seqNoVo = 0;
//        }
//        if (seqNoThis == null) {
//            seqNoThis = 0;
//        }
//        if (seqNoThis > seqNoVo) {
//            return -1;
//        } else if (seqNoThis < seqNoVo) {
//            return 1;
//        } else {
//            if (onlineNumThis > onlineNumVo) {
//                return -1;
//            } else if (onlineNumThis < onlineNumVo) {
//                return 1;
//            } else {
//                return 0;
//            }
//        }

    }
}
