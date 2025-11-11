package com.juxiao.xchat.dao.room.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class HomeHotManualRecommDTO {
    private Integer recommId;
    private Long uid;
    private Integer seqNo;
    private Byte status;
    private Date startValidTime;
    private Date endValidTime;
    private Date createTime;
}