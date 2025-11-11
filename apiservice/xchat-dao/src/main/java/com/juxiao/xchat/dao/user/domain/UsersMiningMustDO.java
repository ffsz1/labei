package com.juxiao.xchat.dao.user.domain;

import lombok.Data;

import java.util.Date;

/**
 * 指定用户必出全服
 */
@Data
public class UsersMiningMustDO {

    private Long id;
    private Long uid;
    private Integer status;
    private Date createTime;
    private Integer giftId;
    private Integer num;
}