package com.juxiao.xchat.dao.task.domain;

import lombok.Data;

/**
 * @author chris
 * @date 2019-06-20
 */
@Data
public class DeviceInfoDO {

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
