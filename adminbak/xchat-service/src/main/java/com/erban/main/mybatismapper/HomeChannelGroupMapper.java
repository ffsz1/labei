package com.erban.main.mybatismapper;

import com.erban.main.model.HomeChannelGroup;
import com.erban.main.model.HomeChannelGroupExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HomeChannelGroupMapper {
    int countByExample(HomeChannelGroupExample example);

    int deleteByExample(HomeChannelGroupExample example);

    int deleteByPrimaryKey(Long groupId);

    int insert(HomeChannelGroup record);

    int insertSelective(HomeChannelGroup record);

    List<HomeChannelGroup> selectByExample(HomeChannelGroupExample example);

    HomeChannelGroup selectByPrimaryKey(Long groupId);

    int updateByExampleSelective(@Param("record") HomeChannelGroup record, @Param("example") HomeChannelGroupExample example);

    int updateByExample(@Param("record") HomeChannelGroup record, @Param("example") HomeChannelGroupExample example);

    int updateByPrimaryKeySelective(HomeChannelGroup record);

    int updateByPrimaryKey(HomeChannelGroup record);
}
