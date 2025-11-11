package com.juxiao.xchat.manager.common.event;

import com.juxiao.xchat.dao.event.dto.DutyDailyRecordDTO;

/**
 * 每日任务公共接口
 *
 * @class: DutyManager.java
 * @author: chenjunsheng
 * @date 2018/8/16
 */
public interface DutyManager {

//    /**
//     * 更新新手任务
//     *
//     * @param uid
//     * @param dutyId
//     */
//    void updateFreshFinish(Long uid, int dutyId);

    /**
     * 每日任务
     *
     * @param uid
     * @param dutyId
     */
    void updateDailyFinish(Long uid, int dutyId);

    /**
     * 每日在线任务
     *
     * @param uid
     * @param dutyId
     */
    void updateDailytimeFinish(Long uid, int dutyId);

    /**
     * @param uid
     * @param dutyId
     * @return
     */
    DutyDailyRecordDTO getDailyRecord(Long uid, int dutyId);
}
