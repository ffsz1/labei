package com.juxiao.xchat.service.api.room.impl;

import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.room.RoomAttentionDao;
import com.juxiao.xchat.dao.room.domain.RoomAttentionDO;
import com.juxiao.xchat.dao.room.dto.RoomAttentionDTO;
import com.juxiao.xchat.service.api.room.RoomAttentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2018/11/28
 * @time 16:15
 */
@Service
public class RoomAttentionServiceImpl implements RoomAttentionService {

    @Autowired
    private RoomAttentionDao roomAttentionDao;

    /**
     * 根据uid查询用户关注列表
     *
     * @param uid
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public List<RoomAttentionDTO> selectUidByRoomAttentions(Long uid, Integer pageNum, Integer pageSize) {
        if (pageNum < 1) {
            pageNum = 1;
        }
        int current = (pageNum - 1) * pageSize;
        return roomAttentionDao.selectUidByRoomAttentions(uid, current, pageSize);
    }

    /**
     * 插入
     *
     * @param uid
     * @param roomId
     * @return
     */
    @Override
    public WebServiceMessage insert(Long uid, Long roomId) {
        if (uid == null && roomId == null) {
            return WebServiceMessage.failure("参数不能为空!");
        }

        RoomAttentionDO roomAttention = selectByUidAndRoomId(uid, roomId);
        if (roomAttention != null) {
            return WebServiceMessage.failure("已关注过该房间!");
        }

        roomAttention = new RoomAttentionDO();
        roomAttention.setRoomId(roomId);
        roomAttention.setUid(uid);
        roomAttention.setCreateTime(new Date());
        roomAttentionDao.insert(roomAttention);
        return WebServiceMessage.success("关注成功!");
    }

    /**
     * 根据uid查询
     *
     * @param uid
     * @return
     */
    public RoomAttentionDO selectByUidAndRoomId(Long uid, Long roomId) {
        return roomAttentionDao.selectByUidAndRoomId(uid, roomId);
    }


    /**
     * 删除关注
     *
     * @param uid
     * @param roomId
     * @return
     */
    @Override
    public int delAttentions(Long uid, String roomId) {
        String[] roomIds = roomId.split(",");
        for (int i = 0; i < roomIds.length; i++) {
            roomAttentionDao.delAttentions(uid, Long.valueOf(roomIds[i]));
        }
        return 1;
    }

    /**
     * 检测是否关注过
     *
     * @param uid
     * @param roomId
     * @return
     */
    @Override
    public boolean checkAttentions(Long uid, Long roomId) {
        RoomAttentionDO roomAttentionDO = roomAttentionDao.selectByUidAndRoomId(uid, roomId);
        if (roomAttentionDO == null) {
            return false;
        } else {
            return true;
        }
    }
}
