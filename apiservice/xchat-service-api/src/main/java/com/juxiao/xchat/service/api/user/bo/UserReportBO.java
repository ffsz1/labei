package com.juxiao.xchat.service.api.user.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户举报BO
 */
@Getter
@Setter
public class UserReportBO {
    /**
     * 被举报用户UID
     */
    private Long informantsId;
    /**
     * 举报用户的UID
     */
    private Long reportUid;
    /**
     * 举报用户的设备ID
     */
    private String deviceId;
    /**
     * 举报用户的手机号
     */
    private String phoneNo;
    /**
     * 举报用户的IP地址
     */
    private String ip;
    /**
     * 举报类型 []
     */
    private Integer reportType;
    /**
     * 类型 [1.用户; 2.房间]
     */
    private Integer type;
    /**
     * 其他原因
     */
    private String remark;
}
