package com.juxiao.xchat.service.task.warning;

public interface WarningService {
    /**
     * 每小时查询充值数据
     */
    void checkHourCharge();

    void checkIosCharge();

    void checkDayCharge();

    void checkSendGift();

    void checkRecvGift();

    void checkTomcat();
}
