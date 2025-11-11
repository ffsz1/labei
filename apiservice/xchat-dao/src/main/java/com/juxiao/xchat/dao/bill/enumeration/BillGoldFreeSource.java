package com.juxiao.xchat.dao.bill.enumeration;

/**
 * 免费送金币记录
 *
 * @class: BillGoldFreeSource.java
 * @author: chenjunsheng
 * @date 2018/5/21
 */
public enum BillGoldFreeSource {
    OFFICIAL_FREE(1, "官方直接送"),

    LINK_WECHAT_ACCOUNT(2, "关注公众号送"),

    EVENT_REWARD(3, "活动奖励金币"),

    TRANSFER_CHARGE(4, "打款账户充值金币");

    private byte source;

    private String desc;

    BillGoldFreeSource(int source, String desc) {
        this.source = (byte) source;
        this.desc = desc;
    }

    public byte getSource() {
        return source;
    }

    public String getDesc() {
        return desc;
    }
}