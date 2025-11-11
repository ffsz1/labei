package com.erban.main.mybatismapper;

import com.erban.main.model.UserPacket;
import com.erban.main.model.UserPacketExample;
import java.util.List;

public interface UserPacketMapper {
    int deleteByPrimaryKey(Long uid);

    int insert(UserPacket record);

    int insertSelective(UserPacket record);

    List<UserPacket> selectByExample(UserPacketExample example);

    UserPacket selectByPrimaryKey(Long uid);

    int updateByPrimaryKeySelective(UserPacket record);

    int updateByPrimaryKey(UserPacket record);
}
