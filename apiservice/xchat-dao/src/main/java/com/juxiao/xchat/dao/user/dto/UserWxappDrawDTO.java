package com.juxiao.xchat.dao.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserWxappDrawDTO {
    private Long uid;
    private Integer totalDrawCount;
    private Integer leftDrawCount;
    private Integer winDrawCount;
    private Date updateTime;
}
