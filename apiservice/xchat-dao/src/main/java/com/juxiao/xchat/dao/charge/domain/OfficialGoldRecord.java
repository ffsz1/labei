package com.juxiao.xchat.dao.charge.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class OfficialGoldRecord {
    private Integer recordId;
    private Long uid;
    private Long goldNum;
    private Byte type;
    private Date createTime;
}