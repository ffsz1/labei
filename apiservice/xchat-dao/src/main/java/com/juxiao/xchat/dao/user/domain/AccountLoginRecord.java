package com.juxiao.xchat.dao.user.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AccountLoginRecord {
    private Long recordId;
    private Long uid;
    private Long erbanNo;
    private String phone;
    private Byte loginType;
    private String loginIp;
    private String weixinOpenid;
    private String qqOpenid;
    private String os;
    private String osversion;
    private String app;
    private String imei;
    private String ispType;
    private String netType;
    private String model;
    private String deviceId;
    private String appVersion;
    private Date createTime;
}