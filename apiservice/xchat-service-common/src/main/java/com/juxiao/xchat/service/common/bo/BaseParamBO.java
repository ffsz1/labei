package com.juxiao.xchat.service.common.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chris
 * @Title:
 * @date 2019-05-08
 * @time 11:20
 */
@Data
public class BaseParamBO {

    @ApiModelProperty(value = "系统类型")
    private String os;
    @ApiModelProperty(value = "系统版本")
    private String osVersion;
    @ApiModelProperty(value = "APP类型")
    private String app;
    @ApiModelProperty(value = "运营商类型")
    private String ispType;
    @ApiModelProperty(value = "网络类型")
    private String netType;
    @ApiModelProperty(value = "手机型号")
    private String model;
    @ApiModelProperty(value = "app版本")
    private String appVersion;
    @ApiModelProperty(value = "imei号")
    private String imei;
    @ApiModelProperty(value = "设备ID")
    private String deviceId;
    @ApiModelProperty(value = "下载渠道")
    private String channel;
    @ApiModelProperty(value = "推广渠道")
    private String linkedmeChannel;
    @ApiModelProperty("用户ID")
    private Long uid;
    @ApiModelProperty("用户登录凭证")
    private String ticket;
    @ApiModelProperty("加密验证字段")
    private String sn;
    @ApiModelProperty(value = "ip", hidden = true)
    private String ip;
}
