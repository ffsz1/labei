package com.juxiao.xchat.dao.family.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author chris
 * @Title: 家族流水记录
 * @date 2018/11/26
 * @time 09:57
 */
@Data
public class FamilyTeamRecordDO {


    /**
     * 编号
     */
    private Long id;

    /**
     * 家族ID
     */
    private Long teamId;

    /**
     * 威望总数
     */
    private Integer num;

    /**
     * 时间 月-日
     */
    private String years;

    /**
     * 记录时间
     */
    private Date createTime;
}
