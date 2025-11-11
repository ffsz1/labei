package com.juxiao.xchat.dao.task.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class WarningSmsRecord {
    private Integer recordId;
    private Long uid;
    private Byte warningType;
    private String warningValue;
    private Date createTime;
}