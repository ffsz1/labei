package com.erban.main.mybatismapper;

import com.erban.main.model.ChannelUsers;
import com.erban.main.model.ChannelUsersExample;

import java.util.Collection;
import java.util.List;

import com.erban.main.model.Users;
import org.apache.ibatis.annotations.Param;

public interface ChannelUsersMapper {
    int countByExample(ChannelUsersExample example);

    int deleteByExample(ChannelUsersExample example);

    int insert(ChannelUsers record);

    int insertSelective(ChannelUsers record);

    List<ChannelUsers> selectByExample(ChannelUsersExample example);

    int updateByExampleSelective(@Param("record") ChannelUsers record, @Param("example") ChannelUsersExample example);

    int updateByExample(@Param("record") ChannelUsers record, @Param("example") ChannelUsersExample example);

    List<Users> listByChannelId(@Param("channelId")Integer channelId);

    int saveChannelUsers(@Param("id") Integer id, @Param("list") Collection<Long> list);

    List<Long> listUsersIdByChannel(@Param("id") Integer id);
}
