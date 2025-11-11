package com.juxiao.xchat.dao.sysconf.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class AdvertiseDTO {
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