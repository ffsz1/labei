package com.juxiao.xchat.dao.user.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AccountDO {
    private Long uid;
    private String phone;
    private Long erbanNo;
    private String password;
    private String passwordSecond;
    private String neteaseToken;
    private String state;
    private Date lastLoginTime;
    private String lastLoginIp;
    private String weixinOpenid;
    private String weixinUnionid;
    private String qqOpenid;
    private String qqUnionid;
    private String os;
    private String osversion;
    private String app;
    private String imei;
    private String channel;
    private String linkedmeChannel;
    private String ispType;
    private String netType;
    private String model;
    private String deviceId;
    private String appVersion;
    private Date accBlockStartTime;
    private Date accBlockEndTime;
    private Date deviceBlockStartTime;
    private Date deviceBlockEndTime;
    private Date signTime;
    private Date updateTime;
    private String registerIp;
    private String widthdrawWxName;
    private String widthdrawWxOpenId;
    private String alipayAccount;
    private String alipayName;
}