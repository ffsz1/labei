package com.erban.main.mybatismapper;

import com.erban.main.model.ActivityConfig;
import com.erban.main.model.ActivityConfigExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActivityConfigMapper {
    int countByExample(ActivityConfigExample example);

    int deleteByExample(ActivityConfigExample example);

    int insert(ActivityConfig record);

    int insertSelective(ActivityConfig record);

    List<ActivityConfig> selectByExample(ActivityConfigExample example);

    int updateByExampleSelective(@Param("record") ActivityConfig record, @Param("example") ActivityConfigExample example);

    int updateByExample(@Param("record") ActivityConfig record, @Param("example") ActivityConfigExample example);
}
