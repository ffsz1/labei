package com.juxiao.xchat.dao.family.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author chris
 * @Title: 封装返回家族成员信息
 * @date 2018/11/26
 * @time 09:43
 */
@Data
public class FamilyJoinDTO {

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
     * 角色(1.族长2.管理员3.普通成员)
     */
    private Integer roleStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /** 0不需要被邀请人同意加入群，1需要被邀请人同意才可以加入群*/
    private Integer magree;

    /**
     * 1-禁言，0-解禁
     */
    private Integer mute;

    /**
     * 1：关闭消息提醒，2：打开消息提醒，其他值无效
     */
    private Integer ope;

}
