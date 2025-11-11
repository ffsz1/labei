package com.juxiao.xchat.dao.user.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserDrawPrettyErbanNoDO {
    private Long prettyErbanNo;
    private Byte type;
    private Byte useStatus;
    private Long useErbanNo;
    private Byte seq;
    private Date createTime;
}