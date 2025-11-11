package com.juxiao.xchat.dao.mcoin.dto;

import lombok.Data;

import java.util.Date;

@Data
public class McoinDrawIssueDTO {
    /**
     * 主键，用作期号
     */
    private Long id;
    /**
     * 抽奖商品显示图片
     */
    private String issueUrl;
    /**
     * 抽奖商品名称
     */
    private String itemName;
    /**
     * 抽奖类型：1，靓号；2，座驾；3，头饰
     */
    private Byte itemType;
    /**
     * 对应的礼品ID
     */
    private Integer itemId;
    /**
     * 不入库，冗余字段
     */
    private int leftCount;
    /**
     * 抽奖人次
     */
    private Integer drawNum;
    /**
     * 单次抽奖价格
     */
    private Integer price;
    /**
     * 状态，0，删除；1：无效；2：有效
     */
    private Byte issueStatus;
    /**
     * 排序，0为置顶，100为置低
     */
    private Integer seq;
    /**
     * 抽奖开始时间
     */
    private Date startTime;
    /**
     * 抽奖结束时间（即开奖时间）
     */
    private Date endTime;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}
