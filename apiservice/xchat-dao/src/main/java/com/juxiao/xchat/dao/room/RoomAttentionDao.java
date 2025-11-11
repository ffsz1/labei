package com.juxiao.xchat.dao.room;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.room.domain.RoomAttentionDO;
import com.juxiao.xchat.dao.room.dto.RoomAttentionDTO;
import com.juxiao.xchat.dao.room.dto.UsersRoomAttentionDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2018/11/28
 * @time 16:18
 */
public interface RoomAttentionDao {

    /**
     * 根据uid查询用户关注列表
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<RoomAttentionDTO> selectUidByRoomAttentions(@Param("uid") Long uid, @Param("pageNum")Integer pageNum, @Param("pageSize")Integer pageSize);

    /**
     * 插入
     * @param roomAttentionDO
     * @return
     */
    @TargetDataSource
    int insert(RoomAttentionDO roomAttentionDO);

    /**
     * 根据uid查询
     * @param uid
     * @param roomId
     * @return
     */
    @TargetDataSource(name = "ds2")
    RoomAttentionDO selectByUidAndRoomId(@Param("uid") Long uid,@Param("roomId")Long roomId);


    /**
     * 删除关注
     * @param uid
     * @param roomId
     * @return
     */
    @TargetDataSource
    int delAttentions(@Param("uid") Long uid,@Param("roomId")Long roomId);

    /**
     * 根据当前UID跟房间id查询是否有关注过
     * @param uid
     * @param roomId
     * @return
     */
    @TargetDataSource(name = "ds2")
    UsersRoomAttentionDTO selectRoomAttentionByUidAndRoomId(@Param("uid")Long uid,@Param("roomId")Long roomId);
}
