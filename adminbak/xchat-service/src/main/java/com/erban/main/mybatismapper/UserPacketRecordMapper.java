package com.erban.main.mybatismapper;

import com.erban.main.model.UserPacketRecord;
import com.erban.main.model.UserPacketRecordExample;
import java.util.List;

public interface UserPacketRecordMapper {
    int deleteByPrimaryKey(String packetId);

    int insert(UserPacketRecord record);

    int insertSelective(UserPacketRecord record);

    List<UserPacketRecord> selectByExample(UserPacketRecordExample example);

    UserPacketRecord selectByPrimaryKey(String packetId);

    int updateByPrimaryKeySelective(UserPacketRecord record);

    int updateByPrimaryKey(UserPacketRecord record);
}
