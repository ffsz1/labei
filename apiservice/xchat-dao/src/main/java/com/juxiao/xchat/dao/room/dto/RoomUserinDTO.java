package com.juxiao.xchat.dao.room.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class RoomUserinDTO implements Comparable<RoomUserinDTO> {
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
    /**
     * 背景图ID
     */
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
    private Boolean isExceptionClose;
    private Date exceptionCloseTime;
    private byte isRecom;  // 是否皇帝推荐
    private int count;  //计数器
    private List<Integer> hideFace;
    private Integer giftEffectSwitch;
    private Integer publicChatSwitch;
    private Integer factor;// 数据库没有这个字段，但是房间扩展字段要使用
    private Integer charmOpen;  // 魅力值开关
    /**
     * 玩法介绍
     */
    private String playInfo;
    /**
     * 背景图地址
     */
    private String backPicUrl;
    /**
     * 声音质量
     */
    private Integer audioLevel;
    /**
     * 是否捡海螺房间，1是；2；否
     */
    private Integer giftDrawEnable;

    /**
     * 座驾特效，默认关闭过滤（0关1开）
     */
    private Integer giftCardSwitch;

    @Override
    public int compareTo(RoomUserinDTO userinDto) {
        if (this.calcSumDataIndex < userinDto.calcSumDataIndex) {
            return -1;
        } else {
            return 1;
        }
    }

    public Boolean getExceptionClose() {
        return isExceptionClose;
    }
}
