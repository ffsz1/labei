package com.juxiao.xchat.dao.item.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Valentine {
    private Integer id;
    private Integer maleUid;
    private Byte maleStatus;
    private Integer femaleUid;
    private Byte femaleStatus;
    private Byte valentineStatus;
    private Date createTime;
    private Date updateTime;
}