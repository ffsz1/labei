package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.user.domain.UserPurseHotRoomRecordDO;
import com.juxiao.xchat.dao.user.dto.UserPurseHotRoomRecordDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户购买推荐位操作
 *
 * @class: UserPurseHotRoomRecordDao.java
 * @author: chenjunsheng
 * @date 2018/7/12
 */
public interface UserPurseHotRoomRecordDao {

    @TargetDataSource
    void save(UserPurseHotRoomRecordDO recordDo);
    /**
     * 根据主键获取记录
     * @param recordId
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT `record_id` AS recordId,`uid` AS uid,`erban_no` AS erbanNo,`gold_num` AS goldNum,`start_time` AS startTime,`end_time` AS endTime,`create_time` AS createTime FROM user_purse_hot_room_record WHERE record_id = #{recordId}")
    UserPurseHotRoomRecordDTO getHotRoomRecord(@Param("recordId") String recordId);

    /**
     * 查询用户推荐位购买
     *
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT record_id FROM user_purse_hot_room_record WHERE uid = #{uid} ORDER BY record_id DESC")
    List<String> listUserHotRoomRecordIds(@Param("uid") Long uid);
}