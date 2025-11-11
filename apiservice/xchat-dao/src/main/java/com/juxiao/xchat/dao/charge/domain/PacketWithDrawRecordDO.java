package com.juxiao.xchat.dao.charge.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @class: PacketWithDrawRecordDO.java
 * @author: chenjunsheng
 * @date 2018/6/13
 */
@Getter
@Setter
public class PacketWithDrawRecordDO {
    private String recordId;
    private Long uid;
    private Integer packetProdCashId;
    private Double packetNum;
    private Byte recordStatus;
    private Date createTime;
    private Byte tranType;
    private String wxOpenId;

}