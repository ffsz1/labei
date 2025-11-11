package com.erban.main.mybatismapper;

import com.erban.main.model.StatBasicRoom;
import com.erban.main.model.StatBasicRoomExample;
import java.util.List;

public interface StatBasicRoomMapper {
    int deleteByPrimaryKey(Long id);

    int insert(StatBasicRoom record);

    int insertSelective(StatBasicRoom record);

    List<StatBasicRoom> selectByExample(StatBasicRoomExample example);

    StatBasicRoom selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StatBasicRoom record);

    int updateByPrimaryKey(StatBasicRoom record);
}
