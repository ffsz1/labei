package com.juxiao.xchat.dao.sysconf.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AppVersion {
    private Integer versionId;
    private String os;
    private String version;
    private String platform;
    private Byte status;
    private String versionDesc;
    private Date publishTime;
    private Date createTime;
}