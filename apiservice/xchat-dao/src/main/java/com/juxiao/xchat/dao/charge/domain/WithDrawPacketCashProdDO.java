package com.juxiao.xchat.dao.charge.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class WithDrawPacketCashProdDO {
    private Integer packetProdCashId;
    private Double packetNum;
    private Byte prodStauts;
    private Integer seqNo;
    private Date createTime;
}