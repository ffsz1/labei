package com.erban.main.mybatismapper;

import com.erban.main.model.Banner;
import com.erban.main.model.ChannelBanner;
import com.erban.main.model.ChannelBannerExample;

import java.util.Collection;
import java.util.List;

import com.erban.main.model.dto.RoomDTO;
import org.apache.ibatis.annotations.Param;

public interface ChannelBannerMapper {
    int countByExample(ChannelBannerExample example);

    int deleteByExample(ChannelBannerExample example);

    int insert(ChannelBanner record);

    int insertSelective(ChannelBanner record);

    List<ChannelBanner> selectByExample(ChannelBannerExample example);

    int updateByExampleSelective(@Param("record") ChannelBanner record, @Param("example") ChannelBannerExample example);

    int updateByExample(@Param("record") ChannelBanner record, @Param("example") ChannelBannerExample example);

    List<Long> listBannerIdByChannel(@Param("channelId")Integer channelId);


    /**
     * 保存渠道的房间
     *
     * @param id   渠道ID
     * @param list 房主ID
     * @return
     */
    int saveChannelBanner(@Param("id") Integer id, @Param("list") Collection<Long> list);

    List<Banner> listByChannelId(@Param("channelId")Integer channelId);
}
