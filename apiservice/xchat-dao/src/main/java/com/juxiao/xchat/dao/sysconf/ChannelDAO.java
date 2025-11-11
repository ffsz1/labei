package com.juxiao.xchat.dao.sysconf;

import com.juxiao.xchat.dao.sysconf.dto.ChannelDTO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Auther: alwyn
 * @Description:
 * @Date: 2018/10/22 16:36
 */
public interface ChannelDAO {

    /**
     * 根据渠道号, 查询房间列表
     * @param channelName
     * @return
     */
    @Select("SELECT uid FROM channel_room WHERE channel_id = #{id}")
    List<Long> listByChannel(String channelName);

    /**
     * 根据渠道名称, 查询渠道信息
     * @param channelName
     * @return
     */
    @Select("SELECT * FROM channel WHERE channel = #{channelName}")
    List<ChannelDTO> getByChannelName(String channelName);

    @Select("SELECT icon_id as iconId FROM channel_icon WHERE channel_id = #{id}")
    List<Integer> listIconByChannelId(String toString);

    @Select("SELECT banner_id as bannerId FROM channel_banner WHERE channel_id = #{id}")
    List<Integer> listBannerByChannelId(String toString);
}
