package com.juxiao.xchat.dao.item.enumeration;

public enum CarGetType {

    user_buy(1),        //用户购买
    sys_give(2),        //官方赠送
    diamond_excharge(3),    // 钻石兑换抽座驾
    first_charge(4),    // 首充
    user_give(5),       // 用户赠送
    box_draw(7),        // 礼盒
    user_draw(8),       // 用户不知道什么抽奖
    integral_exchange(9),// 积分兑换
    ;

    private int value;

    CarGetType(int value) {
        this.value = value;
    }

    public boolean compareTo(int value) {
        return value == this.value;
    }

    public int getValue() {
        return value;
    }

}
