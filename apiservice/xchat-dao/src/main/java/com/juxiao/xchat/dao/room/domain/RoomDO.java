package com.juxiao.xchat.dao.room.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class RoomDO {
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
    private Integer searchTagId;
    private Integer faceType;
    private List<Integer> hideFace;
    private Integer giftEffectSwitch;
    private Integer publicChatSwitch;
    private Integer factor;// 数据库没有这个字段，但是房间扩展字段要使用
    /** 音质级别 */
    private Integer audioLevel;
    private Long erbanNo;
    /**
     * 是否捡海螺厅 1，是；2，否
     */
    private Integer giftDrawEnable;

    /**
     * 座驾特效，默认关闭过滤（0关1开）
     */
    private Integer giftCardSwitch;
}