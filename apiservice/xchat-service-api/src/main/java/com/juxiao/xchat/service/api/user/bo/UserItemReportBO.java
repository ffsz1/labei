package com.juxiao.xchat.service.api.user.bo;

import lombok.Data;

/**
 * @author chris
 * @date 2019-06-30
 */
@Data
public class UserItemReportBO {

    /**
     * 被举报的用户
     */
    private Long uid;
    /**
     * 举报的用户
     */
    private Long reportUid;
    /**
     * 举报类型 1.政治 2.色情 3.广告 4.人身攻击
     */
    private Integer reportType;
    /**
     * 类型 1. 用户 2. 房间
     */
    private Integer type;
    /**
     * 设备号
     */
    private String deviceId;
    /**
     * 手机号
     */
    private String phoneNo;
    /**
     * 用户Ip
     */
    private String ip;
    /**
     * url
     */
    private String url;
}
