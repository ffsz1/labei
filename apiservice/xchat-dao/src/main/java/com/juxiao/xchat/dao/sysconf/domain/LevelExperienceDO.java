package com.juxiao.xchat.dao.sysconf.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class LevelExperienceDO {
    private Integer id;
    private Integer levelSeq;
    private String levelName;
    private String levelGrp;
    private Long amount;
    private Long needMin;
    private Long needMax;
    private Byte status;
    private Byte broadcast;
    private Byte interaction;
    private Date createTime;

}