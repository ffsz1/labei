package com.juxiao.xchat.service.api.room;

import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.room.dto.RoomAttentionDTO;

import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2018/11/5
 * @time 10:22
 */
public interface RoomAttentionService {

    /**
     * 根据uid查询用户关注列表
     * @param uid
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<RoomAttentionDTO> selectUidByRoomAttentions(Long uid, Integer pageNum, Integer pageSize);

    /**
     * 插入
     * @param uid
     * @param roomId
     * @return
     */
    WebServiceMessage insert(Long uid, Long roomId);


    /**
     * 删除关注
     * @param uid
     * @param roomId
     * @return
     */
    int delAttentions(Long uid, String roomId);

    /**
     * 检测是否关注过
     * @param uid
     * @param roomId
     * @return
     */
    boolean checkAttentions(Long uid, Long roomId);
}
