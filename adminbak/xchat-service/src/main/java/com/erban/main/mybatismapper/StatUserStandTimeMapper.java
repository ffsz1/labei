package com.erban.main.mybatismapper;

import com.erban.main.model.StatUserStandTime;
import com.erban.main.model.StatUserStandTimeExample;
import java.util.List;

public interface StatUserStandTimeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(StatUserStandTime record);

    int insertSelective(StatUserStandTime record);

    List<StatUserStandTime> selectByExample(StatUserStandTimeExample example);

    StatUserStandTime selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StatUserStandTime record);

    int updateByPrimaryKey(StatUserStandTime record);
}
