package com.juxiao.xchat.dao.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class StatPacketRegisterDTO {
    private String registerId;
    private Long uid;
    private Long registerUid;
    private Date createTime;
}