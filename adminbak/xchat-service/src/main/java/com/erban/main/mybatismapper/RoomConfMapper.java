package com.erban.main.mybatismapper;

import com.erban.main.dto.RoomConfListDTO;
import com.erban.main.model.RoomConf;
import com.erban.main.model.RoomConfExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RoomConfMapper {
    int countByExample(RoomConfExample example);

    int deleteByExample(RoomConfExample example);

    int deleteByPrimaryKey(Long roomUid);

    int insert(RoomConf record);

    int insertSelective(RoomConf record);

    List<RoomConf> selectByExample(RoomConfExample example);

    RoomConf selectByPrimaryKey(Long roomUid);

    int updateByExampleSelective(@Param("record") RoomConf record, @Param("example") RoomConfExample example);

    int updateByExample(@Param("record") RoomConf record, @Param("example") RoomConfExample example);

    int updateByPrimaryKeySelective(RoomConf record);

    int updateByPrimaryKey(RoomConf record);

    void insertOrUpdateSelective(RoomConf roomConf);

    List<RoomConfListDTO> listRoomConf(@Param("searchText") String searchText);
}
