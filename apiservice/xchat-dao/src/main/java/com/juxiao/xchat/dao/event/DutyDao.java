package com.juxiao.xchat.dao.event;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.event.dto.DutyDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface DutyDao {
    /**
     * 查询任务列表
     *
     * @param dutyId
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT id AS id,duty_name AS dutyName,gold_amount AS goldAmount, duty_type AS dutyType, duty_status AS dutyStatus FROM duty WHERE id = #{dutyId}")
    DutyDTO getDuty(@Param("dutyId") Integer dutyId);
}
