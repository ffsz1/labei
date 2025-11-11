package com.juxiao.xchat.dao.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserDrawDTO {
    private Long uid;
    private Integer leftDrawNum;
    private Integer totalDrawNum;
    private Integer totalWinDrawNum;
    private Boolean isFirstShare;
    private Date createTime;
    private Date updateTime;
}