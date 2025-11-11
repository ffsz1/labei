package com.juxiao.xchat.dao.room;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.room.domain.RoomBgDO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Auther: alwyn
 * @Description: 房间背景DAO
 * @Date: 2018/10/9 11:12
 */
public interface RoomBgDAO {

    @TargetDataSource(name = "ds2")
    @Select("SELECT " +
            "  bg.* " +
            "FROM " +
            "  room_bg bg " +
            "WHERE " +
            "  bg.id = #{id} " +
            "  AND ( " +
            "    bg.STATUS = '1' " +
            "    OR ( bg.STATUS = '2' AND NOW() > bg.begin_date AND NOW() < bg.end_date ) " +
            "  )")
    RoomBgDO getById(Integer id);

    @TargetDataSource(name = "ds2")
    @Select("SELECT " +
            "  bg.* " +
            "FROM " +
            "  room_bg bg " +
            "WHERE " +
            "  bg.type = 1 " +
            "  AND ( bg.status = \'1\' OR (bg.status = \'2\' AND NOW( ) > bg.begin_date AND NOW( ) < bg.end_date ) ) UNION " +
            "SELECT " +
            "  bg.* " +
            "FROM " +
            "  room_bg bg " +
            "  LEFT JOIN room_bg_tag rbt ON rbt.room_bg_id = bg.id " +
            "  JOIN room r ON r.tag_id = rbt.tag_id  " +
            "WHERE " +
            "  r.room_id = #{roomId}  " +
            "  AND bg.type = 2 " +
            "  AND ( bg.status = \'1\' OR (bg.status = \'2\' AND NOW( ) > bg.begin_date AND NOW( ) < bg.end_date ) ) UNION " +
            "SELECT " +
            "  bg.* " +
            "FROM " +
            "  room_bg bg " +
            "  LEFT JOIN room_bg_user rbu ON rbu.room_bg_id = bg.id " +
            "  JOIN room r ON r.uid = rbu.uid  " +
            "WHERE " +
            "  r.room_id = #{roomId} " +
            "  AND bg.type = 3 " +
            "  AND ( bg.status = \'1\' OR (bg.status = \'2\' AND NOW( ) > bg.begin_date AND NOW( ) < bg.end_date ) )")
    List<RoomBgDO> listByRoomId(Long roomId);

    /**
     * 查询指定用的uid
     * @param id
     * @return
     */
    @Select("SELECT uid FROM room_bg_user WHERE room_bg_id = #{id}")
    List<Long> listUidByBgId(Integer id);

    /**
     * 查询指定用的uid
     * @param id
     * @return
     */
    @Select("SELECT tag_id FROM room_bg_tag WHERE room_bg_id = #{id}")
    List<Integer> listTagByBgId(Integer id);

}
