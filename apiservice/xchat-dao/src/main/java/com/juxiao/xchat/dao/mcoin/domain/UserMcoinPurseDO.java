package com.juxiao.xchat.dao.mcoin.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserMcoinPurseDO {
    /**
     * 用户ID
     */
    private Long uid;
    /**
     * 萌币数量
     */
    private Integer mcoinNum;
    /**
     * 用户钱包状态：1，正常；2，冻结
     */
    private Byte purseStatus;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}
