package com.erban.main.mybatismapper;

import com.erban.main.model.AppSystem;
import com.erban.main.model.AppSystemExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppSystemMapper {
    int countByExample(AppSystemExample example);

    int deleteByExample(AppSystemExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AppSystem record);

    int insertSelective(AppSystem record);

    List<AppSystem> selectByExample(AppSystemExample example);

    AppSystem selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AppSystem record, @Param("example") AppSystemExample example);

    int updateByExample(@Param("record") AppSystem record, @Param("example") AppSystemExample example);

    int updateByPrimaryKeySelective(AppSystem record);

    int updateByPrimaryKey(AppSystem record);
}
