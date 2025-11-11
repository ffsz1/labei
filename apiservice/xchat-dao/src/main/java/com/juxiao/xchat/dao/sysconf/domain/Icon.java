package com.juxiao.xchat.dao.sysconf.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Icon {
    private Integer iconId;
    private Byte type;
    private Byte os;
    private Integer seq;
    private String name;
    private String pic;
    private String activity;
    private String iosActivity;
    private String iconUrl;
    private Integer skipType;
    private String skipUri;
    private String key1;
    private String value1;
    private String key2;
    private String value2;
    private String key3;
    private String value3;
    private Byte status;
    private Date createTime;
    private String subtitle;
}