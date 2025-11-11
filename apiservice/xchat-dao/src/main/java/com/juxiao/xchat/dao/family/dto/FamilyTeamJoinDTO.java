package com.juxiao.xchat.dao.family.dto;

import lombok.Data;

/**
 * @author chris
 * @Title:
 * @date 2018/11/26
 * @time 09:46
 */
@Data
public class FamilyTeamJoinDTO {

    private Long id;

    /**
     * uid
     */
    private Long uid;

    /**
     * 果果号
     */
    private Long erbanNo;

    /**
     * 昵称
     */
    private String nike;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 级别
     */
    private Integer level;

    /**
     * 威望
     */
    private Integer prestige;

    /**
     * 个性签名
     */
    private String userDesc;

    /**
     * 角色状态
     */
    private Integer roleStatus;

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
