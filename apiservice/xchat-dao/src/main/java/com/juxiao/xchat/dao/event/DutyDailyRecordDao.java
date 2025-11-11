package com.juxiao.xchat.dao.event;

import com.juxiao.xchat.dao.event.domain.DutyDailyRecordDO;
import com.juxiao.xchat.dao.event.dto.DutyDailyRecordDTO;
import com.juxiao.xchat.dao.event.dto.DutyResultDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @class: DutyDailyRecordDao.java
 * @author: chenjunsheng
 * @date 2018/8/8
 */
public interface DutyDailyRecordDao {

    /**
     * @param record
     */
    void save(DutyDailyRecordDO record);

    void update(DutyDailyRecordDO record);

    /**
     * 获取用户新手任务
     *
     * @param uid
     * @param dutyId
     * @return
     */
    DutyDailyRecordDTO getFreshDuty(@Param("uid") Long uid, @Param("dutyId") Integer dutyId);

    /**
     * @param uid
     * @param dutyId
     * @return
     */
    DutyDailyRecordDTO getDailyDuty(@Param("uid") Long uid, @Param("dutyId") Integer dutyId);

    /**
     * 查询新手任务
     *
     * @param uid
     * @return
     */
    List<DutyResultDTO> listFreshDuties(@Param("uid") Long uid);

    /**
     * 查询当天用户的完成情况
     *
     * @param uid
     * @return
     */
    List<DutyResultDTO> listDailyDuties(@Param("uid") Long uid);

    List<DutyResultDTO> listDailyTime(@Param("uid") Long uid);
}
