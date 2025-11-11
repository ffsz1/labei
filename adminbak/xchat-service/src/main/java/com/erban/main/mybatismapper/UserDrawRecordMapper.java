package com.erban.main.mybatismapper;

import com.erban.main.model.UserDrawRecord;
import com.erban.main.model.UserDrawRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserDrawRecordMapper {
    int countByExample(UserDrawRecordExample example);

    int deleteByExample(UserDrawRecordExample example);

    int deleteByPrimaryKey(Integer recordId);

    int insert(UserDrawRecord record);

    int insertSelective(UserDrawRecord record);

    List<UserDrawRecord> selectByExample(UserDrawRecordExample example);

    UserDrawRecord selectByPrimaryKey(Integer recordId);

    int updateByExampleSelective(@Param("record") UserDrawRecord record, @Param("example") UserDrawRecordExample example);

    int updateByExample(@Param("record") UserDrawRecord record, @Param("example") UserDrawRecordExample example);

    int updateByPrimaryKeySelective(UserDrawRecord record);

    int updateByPrimaryKey(UserDrawRecord record);
}
