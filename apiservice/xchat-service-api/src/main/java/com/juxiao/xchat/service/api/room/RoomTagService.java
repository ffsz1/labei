package com.juxiao.xchat.service.api.room;

import com.juxiao.xchat.dao.room.dto.RoomTagDTO;

import java.util.List;

public interface RoomTagService {

    /**
     * 搜索分类标签
     * @param os 系统
     * @param appVersion app版本
     * @param uid uid
     * @param channel 渠道
     * @return
     */
    List<RoomTagDTO> getSearchTags(String os, String appVersion, Long uid, String channel);

}
