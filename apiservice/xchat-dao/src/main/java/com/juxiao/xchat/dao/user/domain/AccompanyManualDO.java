package com.juxiao.xchat.dao.user.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author chris
 * @Title:
 * @date 2018/11/14
 * @time 16:00
 */
@Data
public class AccompanyManualDO {

    private Integer id;

    private Long uid;

    private Integer type;

    private Integer status;

    private Integer seqNo;

    private Integer isDisplay;

    private Date startTime;

    private Date endTime;

    private Date createTime;


}
