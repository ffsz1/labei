package com.erban.main.mybatismapper;

import com.erban.main.model.MsgPushRecord;
import com.erban.main.model.MsgPushRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MsgPushRecordMapper {
    int countByExample(MsgPushRecordExample example);

    int deleteByExample(MsgPushRecordExample example);

    int deleteByPrimaryKey(Integer recordId);

    int insert(MsgPushRecord record);

    int insertSelective(MsgPushRecord record);

    List<MsgPushRecord> selectByExample(MsgPushRecordExample example);

    MsgPushRecord selectByPrimaryKey(Integer recordId);

    int updateByExampleSelective(@Param("record") MsgPushRecord record, @Param("example") MsgPushRecordExample example);

    int updateByExample(@Param("record") MsgPushRecord record, @Param("example") MsgPushRecordExample example);

    int updateByPrimaryKeySelective(MsgPushRecord record);

    int updateByPrimaryKey(MsgPushRecord record);
}
