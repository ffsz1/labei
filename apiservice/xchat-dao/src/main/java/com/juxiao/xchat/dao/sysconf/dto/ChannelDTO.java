package com.juxiao.xchat.dao.sysconf.dto;

import lombok.Data;

import java.util.Date;

/**
 * 渠道信息
 */
@Data
public class ChannelDTO {
    private Integer id;
    private String channel;
    private Boolean auditOption;
    private Integer leftLevel;
    private String auditVersion;
    private Date beginTime;
    private Date endTime;
    private Date createTime;
}