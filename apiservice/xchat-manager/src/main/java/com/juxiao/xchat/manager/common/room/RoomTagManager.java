package com.juxiao.xchat.manager.common.room;

import com.juxiao.xchat.dao.room.dto.RoomTagDTO;

import java.util.List;

/**
 * 房间标签
 */
public interface RoomTagManager {

    /**
     * 获取全部标签
     *
     * @return
     */
    List<RoomTagDTO> getAllTag(Long uid);

    /**
     * 获取顶部标签
     *
     * @return
     */
    List<RoomTagDTO> getTopTagList(String os, String appVersion);

    /**
     * 获取分类标签
     *
     * @return
     */
    List<RoomTagDTO> getSearchTags();


    /**
     * 获取分类标签
     *
     * @return
     */
    List<RoomTagDTO> getSearchTags(boolean isFilter);

    /**
     * 获取分类标签
     *
     * @return
     */
    List<RoomTagDTO> listWxappTags();

    /**
     * 根据ID获取标签信息
     *
     * @param id 标签 ID
     * @return
     */
    RoomTagDTO getById(Long id);

    /**
     * 根据标签名获取标签信息
     *
     * @param name 标签名
     * @return
     */
    RoomTagDTO getByName(String name);

}
