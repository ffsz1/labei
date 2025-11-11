package com.juxiao.xchat.dao.family.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author chris
 * @Title: 家族信息
 * @date 2018/11/26
 * @time 09:36
 */
@Data
public class FamilyDTO {

    @JsonIgnore
    private Long id;

    /**
     * 家族ID
     */
    private Long familyId;

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
     * 家族威望
     */
    private Integer prestige;

    /**
     * 家族人数
     */
    private Integer member;

    /**
     * 时间戳
     */
    private Long times;

    /**
     * 排名
     */
    private Integer ranking;

    /**
     * 云信群tid
     */
    private Long roomId;

    /**
     * 验证
     */
    private Integer verification;

    /**
     * 更新时间
     */
    private Date updateTime;

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

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 族长uid
     */
    private Long uid;

    /**
     * 族长昵称
     */
    private String nick;

    private List<FamilyUsersDTO> familyUsersDTOS;

    private String bgimg;

    private Integer roleStatus;
}
