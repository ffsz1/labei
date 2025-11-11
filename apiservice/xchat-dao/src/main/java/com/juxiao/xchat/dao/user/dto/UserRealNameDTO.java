package com.juxiao.xchat.dao.user.dto;

import lombok.Data;

import java.util.Date;

/**
 * 用户实名认证DTO
 *
 * @author chris
 * @date 2019-05-08 15:30
 */
@Data
public class UserRealNameDTO {
    private Long uid;
    private String realName;
    private String idCardNo;
    private String idCardFront;
    private String idCardOpposite;
    private String idCardHandheld;
    private Byte auditStatus;
    private Date createDate;
    private Date updateDate;
    private String phone;
    private String remark;
}
