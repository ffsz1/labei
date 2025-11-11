package com.juxiao.xchat.dao.charge.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @class: ExchangeDiamondGoldRecordDO.java
 * @author: chenjunsheng
 * @date 2018/6/8
 */
@Getter
@Setter
public class ExchangeDiamondGoldRecordDO {
    private String recordId;
    private Long uid;
    private Double exDiamondNum;
    private Long exGoldNum;
    private Date createTime;
}