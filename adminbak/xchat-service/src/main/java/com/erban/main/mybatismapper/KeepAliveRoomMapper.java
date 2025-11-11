package com.erban.main.mybatismapper;

import com.erban.main.model.KeepAliveRoom;
import com.erban.main.model.KeepAliveRoomExample;
import java.util.List;

public interface KeepAliveRoomMapper {
    int deleteByPrimaryKey(Integer keepId);

    int insert(KeepAliveRoom record);

    int insertSelective(KeepAliveRoom record);

    List<KeepAliveRoom> selectByExample(KeepAliveRoomExample example);

    KeepAliveRoom selectByPrimaryKey(Integer keepId);

    int updateByPrimaryKeySelective(KeepAliveRoom record);

    int updateByPrimaryKey(KeepAliveRoom record);
}
