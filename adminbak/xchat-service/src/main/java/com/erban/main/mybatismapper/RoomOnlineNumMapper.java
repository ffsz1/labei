package com.erban.main.mybatismapper;

import com.erban.main.model.RoomOnlineNum;
import com.erban.main.model.RoomOnlineNumExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RoomOnlineNumMapper {
    int countByExample(RoomOnlineNumExample example);

    int deleteByExample(RoomOnlineNumExample example);

    int deleteByPrimaryKey(Long uid);

    int insert(RoomOnlineNum record);

    int insertSelective(RoomOnlineNum record);

    List<RoomOnlineNum> selectByExample(RoomOnlineNumExample example);

    RoomOnlineNum selectByPrimaryKey(Long uid);

    int updateByExampleSelective(@Param("record") RoomOnlineNum record, @Param("example") RoomOnlineNumExample example);

    int updateByExample(@Param("record") RoomOnlineNum record, @Param("example") RoomOnlineNumExample example);

    int updateByPrimaryKeySelective(RoomOnlineNum record);

    int updateByPrimaryKey(RoomOnlineNum record);
}
