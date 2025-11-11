package com.erban.main.mybatismapper;

import com.erban.main.dto.RoomBgDTO;
import com.erban.main.model.RoomBg;
import com.erban.main.model.RoomBgExample;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RoomBgMapper {
    int countByExample(RoomBgExample example);

    int deleteByExample(RoomBgExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RoomBg record);

    int insertSelective(RoomBg record);

    List<RoomBg> selectByExample(RoomBgExample example);

    RoomBg selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") RoomBg record, @Param("example") RoomBgExample example);

    int updateByExample(@Param("record") RoomBg record, @Param("example") RoomBgExample example);

    int updateByPrimaryKeySelective(RoomBg record);

    int updateByPrimaryKey(RoomBg record);

    @Delete("DELETE FROM room_bg_tag WHERE room_bg_id = #{id}")
    int deleteTagByBgId(Integer id);

    @Delete("DELETE FROM room_bg_user WHERE room_bg_id = #{id}")
    int deleteUserByBgId(Integer id);

    @Insert("<script> " +
            "INSERT INTO room_bg_tag " +
            "   (room_bg_id, tag_id) " +
            "VALUES " +
            "   <foreach item='value' index='index' collection='list' separator=','> " +
            "       (#{id}, #{value}) " +
            "   </foreach> " +
            "</script> ")
    int addRoomBgTag(@Param("id") Integer id, @Param("list") List<Integer> list);

    @Insert("<script> " +
            "INSERT INTO room_bg_user " +
            "   (room_bg_id, uid) " +
            "VALUES " +
            "   <foreach item='value' index='index' collection='list' separator=','> " +
            "       (#{id}, #{value}) " +
            "   </foreach> " +
            "</script> ")
    int addRoomBgTagUser(@Param("id") Integer id, @Param("list") List<Integer> list);


    /**
     * 根据ID查询
     *
     * @param id id
     * @return RoomBgDTO
     */
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
    RoomBgDTO getById(@Param("id") Integer id);


    /**
     * 查询指定用的uid
     *
     * @param id id
     * @return List<Integer>
     */
    @Select("SELECT tag_id FROM room_bg_tag WHERE room_bg_id = #{id}")
    List<Integer> listTagByBgId(@Param("id") Integer id);

    /**
     * 查询指定用的id
     *
     * @param id id
     * @return List<Long>
     */
    @Select("SELECT uid FROM room_bg_user WHERE room_bg_id = #{id}")
    List<Long> listUidByBgId(@Param("id") Integer id);
}
