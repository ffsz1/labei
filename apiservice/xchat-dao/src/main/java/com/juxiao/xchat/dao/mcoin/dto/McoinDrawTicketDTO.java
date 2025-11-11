package com.juxiao.xchat.dao.mcoin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class McoinDrawTicketDTO {
    /**
     * 主键
     */
    private Long id;
    /**
     * 生成的抽奖号码
     */
    private String ticketNo;
    /**
     * 生成期号ID
     */
    private Long issueId;
    /**
     * 记录ID
     */
    private Long recordId;
    /**
     * 用户ID
     */
    private Long uid;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

}
