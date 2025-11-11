package com.erban.main.mybatismapper;

import com.erban.main.model.ClockAttendRecord;
import com.erban.main.model.ClockAttendRecordExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClockAttendRecordMapper {
    int countByExample(ClockAttendRecordExample example);

    int deleteByExample(ClockAttendRecordExample example);

    int deleteByPrimaryKey(Integer clockRecordId);

    int insert(ClockAttendRecord record);

    int insertSelective(ClockAttendRecord record);

    List<ClockAttendRecord> selectByExample(ClockAttendRecordExample example);

    ClockAttendRecord selectByPrimaryKey(Integer clockRecordId);

    int updateByExampleSelective(@Param("record") ClockAttendRecord record, @Param("example") ClockAttendRecordExample example);

    int updateByExample(@Param("record") ClockAttendRecord record, @Param("example") ClockAttendRecordExample example);

    int updateByPrimaryKeySelective(ClockAttendRecord record);

    int updateByPrimaryKey(ClockAttendRecord record);
}
