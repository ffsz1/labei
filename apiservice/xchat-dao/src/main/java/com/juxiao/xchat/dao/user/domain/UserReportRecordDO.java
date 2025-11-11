package com.juxiao.xchat.dao.user.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @class: UserReportRecordDO.java
 * @author: chenjunsheng
 * @date 2018/7/23
 */
@Getter
@Setter
public class UserReportRecordDO {
    private Long id;
    private Long informantsId;
    private Long reportUid;
    private String deviceId;
    private String phoneNo;
    private String ip;
    private Integer reportType;
    private Integer type;
    private Date createDate;
    private String remark;
}
