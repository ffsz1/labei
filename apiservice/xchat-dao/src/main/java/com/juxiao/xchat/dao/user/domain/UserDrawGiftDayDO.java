package com.juxiao.xchat.dao.user.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author chris
 * @Title:
 * @date 2019-06-05
 * @time 09:57
 */
@Data
public class UserDrawGiftDayDO {

    private Long uid;

    private Integer inputNum;

    private Integer outNum;

    private Date createDate;
}
