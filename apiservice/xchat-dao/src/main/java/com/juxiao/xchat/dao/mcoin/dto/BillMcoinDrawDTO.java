package com.juxiao.xchat.dao.mcoin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillMcoinDrawDTO {
    /**
     * 主键
     */
    private Long id;
    /**
     * 用户ID
     */
    private Long uid;
    /**
     * 期号
     */
    private Long issueId;
    /**
     * 购买人次
     */
    private Integer drawCount;
    /**
     * 本次消耗萌币数量
     */
    private Integer mcoinCost;
    /**
     * 创建时间
     */
    private Date createTime;


}
