package com.juxiao.xchat.dao.user.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserShareRecordDO {
    private String shareId;
    private Long uid;
    private Byte shareType;
    private Integer sharePageId;
    private Long targetUid;
    private Date createTime;
}