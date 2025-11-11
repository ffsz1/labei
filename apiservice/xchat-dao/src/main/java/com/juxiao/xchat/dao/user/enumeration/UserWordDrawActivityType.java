package com.juxiao.xchat.dao.user.enumeration;

/**
 * 用户字体抽奖活动类型
 */
public enum UserWordDrawActivityType {
    NIU_DAN(1),  //扭蛋中大奖
    ;


    UserWordDrawActivityType(int type) {
        this.type = type;
    }
    private int type;

    public int getType() {
        return type;
    }
}
