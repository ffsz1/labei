package com.juxiao.xchat.dao.sysconf.domain;

import lombok.Data;

import java.util.Date;

/**
 * @Auther: alwyn
 * @Description: 渠道信息
 * @Date: 2018/11/7 15:56
 */
@Data
public class HomeChannelDO {

    private Long id;
    private String channel;
    private String homeDefault;
    private Boolean isNew;
    private Date createTime;
    private Integer type;
    private Integer groupId;
}
