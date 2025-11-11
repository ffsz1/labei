package com.juxiao.xchat.dao.mcoin.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class McoinDrawPrizeDO {
    /**
     * 主键，当前抽奖期号
     */
    private Long issueId;

    /**
     * 用户ID
     */
    private Long uid;

    /**
     * 用户号码ID
     */
    private Long ticketId;

    /**
     * 创建时间
     */
    private Date createTime;
}
