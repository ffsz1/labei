package com.erban.main.mybatismapper;

import com.erban.main.model.AppChannel;
import com.erban.main.model.AppChannelExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppChannelMapper {
    int countByExample(AppChannelExample example);

    int deleteByExample(AppChannelExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AppChannel record);

    int insertSelective(AppChannel record);

    List<AppChannel> selectByExample(AppChannelExample example);

    AppChannel selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AppChannel record, @Param("example") AppChannelExample example);

    int updateByExample(@Param("record") AppChannel record, @Param("example") AppChannelExample example);

    int updateByPrimaryKeySelective(AppChannel record);

    int updateByPrimaryKey(AppChannel record);
}
