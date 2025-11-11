package com.juxiao.xchat.dao.family.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author chris
 * @Title:
 * @date 2018/11/26
 * @time 09:44
 */
@Data
public class FamilyRecordDTO {

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 昵称
     */
    private String nick;

    /**
     * 官方号
     */
    private Long erbanNo;

    /**
     * UID
     */
    private Long uid;

    /**
     * 魅力等级
     */
    private Integer charm;

    /**
     * 等级
     */
    private Integer level;

    /**
     * 类型(1.加入 ,退出)
     */
    private Integer type;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 审核状态(0.审核中,1.审核成功,2.审核失败(审核失败7天自动退出）3.处理自动退出成功)
     */
    private Integer status;
}
