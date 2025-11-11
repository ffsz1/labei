package com.juxiao.xchat.dao.sysconf.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class IconDTO {
    private String title;
    private String pic;
    private String activity;
    private String iosActivity;
    private String url;
    private String params;
    private String subtitle;
    private Integer skipType;
    private String skipUri;
}
