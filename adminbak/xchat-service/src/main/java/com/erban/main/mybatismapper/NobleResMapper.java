package com.erban.main.mybatismapper;

import com.erban.main.model.NobleRes;
import com.erban.main.model.NobleResExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface NobleResMapper {
    int countByExample(NobleResExample example);

    int deleteByExample(NobleResExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(NobleRes record);

    int insertSelective(NobleRes record);

    List<NobleRes> selectByExample(NobleResExample example);

    NobleRes selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") NobleRes record, @Param("example") NobleResExample example);

    int updateByExample(@Param("record") NobleRes record, @Param("example") NobleResExample example);

    int updateByPrimaryKeySelective(NobleRes record);

    int updateByPrimaryKey(NobleRes record);
}
