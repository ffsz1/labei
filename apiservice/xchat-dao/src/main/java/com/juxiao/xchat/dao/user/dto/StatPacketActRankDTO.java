package com.juxiao.xchat.dao.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatPacketActRankDTO {
    private int seqNo;
    private Long uid;
    private double packetNum;
    private String nick;
    private String avatar;
}
