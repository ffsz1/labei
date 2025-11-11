package com.juxiao.xchat.dao.item.enumeration;

/**
 * 头饰获取类型
 * 获得类型 1 购买 2 官方赠送 4 新手礼包 5 用户赠送 6 卡片兑换
 */
public enum HeadwearGetType {
    user_buy(1),  //用户购买
    sys_give(2),  //官方赠送
    first_charge(4),// 首充
    user_give(5),// 用户赠送
    card_exchange(6),// 卡片兑换
    box_draw(7),// 礼盒
    integral_exchange(8),// 积分兑换
    ;

    private int value;

    HeadwearGetType(int value) {
        this.value = value;
    }

    public boolean compareTo(int value) {
        return value == this.value;
    }

    public int getValue() {
        return value;
    }
}
