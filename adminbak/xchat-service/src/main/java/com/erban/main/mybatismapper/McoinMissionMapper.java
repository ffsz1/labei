package com.erban.main.mybatismapper;

import com.erban.main.model.McoinMission;
import com.erban.main.model.McoinMissionExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface McoinMissionMapper {
    int countByExample(McoinMissionExample example);

    int deleteByExample(McoinMissionExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(McoinMission record);

    int insertSelective(McoinMission record);

    List<McoinMission> selectByExample(McoinMissionExample example);

    McoinMission selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") McoinMission record, @Param("example") McoinMissionExample example);

    int updateByExample(@Param("record") McoinMission record, @Param("example") McoinMissionExample example);

    int updateByPrimaryKeySelective(McoinMission record);

    int updateByPrimaryKey(McoinMission record);
}
