package com.juxiao.xchat.dao.family.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author chris
 * @Title: 家族信息
 * @date 2018/11/26
 * @time 09:56
 */
@Data
public class FamilyTeamDO {

    private Long id;

    /**
     * 家族ID
     */
    private Long familyId;

    /**
     * 用户ID
     */
    private Long uid;

    /**
     * logo
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
     * 手上厅，多个以逗号隔开
     */
    private String hall;

    /**
     *  审核(0.未审核,1.审核通过,2.审核失败)
     */
    private Integer status;

    /**
     * 威望
     */
    private Integer prestige;


    /**
     * 背景图
     */
    private String bgImg;


    /**
     * 申请加入验证(0.否1.是)
     */
    private Integer verification;


    /**
     * 创建时间
     */
    private Date createTime;


    /**
     * 更新时间
     */
    private Date updateTime;


    /**
     * 显示/隐藏
     */
    private Integer display;

    /**
     * 房间ID
     */
    private Long roomId;

    /**
     * 禁言类型 0:解除禁言，1:禁言普通成员 3:禁言整个群(包括群主)
     */
    private Integer muteType;

    /**
     * 被邀请人同意方式，0-需要同意(默认),1-不需要同意
     */
    private Integer beinvitemode;

    /**
     * 谁可以邀请他人入群，0-管理员(默认),1-所有人
     */
    private Integer invitemode;

    /**
     * 谁可以修改群资料，0-管理员(默认),1-所有人 备注:只有群主才能修改
     */
    private Integer uptinfomode;
}
