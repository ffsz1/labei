package com.erban.main.mybatismapper;

import com.erban.main.model.ChannelIp;
import com.erban.main.model.ChannelIpExample;

import java.util.Collection;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ChannelIpMapper {
    int countByExample(ChannelIpExample example);

    int deleteByExample(ChannelIpExample example);

    int insert(ChannelIp record);

    int insertSelective(ChannelIp record);

    List<ChannelIp> selectByExample(ChannelIpExample example);

    int updateByExampleSelective(@Param("record") ChannelIp record, @Param("example") ChannelIpExample example);

    int updateByExample(@Param("record") ChannelIp record, @Param("example") ChannelIpExample example);

    List<ChannelIp> listByChannelId(@Param("channelId")Integer channelId);

    int saveChannelIp(@Param("id") Integer id, @Param("list") Collection<String> list);

    List<String> listIpByChannel(@Param("channelId") Integer id);
}
