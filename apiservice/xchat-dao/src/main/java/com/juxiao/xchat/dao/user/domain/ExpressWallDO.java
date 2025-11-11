package com.juxiao.xchat.dao.user.domain;

import lombok.Data;

import java.util.Date;

/**
 * @Auther: alwyn
 * @Description: 表白墙
 * @Date: 2018/10/8 15:49
 */
@Data
public class ExpressWallDO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 发送礼物的用户
     */
    private Long sendUid;

    /**
     * 礼物接收人
     */
    private Long receiveUid;

    /**
     * 礼物
     */
    private Integer giftId;

    /**
     * 礼物数量
     */
    private Integer giftNum;

    /**
     * 礼物总金额
     */
    private Long totalGold;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 表白留言
     */
    private String message;
}
