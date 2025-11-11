package com.juxiao.xchat.dao.user.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class UserWordDrawOverviewDTO {
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


}
