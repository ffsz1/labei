package com.juxiao.xchat.manager.common.room.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    private Boolean exceptionClose;
    private Boolean isExceptionClose;
    private Date exceptionCloseTime;
    private Integer status;
    private String userDescription;

    @Override
    public int compareTo(RoomVo o) {
        return o.calcSumDataIndex > this.calcSumDataIndex ? 1 : -1;
    }
}
