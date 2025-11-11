package com.juxiao.xchat.service.api.family.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author chris
 * @Title:
 * @date 2018/10/17
 * @time 11:23
 */
@Data
public class FamilyInfoDTO {

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
     * 创建时间
     */
    private Date createTime;
}
