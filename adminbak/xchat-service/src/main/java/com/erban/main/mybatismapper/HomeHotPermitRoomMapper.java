package com.erban.main.mybatismapper;

import com.erban.main.model.HomeHotPermitRoom;
import com.erban.main.model.HomeHotPermitRoomExample;
import java.util.List;

public interface HomeHotPermitRoomMapper {
    int deleteByExample(HomeHotPermitRoomExample example);

    int deleteByPrimaryKey(Integer permitId);

    int insert(HomeHotPermitRoom record);

    int insertSelective(HomeHotPermitRoom record);

    List<HomeHotPermitRoom> selectByExample(HomeHotPermitRoomExample example);

    HomeHotPermitRoom selectByPrimaryKey(Integer permitId);

    int updateByPrimaryKeySelective(HomeHotPermitRoom record);

    int updateByPrimaryKey(HomeHotPermitRoom record);
}
