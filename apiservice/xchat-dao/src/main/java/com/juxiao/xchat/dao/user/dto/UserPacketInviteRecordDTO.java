package com.juxiao.xchat.dao.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserPacketInviteRecordDTO {
    private Long uid;
    private String nick;
    private String avatar;
    private Date createTime;
    private Double packetNum;
}
