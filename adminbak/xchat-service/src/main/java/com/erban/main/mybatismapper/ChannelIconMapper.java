package com.erban.main.mybatismapper;

import com.erban.main.model.ChannelIcon;
import com.erban.main.model.ChannelIconExample;

import java.util.Collection;
import java.util.List;

import com.erban.main.model.Icon;
import com.erban.main.model.dto.RoomDTO;
import org.apache.ibatis.annotations.Param;

public interface ChannelIconMapper {
    int countByExample(ChannelIconExample example);

    int deleteByExample(ChannelIconExample example);

    int insert(ChannelIcon record);

    int insertSelective(ChannelIcon record);

    List<ChannelIcon> selectByExample(ChannelIconExample example);

    int updateByExampleSelective(@Param("record") ChannelIcon record, @Param("example") ChannelIconExample example);

    int updateByExample(@Param("record") ChannelIcon record, @Param("example") ChannelIconExample example);

    List<Long> listIconByChannel(@Param("channelId")Integer channelId);


    int saveChannelIcon(@Param("id") Integer id, @Param("list") Collection<Long> list);

    List<Icon> listByChannelId(@Param("channelId")Integer channelId);
}
