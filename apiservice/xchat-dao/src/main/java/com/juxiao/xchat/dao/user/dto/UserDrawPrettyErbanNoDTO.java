package com.juxiao.xchat.dao.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserDrawPrettyErbanNoDTO {
    private Long prettyErbanNo;
    private Byte type;
    private Byte useStatus;
    private Long useErbanNo;
    private Byte seq;
    private Date createTime;
}