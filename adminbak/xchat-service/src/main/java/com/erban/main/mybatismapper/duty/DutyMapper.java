package com.erban.main.mybatismapper.duty;

import com.erban.main.service.duty.dto.DutyDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface DutyMapper {


    @Select("SELECT id AS id,duty_name AS dutyName,gold_amount AS goldAmount, duty_type AS dutyType, duty_status AS dutyStatus FROM duty WHERE id = #{dutyId}")
    DutyDTO getDuty(@Param("dutyId") Integer dutyId);
}
