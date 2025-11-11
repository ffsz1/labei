package com.juxiao.xchat.dao.user.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AccountBlock {
    private Integer blockId;
    private Long uid;
    private Long erbanNo;
    private String phone;
    private String deviceId;
    private Byte blockType;
    private String blockIp;
    private Byte blockStatus;
    private Integer blockMinute;
    private Date blockStartTime;
    private Date blockEndTime;
    private String blockDesc;
    private Integer adminId;
    private Date createTime;
}