package com.juxiao.xchat.dao.room;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.room.domain.RoomDO;
import com.juxiao.xchat.dao.room.dto.RoomDTO;
import com.juxiao.xchat.dao.room.dto.RoomLinkVo;
import com.juxiao.xchat.dao.room.dto.RoomSearchDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @class: RoomDao.java
 * @author: chenjunsheng
 * @date 2018/6/21
 */
public interface RoomDao {

    @TargetDataSource
    int save(RoomDO room);

    @TargetDataSource
    int update(RoomDO room);

    /**
     * 获取房间
     *
     * @param roomId
     * @return
     */
    @TargetDataSource(name = "ds2")
    RoomDTO getRoom(@Param("roomId") Long roomId);

    /**
     * 根据用户查询房间
     *
     * @param uid
     * @return
     */
    @TargetDataSource
    RoomDTO getUserRoom(@Param("uid") Long uid);

    /**
     * 查询房间
     *
     * @param
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<RoomSearchDTO> listSearchRoom(@Param("key") String key, @Param("uids") List<Long> uids);

    @TargetDataSource(name = "ds2")
    List<RoomDTO> listByTagIds(@Param("tagIds") List<Integer> tagIds, @Param("onlineNum") Integer onlineNum);

    @TargetDataSource(name = "ds2")
    @Select("SELECT u.*, r.room_id as roomId, r.avatar as roomAvatar, r.`online_num` as onlineNum, 0 as linkNum FROM `room` r inner join users u on r.uid = u.uid WHERE r.tag_id=8 AND r.`valid` = 1 and r.`online_num` > 0 and r.can_show=1 and ifnull(r.room_pwd, '')='' order by if(u.gender=#{gender},1,0), r.online_num, u.fans_num desc, u.follow_num desc")
    List<RoomLinkVo> getLinkRoom(@RequestParam("gender") Byte gender);
}
