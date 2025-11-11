package com.juxiao.xchat.dao.user.domain;

import lombok.Data;

import java.util.Date;

@Data
public class UserWordDrawPrizeConfDO {

    private Integer id;

    private Integer activityType;

    private Integer prizeType;

    private Integer prizeObjId;

    private Integer prizeGoldAmount;

    private Integer status;

    private Date createTime;

    private Date updateTime;


}
