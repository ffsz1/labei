package com.juxiao.xchat.dao.user.domain;


import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.Date;


@Data
public class UserWordDrawDO {
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

    private Date createTime;

    private Date updateTime;


}
