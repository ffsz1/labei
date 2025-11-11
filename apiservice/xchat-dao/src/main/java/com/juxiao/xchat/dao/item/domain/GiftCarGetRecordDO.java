package com.juxiao.xchat.dao.item.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @class: GiftCarGetRecordDO.java
 * @author: chenjunsheng
 * @date 2018/6/11
 */
@Getter
@Setter
public class GiftCarGetRecordDO {
    private Long recordId;
    private Long uid;
    private Long carId;
    private Integer carDate;
    private Byte type;
    private Date createTime;
}