package com.juxiao.xchat.dao.sysconf.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Advertise {
    private Integer advId;
    private String advName;
    private String advIcon;
    private Byte skipType;
    private String skipUri;
    private Integer seqNo;
    private Byte advStatus;
    private Date startTime;
    private Date endTime;
    private Date createTime;
}