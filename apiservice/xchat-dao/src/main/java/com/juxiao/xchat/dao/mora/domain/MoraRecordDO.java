package com.juxiao.xchat.dao.mora.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author chris
 * @Title: 猜拳记录
 * @date 2019-06-01
 * @time 22:52
 */
@Data
public class MoraRecordDO {

    private Integer id;

    /**
     * 房间ID
     */
    private Long roomId;

    /**
     * uid
     */
    private Long uid;

    /**
     * 房主uid
     */
    private Long roomUid;

    /**
     * 类型(1.发起 2.参与)
     */
    private Integer type;

    /**
     * 概率(1.高 2.中 3.低)
     */
    private Integer probability;

    /**
     * 总金币
     */
    private Long total;

    /**
     * 礼物id
     */
    private Integer giftId;

    /**
     * 数量
     */
    private Integer num;

    /**
     * 选择(1.剪刀 2.石头 3.布)
     */
    private Integer choose;

    /**
     * 是否有效(1.有效 2.无效)
     */
    private Integer isValid;

    /**
     * 记录id(主键ID)
     */
    private Integer refId;

    /**
     * 是否返回金币(1.是 2.否)
     */
    private Integer isReturnGold;

    /**
     * 是否结束 (1.是 2.否)
     */
    private Integer isFinish;

    /**
     * 截止日期
     */
    private String expirationDate;

    /**
     * 结果 (1.赢 2.输 3.平)
     */
    private Integer result;

    /**
     * 创建时间
     */
    private Date createTime;


}
