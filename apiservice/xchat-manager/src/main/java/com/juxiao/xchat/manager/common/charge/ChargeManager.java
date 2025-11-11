package com.juxiao.xchat.manager.common.charge;

public interface ChargeManager {

    /**
     * 统计用户充值信息
     *
     * @param uid
     * @param amount
     */
    void sumUserCharge(Long uid, Integer amount);
}
