package com.juxiao.xchat.service.api.user.bo;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateV3BO extends UserUpdateBO {
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

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
