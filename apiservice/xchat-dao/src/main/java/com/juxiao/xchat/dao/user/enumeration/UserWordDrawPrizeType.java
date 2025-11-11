package com.juxiao.xchat.dao.user.enumeration;

/**
 * 用户字体抽奖的及奖品类型
 */
public enum UserWordDrawPrizeType {

    GOLD(0),
    ERBAN_NO(1),
    CAR(2),
    ;


    UserWordDrawPrizeType(int type) {
        this.type = type;
    }
    private int type;

    public int getType() {
        return type;
    }



}
