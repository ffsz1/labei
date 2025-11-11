package com.juxiao.xchat.dao.charge.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserPacketRecordDTO {
    private String recordId;
    private Integer packetProdCashId;
    private Double packetNum;
    private Date createTime;
    private Long uid;
    private Long erbanNo;
    private String nick;
    private String avatar;
}
