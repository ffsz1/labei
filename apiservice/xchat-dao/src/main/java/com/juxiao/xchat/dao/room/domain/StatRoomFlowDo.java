package com.juxiao.xchat.dao.room.domain;

import lombok.Data;

import java.util.Date;

// 房间赠送流水记录
@Data
public class StatRoomFlowDo {
    private Integer status; // 0未分成 1已分成
    //========statroomflowtotal============
    private Long totalGoldNum; // 总金币流水
    private Double shareDiamondNum; // 水流分成
    //========users==================
    private Long uid;
    private Long erbanNo;
    private String phone;
    private String nick;
    //================================
    private Byte isPermitRoom;
    // =================================
    private Long roomId;
    private String roomTag;
    private Date createTime;
}
