package com.erban.main.mybatismapper;

import com.erban.main.model.ClockResultRecord;
import com.erban.main.model.ClockResultRecordExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClockResultRecordMapper {
    int countByExample(ClockResultRecordExample example);

    int deleteByExample(ClockResultRecordExample example);

    int deleteByPrimaryKey(Integer clockResultId);

    int insert(ClockResultRecord record);

    int insertSelective(ClockResultRecord record);

    List<ClockResultRecord> selectByExample(ClockResultRecordExample example);

    ClockResultRecord selectByPrimaryKey(Integer clockResultId);

    int updateByExampleSelective(@Param("record") ClockResultRecord record, @Param("example") ClockResultRecordExample example);

    int updateByExample(@Param("record") ClockResultRecord record, @Param("example") ClockResultRecordExample example);

    int updateByPrimaryKeySelective(ClockResultRecord record);

    int updateByPrimaryKey(ClockResultRecord record);
}
