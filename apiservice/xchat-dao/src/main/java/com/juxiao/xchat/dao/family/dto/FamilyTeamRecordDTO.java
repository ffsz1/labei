package com.juxiao.xchat.dao.family.dto;

import lombok.Data;

/**
 * @author chris
 * @Title: 家族流水
 * @date 2018/11/26
 * @time 09:48
 */
@Data
public class FamilyTeamRecordDTO {

    /**
     * 编号
     */
    private Long id;

    /**
     * 家族ID
     */
    private Long teamId;

    /**
     * 家族威望总数
     */
    private Integer num;

    /**
     * 月份-月-日
     */
    private String years;
}
