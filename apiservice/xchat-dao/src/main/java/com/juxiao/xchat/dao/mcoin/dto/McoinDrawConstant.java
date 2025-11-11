package com.juxiao.xchat.dao.mcoin.dto;

public interface McoinDrawConstant {
    /**
     * 抽奖删除状态
     */
    byte ISSUE_STATUS_DELETE = 0;
    /**
     * 抽奖无效状态
     */
    byte ISSUE_STATUS_UNEFFECT = 1;
    /**
     * 抽奖有效状态
     */
    byte ISSUE_STATUS_EFFECT = 2;
    /**
     * 抽奖有效状态
     */
    byte ISSUE_STATUS_FINISH = 3;
    /**
     * 1，靓号；
     */
    byte ISSUE_ITEM_TYPE_PRETTY_NO = 1;
    /**
     * 2，座驾；
     */
    byte ISSUE_ITEM_TYPE_GIFT_CAR = 2;
    /**
     * 3，头饰
     */
    byte ISSUE_ITEM_TYPE_HEADWEAR = 3;
}
