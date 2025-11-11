package com.erban.main.mybatismapper;

import com.erban.main.model.RoomTag;
import com.erban.main.model.RoomTagExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RoomTagMapper {
    int countByExample(RoomTagExample example);

    List<RoomTag> selectByExample(RoomTagExample example);

    RoomTag selectByPrimaryKey(Integer id);

    int insert(RoomTag record);

    int insertSelective(RoomTag record);

    int updateByExampleSelective(@Param("record") RoomTag record, @Param("example") RoomTagExample example);

    int updateByExample(@Param("record") RoomTag record, @Param("example") RoomTagExample example);

    int updateByPrimaryKeySelective(RoomTag record);

    int updateByPrimaryKey(RoomTag record);

    int deleteByExample(RoomTagExample example);

    int deleteByPrimaryKey(Integer id);
}
