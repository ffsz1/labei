package com.juxiao.xchat.dao.event.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class UserSignCardDO {

    private Long id;
    private Long uid;
    private Long chipId;
    private Integer chipNum;
    private Date createDate;
}
