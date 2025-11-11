package com.juxiao.xchat.dao.user.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class UserWordDrawDTO {
    /***
     * id
     */
    private Integer userWordDrawId;
    /**
     * 用户id
     */
    private Long uid;
    /**
     * 文字
     */
    private String word;
    /**
     * 活动类型
     */
    private Integer activityType;
    /**
     * 是否已使用
     */
    private Boolean isUse;


}
