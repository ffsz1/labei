package com.juxiao.xchat.service.api.play.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author chris
 * @date 2019-06-19
 */
@Data
public class MoraVO {

    private Long erbanNo;

    private Long uid;

    private String nick;

    private String avatar;

    private Integer experienceLevel;

    private Integer charmLevel;

    private String subject;

    private Integer recordId;

    private Integer giftId;

    private String giftName;

    private String giftUrl;

    private String giftNum;

    private Date createTime;

}
