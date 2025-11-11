package com.juxiao.xchat.dao.bill.enumeration;

public enum BillRecordType {

    /**
     * 充值
     */
    charge((byte) 1),
    /**
     * 提现
     */
    getCash((byte) 2),
    /**
     * 消费订单支出
     */
    orderPay((byte) 3),
    /**
     * 服务订单收入
     */
    orderIncome((byte) 4),
    /**
     * 刷礼物消费
     */
    giftPay((byte) 5),
    /**
     * 收礼物收入
     */
    giftIncome((byte) 6),
    /**
     * 发个人红包消费
     */
    redPackPay((byte) 7),
    /**
     * 收到个人红包收入
     */
    redPackIncome((byte) 8),
    /**
     * 房主佣金收入
     */
    roomOwnerIncome((byte) 9),
    /**
     * 抽礼物
     */
    drawGift((byte) 10),
    /**
     * 使用礼物
     */
    useGift((byte) 11),
    /**
     * 官方直接送金币
     */
    interSendGold((byte) 12),
    /**
     * 关注公众号送金币
     */
    followSendGold((byte) 13),
    /**
     * 钻石换金币
     */
    exchangeDimondToGoldPay((byte) 14),
    /**
     * 钻石换金币
     */
    exchangeDimondToGoldIncome((byte) 15),
    /**
     * 购买座驾
     */
    purseCar((byte) 16),
    /**
     * 购买推荐位
     */
    purseHot((byte) 17),
    /**
     * 参加打卡活动
     */
    clockAttend((byte) 18),
    /**
     * 打卡活动结算
     */
    clockResult((byte) 19),
    /**
     * 打款至公账充值金币
     */
    chargeByCompanyAccount((byte) 20),
    /**
     * 兑换码兑换金币
     */
    redeemCode((byte) 21),
    /**
     * 抽奖得金币
     */
    draw((byte) 23),
    /**
     * 发送的
     */
    bonusPerDaySend((byte) 24),
    /**
     * 钻石回馈账单
     */
    bonusPerDayRecv((byte) 25),
    /**
     * 开通贵族
     */
    openNoble((byte) 26),
    /**
     * 续费贵族
     */
    renewNoble((byte) 27),
    /**
     * 房间内开通贵族分成
     */
    roomNoble((byte) 28),
    /**
     * 购买头饰
     */
    purseHeadwear((byte) 29),
    /**
     * 问卷抽奖
     */
    questionnaireDraw((byte) 30);

    private byte value;

    BillRecordType(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
