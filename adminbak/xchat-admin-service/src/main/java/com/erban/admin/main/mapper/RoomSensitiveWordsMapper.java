package com.erban.admin.main.mapper;

import com.erban.admin.main.model.RoomSensitiveWords;
import com.erban.admin.main.model.RoomSensitiveWordsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RoomSensitiveWordsMapper {
    int countByExample(RoomSensitiveWordsExample example);

    int deleteByExample(RoomSensitiveWordsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RoomSensitiveWords record);

    int insertSelective(RoomSensitiveWords record);

    List<RoomSensitiveWords> selectByExample(RoomSensitiveWordsExample example);

    RoomSensitiveWords selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") RoomSensitiveWords record, @Param("example") RoomSensitiveWordsExample example);

    int updateByExample(@Param("record") RoomSensitiveWords record, @Param("example") RoomSensitiveWordsExample example);

    int updateByPrimaryKeySelective(RoomSensitiveWords record);

    int updateByPrimaryKey(RoomSensitiveWords record);
}
