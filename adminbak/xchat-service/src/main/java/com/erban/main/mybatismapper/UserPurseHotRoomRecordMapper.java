package com.erban.main.mybatismapper;

import com.erban.main.model.UserPurseHotRoomRecord;
import com.erban.main.model.UserPurseHotRoomRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserPurseHotRoomRecordMapper {
    int countByExample(UserPurseHotRoomRecordExample example);

    int deleteByExample(UserPurseHotRoomRecordExample example);

    int deleteByPrimaryKey(Integer recordId);

    int insert(UserPurseHotRoomRecord record);

    int insertSelective(UserPurseHotRoomRecord record);

    List<UserPurseHotRoomRecord> selectByExample(UserPurseHotRoomRecordExample example);

    UserPurseHotRoomRecord selectByPrimaryKey(Integer recordId);

    int updateByExampleSelective(@Param("record") UserPurseHotRoomRecord record, @Param("example") UserPurseHotRoomRecordExample example);

    int updateByExample(@Param("record") UserPurseHotRoomRecord record, @Param("example") UserPurseHotRoomRecordExample example);

    int updateByPrimaryKeySelective(UserPurseHotRoomRecord record);

    int updateByPrimaryKey(UserPurseHotRoomRecord record);
}
