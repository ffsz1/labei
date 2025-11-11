package com.juxiao.xchat.dao.family.dto;

import lombok.Data;

/**
 * @author chris
 * @Title:
 * @date 2018/11/26
 * @time 09:49
 */
@Data
public class FamilyTeamUsersDTO {

    /**
     * UID
     */
    private Long uid;

    /**
     * 官方号
     */
    private Long erbanNo;

    /**
     *  手机
     */
    private String phone;

    /**
     * 昵称
     */
    private String nick;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 家族ID
     */
    private Long familyId;

    /**
     * 家族名称
     */
    private String familyName;
}
