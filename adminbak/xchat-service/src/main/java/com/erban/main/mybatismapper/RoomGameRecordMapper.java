package com.erban.main.mybatismapper;

import com.erban.main.model.RoomGameRecord;
import com.erban.main.model.RoomGameRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RoomGameRecordMapper {
    int countByExample(RoomGameRecordExample example);

    int deleteByExample(RoomGameRecordExample example);

    int deleteByPrimaryKey(Integer recordId);

    int insert(RoomGameRecord record);

    int insertSelective(RoomGameRecord record);

    List<RoomGameRecord> selectByExample(RoomGameRecordExample example);

    RoomGameRecord selectByPrimaryKey(Integer recordId);

    int updateByExampleSelective(@Param("record") RoomGameRecord record, @Param("example") RoomGameRecordExample example);

    int updateByExample(@Param("record") RoomGameRecord record, @Param("example") RoomGameRecordExample example);

    int updateByPrimaryKeySelective(RoomGameRecord record);

    int updateByPrimaryKey(RoomGameRecord record);
}
