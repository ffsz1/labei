package com.juxiao.xchat.dao.sysconf.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SmsRecordDO {
    private Long smsRecordId;
    private String phone;
    private String deviceId;
    private String imei;
    private String os;
    private String osversion;
    private String channel;
    private String appVersion;
    private String model;
    private String smsCode;
    private Byte smsType;
    private String ip;
    private Date createTime;
}