package com.juxiao.xchat.dao.user.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PrivatePhotoDO {
    private Long pid;
    private Long uid;
    private String photoUrl;
    private Integer seqNo;
    private Date createTime;
    private Byte photoStatus;
}