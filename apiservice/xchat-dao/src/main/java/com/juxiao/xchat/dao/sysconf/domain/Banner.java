package com.juxiao.xchat.dao.sysconf.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Banner {
    private Integer bannerId;
    private String bannerName;
    private String bannerPic;
    private Byte skipType;
    private String skipUri;
    private Integer seqNo;
    private Byte osType;
    private Byte isNewUser;
    private Byte appType;
    private Byte bannerStatus;
    private Date startTime;
    private Date endTime;
    private Date createTiem;
    /*显示位置:1.首页顶部 2.节目预告 3.连麦互动 4.交友扩列 5.活动中心*/
    private Integer viewType;
}