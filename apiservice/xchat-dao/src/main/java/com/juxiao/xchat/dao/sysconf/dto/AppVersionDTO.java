package com.juxiao.xchat.dao.sysconf.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AppVersionDTO {
    private Integer versionId;
    private String os;
    private String version;
    private String platform;
    private Byte status;
    private String versionDesc;
    private Date publishTime;
    private Date createTime;
    private String appId;
    private String downloadUrl;

    public AppVersionDTO() {
        super();
    }

    public AppVersionDTO(String version, String os) {
        this();
        this.os = os;
        this.version = version;
    }
}