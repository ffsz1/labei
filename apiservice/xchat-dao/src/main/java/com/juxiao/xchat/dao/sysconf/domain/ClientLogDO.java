package com.juxiao.xchat.dao.sysconf.domain;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName: ClientLogDO
 * @Description: 客户端日志DO
 * @Author: alwyn
 * @Date: 2019/4/19 15:23
 * @Version: 1.0
 */
@Data
public class ClientLogDO {

    private Long id;
    private Long uid;
    private String url;
    private String os;
    private String osVersion;
    private String model;
    private String deviceId;
    private String app;
    private String appVersion;
    private Date createTime;
    private String ip;
    private String erbanNo;
}
