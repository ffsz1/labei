package com.erban.main.mybatismapper;

import com.erban.main.model.dto.RoomDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface ChannelRoomMapper {

    /**
     * 根据渠道ID删除房间管理信息
     *
     * @param id 渠道ID
     * @return
     */
    int deleteByChannel(Integer id);

    /**
     *  删除渠道房间
     * @param id
     * @param uid
     * @return
     */
    int deleteChannelRoom(@Param("id") Integer id, @Param("uid") Long uid);

    /**
     * 根据渠道id 查询房间信息
     *
     * @param id 渠道ID
     * @return
     */
    List<Long> listUidByChannel(Integer id);

    /**
     * 保存渠道的房间
     *
     * @param id   渠道ID
     * @param list 房主ID
     * @return
     */
    int saveChannelRoom(@Param("id") Integer id, @Param("list") Collection<Long> list);

    /**
     * 根据渠道 ID 查询房间信息
     * @param id 渠道ID
     * @return
     */
    List<RoomDTO> listByChannelId(Integer id);
}
