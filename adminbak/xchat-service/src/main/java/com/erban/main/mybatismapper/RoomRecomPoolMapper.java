package com.erban.main.mybatismapper;

import com.erban.main.model.RoomRecomPool;
import com.erban.main.model.RoomRecomPoolExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RoomRecomPoolMapper {
    int countByExample(RoomRecomPoolExample example);

    int deleteByExample(RoomRecomPoolExample example);

    int deleteByPrimaryKey(Long uid);

    int insert(RoomRecomPool record);

    int insertSelective(RoomRecomPool record);

    List<RoomRecomPool> selectByExample(RoomRecomPoolExample example);

    RoomRecomPool selectByPrimaryKey(Long uid);

    int updateByExampleSelective(@Param("record") RoomRecomPool record, @Param("example") RoomRecomPoolExample example);

    int updateByExample(@Param("record") RoomRecomPool record, @Param("example") RoomRecomPoolExample example);

    int updateByPrimaryKeySelective(RoomRecomPool record);

    int updateByPrimaryKey(RoomRecomPool record);
}
