package com.erban.main.mybatismapper;

import com.erban.main.dto.RoomGameConfigDTO;
import com.erban.main.model.RoomGameConfig;
import com.erban.main.model.RoomGameConfigExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RoomGameConfigMapper {
    int countByExample(RoomGameConfigExample example);

    int deleteByExample(RoomGameConfigExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RoomGameConfig record);

    int insertSelective(RoomGameConfig record);

    List<RoomGameConfig> selectByExample(RoomGameConfigExample example);

    RoomGameConfig selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") RoomGameConfig record, @Param("example") RoomGameConfigExample example);

    int updateByExample(@Param("record") RoomGameConfig record, @Param("example") RoomGameConfigExample example);

    int updateByPrimaryKeySelective(RoomGameConfig record);

    int updateByPrimaryKey(RoomGameConfig record);

    List<RoomGameConfigDTO> selectByPage(@Param("search") String searchText);

    int save(RoomGameConfig roomGameConfig);
}
