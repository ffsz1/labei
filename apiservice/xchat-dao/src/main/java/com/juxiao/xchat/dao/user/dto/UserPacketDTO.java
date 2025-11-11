package com.juxiao.xchat.dao.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserPacketDTO {
    private Long uid;
    private Double packetNum;
    private Double histPacketNum;
    private Date firstGetTime;
    private Date createTime;
    private Date updateTime;
}