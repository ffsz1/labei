package com.juxiao.xchat.dao.room;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.room.dto.RoomTagDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoomTagDao {

    /**
     * 获取APP顶部的标签
     *
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<RoomTagDTO> listAppTopTags();

    /**
     * 获取全部标签
     *
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<RoomTagDTO> listAppAllTags();

    /**
     * 获取分类标签
     *
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<RoomTagDTO> listAppSearchTags();

    /**
     * 根据ID查询标签信息
     *
     * @param id
     * @return
     */
    @TargetDataSource(name = "ds2")
    RoomTagDTO getById(Long id);

    /**
     * 根据标签名查询标签信息
     *
     * @param name
     * @return
     */
    @TargetDataSource(name = "ds2")
    RoomTagDTO getByName(String name);

    /**
     * 根据uid获取显示的标签
     * @param uid uid
     * @return list
     */
    @TargetDataSource(name = "ds2")
    List<String> getRoomUsersTagByTagName(@Param("uid") Long uid);

}
