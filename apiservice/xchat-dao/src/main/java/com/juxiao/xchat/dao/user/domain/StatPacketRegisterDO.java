package com.juxiao.xchat.dao.user.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class StatPacketRegisterDO {
    private String registerId;
    private Long uid;
    private Long registerUid;
    private Date createTime;
}