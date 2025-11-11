package com.juxiao.xchat.service.task.room;

import com.juxiao.xchat.base.web.WebServiceException;

public interface GiftDrawService {

    void refreshRank(Integer type) throws WebServiceException;

    /**
     * 删除捡海螺爆出活动礼物的数量
     */
    void deleteDrawGiftNum();

    /**
     * 统计当天的捡海螺流水
     */
    void countGiftDayDraw();

    /**
     * 统计用户当天的捡海螺流水
     */
    void countUserDayDraw();
}
