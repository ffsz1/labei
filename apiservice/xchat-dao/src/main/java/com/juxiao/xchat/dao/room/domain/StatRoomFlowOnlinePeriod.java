package com.juxiao.xchat.dao.room.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}