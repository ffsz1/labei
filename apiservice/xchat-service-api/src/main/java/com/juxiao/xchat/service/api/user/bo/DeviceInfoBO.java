package com.juxiao.xchat.service.api.user.bo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceInfoBO {
    private String os;
    private String osVersion;
    private String app;
    private String ispType;
    private String netType;
    private String model;
    private String appVersion;
    private String imei;
    private String deviceId;
    private String channel;
    private String linkedmeChannel;
    private String ip;
}
