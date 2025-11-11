package com.juxiao.xchat.dao.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class StatPacketBounsDTO {
    private Long uid;
    private Long erbanNo;
    private String avatar;
    private String nick;
    private String bounsId;
    private Long amount;
    private String lowerNick;
    private Double packetNum;
    private Date createTime;
}
