package com.juxiao.xchat.dao.family.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author chris
 * @Title: 家族申请退出记录
 * @date 2018/11/26
 * @time 09:41
 */
@Data
public class FamilyExitRecordDTO {

    /**
     * 时间
     */
    private Date createTime;
    /**
     * 昵称
     */
    private String nick;
    /**
     * 果果号
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
     * 财富等级
     */
    private Integer level;
}
