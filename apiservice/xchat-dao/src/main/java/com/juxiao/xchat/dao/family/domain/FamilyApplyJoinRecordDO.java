package com.juxiao.xchat.dao.family.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author chris
 * @Title: 申请加入记录
 * @date 2018/11/26
 * @time 09:52
 */
@Data
public class FamilyApplyJoinRecordDO {

    /**
     * 编号
     */
    private Long id;

    /**
     * 家族ID
     */
    private Long teamId;

    /**
     * 用户ID
     */
    private Long uid;

    /**
     * 审核状态(0.审核中 1.审核成功 2.审核失败)
     */
    private Integer status;

    /**
     * 申请时间
     */
    private Date createTime;

    /**
     * 审核时间
     */
    private Date updateTime;
}
