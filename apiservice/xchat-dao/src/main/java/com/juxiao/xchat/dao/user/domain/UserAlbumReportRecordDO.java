package com.juxiao.xchat.dao.user.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 用户相册举报
 */
@Getter
@Setter
public class UserAlbumReportRecordDO {
    private Long id;
    private Long uid;
    private Long reportUid;
    private Integer reportType;
    private Integer type;
    private String deviceId;
    private String phoneNo;
    private String ip;
    private String url;
    private Date createDate;
}
