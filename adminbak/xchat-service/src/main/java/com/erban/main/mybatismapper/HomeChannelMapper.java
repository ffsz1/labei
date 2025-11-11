package com.erban.main.mybatismapper;

import com.erban.main.model.HomeChannel;
import com.erban.main.model.HomeChannelExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HomeChannelMapper {
    int countByExample(HomeChannelExample example);

    int deleteByExample(HomeChannelExample example);

    int deleteByPrimaryKey(Long id);

    int insert(HomeChannel record);

    int insertSelective(HomeChannel record);

    List<HomeChannel> selectByExample(HomeChannelExample example);

    HomeChannel selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") HomeChannel record, @Param("example") HomeChannelExample example);

    int updateByExample(@Param("record") HomeChannel record, @Param("example") HomeChannelExample example);

    int updateByPrimaryKeySelective(HomeChannel record);

    int updateByPrimaryKey(HomeChannel record);

    List<HomeChannel> listChannel(HomeChannel channel);
}
