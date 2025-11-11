package com.juxiao.xchat.service.api.user.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2018/11/14
 * @time 15:30
 */
@Data
public class AccompanyManualVO implements Comparable<AccompanyManualVO>  {

    private Long uid;

    private Long erbanNo;

    private String nick;

    private Byte gender;

    private String avatar;

    private String userVoice;

    private Integer voiceDura;

    private String height;

    private String weight;

    private String accompanyType;

    private Integer defaultTag;

    private Byte officeUser;// 1官方人员 其他非官方人员 用于判断是否有竞拍权限
    private Long roomId;
    private String title;
    private Byte type;
    private String meetingName;
    private Boolean valid;
    private Byte operatorStatus;
    private String roomDesc;
    private String roomNotice;
    private String backPic;
    private Date openTime;
    private Integer onlineNum;
    private Integer seqNo;
    private Byte abChannelType;
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

    @Override
    public int compareTo(AccompanyManualVO o) {
        return o.calcSumDataIndex > this.calcSumDataIndex ? 1 : -1;
    }
}
