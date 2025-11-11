package com.juxiao.xchat.dao.user.domain;


import lombok.Data;

import java.util.Date;


@Data
public class UserWordDrawOverviewDO {
    /***
     * id
     */
    private Integer id;
    /**
     * 用户id
     */
    private Long uid;
    /**
     * 活动类型
     */
    private Integer activityType;
    private Integer leftDrawNum;
    private Integer totalDrawNum;
    private Integer winDrawNum;
    private Date createTime;

    private Date updateTime;


}
