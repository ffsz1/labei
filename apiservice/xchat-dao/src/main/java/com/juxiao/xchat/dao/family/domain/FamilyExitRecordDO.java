package com.juxiao.xchat.dao.family.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author chris
 * @Title: 申请退出记录
 * @date 2018/11/26
 * @time 09:53
 */
@Data
public class FamilyExitRecordDO {

    /**
     * 编号
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long uid;

    /**
     * 家族ID
     */
    private Long teamId;

    /**
     * 审核状态(0.审核中,1.审核成功,2.审核失败:注<审核失败7天自动退出/>)
     */
    private Integer status;

    /**
     * 类型(1.申请退出2.逐出)
     */
    private Integer type;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 家族ID
     */
    private Long familyId;

    /**
     * 家族云信群tid
     */
    private Long roomId;
}
