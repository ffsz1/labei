package com.erban.main.mybatismapper.duty;

import com.erban.main.mybatismapper.duty.domain.DutyDailyRecord;
import com.erban.main.service.duty.dto.DutyResultDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.access.method.P;

import java.util.List;

/**
 * @class: DutyDailyRecordMapper.java
 * @author: chenjunsheng
 * @date 2018/8/8
 */
public interface DutyDailyRecordMapper {

    /**
     * @param record
     */
    void save(DutyDailyRecord record);

    void update(DutyDailyRecord record);

    /**
     * 获取用户新手任务
     *
     * @param uid
     * @param dutyId
     * @return
     */
    DutyDailyRecord getFreshDuty(@Param("uid") Long uid, @Param("dutyId") Integer dutyId);

    /**
     * @param uid
     * @param dutyId
     * @return
     */
    DutyDailyRecord getDailyDuty(@Param("uid") Long uid, @Param("dutyId") Integer dutyId);

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
