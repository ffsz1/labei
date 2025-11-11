package com.juxiao.xchat.dao.family.dto;

import lombok.Data;

/**
 * @author chris
 * @Title: 家族成员信息
 * @date 2018/11/26
 * @time 09:37
 */
@Data
public class FamilyJoinsDTO {

    /**
     *  编号
     */
    private Long id;

    /**
     * uid
     */
    private Long uid;

    /**
     * 威望
     */
    private Integer prestige;


    /**
     * 角色状态
     */
    private Integer roleStatus;

    /**
     * 家族logo
     */
    private String familyLogo;

    /**
     * 家族名称
     */
    private String familyName;

    /**
     * 家族公告
     */
    private String familyNotice;

    /**
     * 排名
     */
    private Integer ranking;

    /**
     * 家族ID
     */
    private Long familyId;

    /**
     * 家族房间
     */
    private Long roomId;


    /**
     * 0不需要被邀请人同意加入群
     * 1需要被邀请人同意才可以加入群
     */
    private Integer magree;

    /**
     * 1-禁言
     * 0-解禁
     */
    private Integer mute;

    /**
     * 1：关闭消息提醒
     * 2：打开消息提醒，其他值无效
     */
    private Integer ope;
    /**家族成员*/
    private Integer member;

    /**
     * 验证
     */
    private Integer verification;

    /**
     * 时间戳
     */
    private Long times;

    /**
     * 成员昵称
     */
    private String nick;

    private String bgimg;

}
