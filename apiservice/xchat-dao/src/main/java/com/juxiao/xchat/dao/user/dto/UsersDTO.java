package com.juxiao.xchat.dao.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UsersDTO {
    private Long uid;
    private Long erbanNo;
    private Boolean hasPrettyErbanNo;
    private String phone;
    private Date birth;
    private Byte star;
    private String nick;
    private String email;
    private String signture;
    private String userVoice;
    private Integer voiceDura;
    private Integer followNum;
    private Integer fansNum;
    private Byte defUser;
    private Long fortune;
    private Byte channelType;
    private Date lastLoginTime;
    private String lastLoginIp;
    private Byte gender;
    private String avatar;
    private String region;
    private String userDesc;
    private String alipayAccount;
    private String alipayAccountName;
    private String bankCard;
    private String bankCardName;
    private Date createTime;
    private Date updateTime;
    private String wxPubFansOpenid;
    private Byte wxPubFansGender;
    private Long roomUid;
    private Long shareUid;
    private Byte shareChannel;
    private String wxOpenid;
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
    private Integer nobleId;
    private String nobleName;
    private Byte withdrawStatus;
    private String accompanyType;
    private Integer defaultTag;
    private String weight;
    private String height;
    private Long shareCode;
    private String realNamePhone;
}