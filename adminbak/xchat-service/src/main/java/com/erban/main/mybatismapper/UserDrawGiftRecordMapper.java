package com.erban.main.mybatismapper;

import com.erban.main.model.UserDrawGiftRecord;
import com.erban.main.model.UserDrawGiftRecordExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface UserDrawGiftRecordMapper {
    int countByExample(UserDrawGiftRecordExample example);

    int deleteByExample(UserDrawGiftRecordExample example);

    int deleteByPrimaryKey(Integer recordId);

    int insert(UserDrawGiftRecord record);

    int insertSelective(UserDrawGiftRecord record);

    List<UserDrawGiftRecord> selectByExample(UserDrawGiftRecordExample example);

    UserDrawGiftRecord selectByPrimaryKey(Integer recordId);

    int updateByExampleSelective(@Param("record") UserDrawGiftRecord record, @Param("example") UserDrawGiftRecordExample example);

    int updateByExample(@Param("record") UserDrawGiftRecord record, @Param("example") UserDrawGiftRecordExample example);

    int updateByPrimaryKeySelective(UserDrawGiftRecord record);

    int updateByPrimaryKey(UserDrawGiftRecord record);

    Integer selectUserDrawGiftRecord(Map<String, Object> map);

    Integer selectUserDrawWhitelistGiftRecord(Map<String, Object> map);
}
