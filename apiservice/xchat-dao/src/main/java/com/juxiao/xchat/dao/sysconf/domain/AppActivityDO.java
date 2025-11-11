package com.juxiao.xchat.dao.sysconf.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 活动页面
 *
 * @class: AppActivityActStatus.java
 * @author: chenjunsheng
 * @date 2018/6/7
 */
@Getter
@Setter
public class AppActivityDO {
    private Integer actId;
    private String actName;
    private Byte actStatus;
    private String actAlertVersion;
    private Byte alertWin;
    private String alertWinPic;
    private Byte alertWinLoc;
    private Byte skipType;
    private String skipUrl;
    private Date createTime;
}