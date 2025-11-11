package com.erban.main.mybatismapper;

import com.erban.main.model.HomeBlackRoom;
import com.erban.main.model.HomeBlackRoomExample;
import java.util.List;

public interface HomeBlackRoomMapper {
    int deleteByPrimaryKey(Integer blackId);

    int insert(HomeBlackRoom record);

    int insertSelective(HomeBlackRoom record);

    List<HomeBlackRoom> selectByExample(HomeBlackRoomExample example);

    HomeBlackRoom selectByPrimaryKey(Integer blackId);

    int updateByPrimaryKeySelective(HomeBlackRoom record);

    int updateByPrimaryKey(HomeBlackRoom record);
}
