package com.juxiao.xchat.dao.sysconf.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Auther: alwyn
 * @Description: 客户端安全上报DO
 * @Date: 2018/10/30 16:11
 */
@ApiModel("安全上报信息")
@Data
public class ClientSecurityInfoDO {

    @ApiModelProperty(value = "UID")
    private Long uid;
    @ApiModelProperty(value = "消息ID")
    private String msgId;
    @ApiModelProperty(value = "设备ID")
    private String deviceId;
    @ApiModelProperty(value = "创建时间", hidden = true)
    private Date createDate;
    @ApiModelProperty(value = "更新时间", hidden = true)
    private Date updateDate;
    @ApiModelProperty(value = "APP类型")
    private String app;
    @ApiModelProperty(value = "app 版本")
    private String appVersion;
    @ApiModelProperty(value = "系统类型")
    private String os;
    @ApiModelProperty(value = "系统版本")
    private String osVersion;
    @ApiModelProperty(value = "手机型号")
    private String model;
    @ApiModelProperty(value = "下载渠道")
    private String channel;
    @ApiModelProperty(value = "总共操作次数", hidden = true)
    private Long totalNum;
    @ApiModelProperty(value = "签名验证")
    private String sn;
    @ApiModelProperty(value = "报文内容")
    private String content;
}
