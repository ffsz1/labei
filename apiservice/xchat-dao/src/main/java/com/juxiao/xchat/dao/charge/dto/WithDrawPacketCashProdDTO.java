package com.juxiao.xchat.dao.charge.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class WithDrawPacketCashProdDTO {
    private Integer packetProdCashId;
    private Double packetNum;
    private Byte prodStauts;
    private Integer seqNo;
    private Date createTime;
}