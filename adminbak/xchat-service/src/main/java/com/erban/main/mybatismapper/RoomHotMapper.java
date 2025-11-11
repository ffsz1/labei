package com.erban.main.mybatismapper;

import com.erban.main.model.RoomHot;
import com.erban.main.model.RoomHotExample;
import java.util.List;

public interface RoomHotMapper {
    int deleteByPrimaryKey(Long uid);

    int insert(RoomHot record);

    int insertSelective(RoomHot record);

    List<RoomHot> selectByExample(RoomHotExample example);

    RoomHot selectByPrimaryKey(Long uid);

    int updateByPrimaryKeySelective(RoomHot record);

    int updateByPrimaryKey(RoomHot record);
}
