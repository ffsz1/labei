package com.juxiao.xchat.dao.sysconf.domain;

import lombok.Data;

import java.util.Date;

@Data
public class SysNoticeDO {

    private Integer noticeId;

    private Byte type;

    private String content;

    private Integer seq;

    private Date createTime;

    private Date updateTime;

}
