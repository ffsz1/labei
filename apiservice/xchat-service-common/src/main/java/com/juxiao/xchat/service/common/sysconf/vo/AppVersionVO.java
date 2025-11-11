package com.juxiao.xchat.service.common.sysconf.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel("AppVersion")
public class AppVersionVO {
    private String os;
    private String version;
    private Byte status;
    private String versionDesc;
    private String updateVersion;
    private String updateVersionDesc;
    private String downloadUrl;
    private Integer kickWaiting;
    private Integer newestVersion;
}
