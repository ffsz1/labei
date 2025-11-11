package com.erban.main.mybatismapper;

import com.erban.main.model.LevelCharm;
import com.erban.main.model.LevelCharmExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LevelCharmMapper {
    int countByExample(LevelCharmExample example);

    int deleteByExample(LevelCharmExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(LevelCharm record);

    int insertSelective(LevelCharm record);

    List<LevelCharm> selectByExample(LevelCharmExample example);

    LevelCharm selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") LevelCharm record, @Param("example") LevelCharmExample example);

    int updateByExample(@Param("record") LevelCharm record, @Param("example") LevelCharmExample example);

    int updateByPrimaryKeySelective(LevelCharm record);

    int updateByPrimaryKey(LevelCharm record);
}
