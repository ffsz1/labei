package com.juxiao.xchat.dao.room.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class RoomDTO implements Serializable {
    private static final long serialVersionUID = 7586504146789054567L;
    private Long uid;
    private Long erbanNo;
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
    /**
     * 背景图ID
     */
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
    /**
     * 玩法介绍
     */
    private String playInfo;
    /**
     * 背景图url
     */
    private String backPicUrl;
    /**
     * 声音质量 1 普通, 2 高音质 默认1
     */
    private Integer audioLevel;
    /**
     * 是否捡海螺厅 1，是；2，否
     */
    private Integer giftDrawEnable;
    /**
     * 座驾特效，默认关闭过滤（0关1开）
     */
    private Integer giftCardSwitch;
}