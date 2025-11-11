package com.juxiao.xchat.service.api.play.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author chris
 * @Title: 猜拳记录
 * @date 2019-06-01
 * @time 22:32
 */
@Data
public class MoraRecordVO {

    private String subject;

    private Integer giftId;

    private String giftName;

    private String giftUrl;

    private Integer num;

    private Date createTime;

    private String nick;

    private Long erbanNo;

    private Long uid;

    private String avatar;
}
