package com.juxiao.xchat.dao.task;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.family.domain.FamilyExitRecordDO;
import com.juxiao.xchat.dao.item.dto.GiftCarPurseRecordDTO;
import com.juxiao.xchat.dao.item.dto.HeadwearPurseRecordDTO;
import com.juxiao.xchat.dao.room.domain.RoomRobotGroup;
import com.juxiao.xchat.dao.room.domain.RoomRobotGroupRela;
import com.juxiao.xchat.dao.room.domain.StatRoomFlowOnlinePeriod;
import com.juxiao.xchat.dao.room.dto.HomeRoomFlowPeriod;
import com.juxiao.xchat.dao.room.dto.RoomDTO;
import com.juxiao.xchat.dao.room.dto.RoomFlowWeekVo;
import com.juxiao.xchat.dao.room.dto.RoomGameConfigDTO;
import com.juxiao.xchat.dao.task.domain.DeviceInfoDO;
import com.juxiao.xchat.dao.task.domain.StatDailyUserPurseDO;
import com.juxiao.xchat.dao.task.dto.UserChargeCountDTO;
import com.juxiao.xchat.dao.task.dto.UserSumDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TaskDao {
    @TargetDataSource(name = "ds2")
    @Select("SELECT u.uid, u.erban_no AS erbanNo, u.nick, u.gender, u.avatar FROM users u WHERE u.def_user = 1 AND u" +
            ".gender <> #{gender} AND u.avatar <> 'https://pic.chaoxuntech.com/default_head.png' AND EXISTS (select 1" +
            " from account_login_record a where a.uid = u.uid and a.create_time BETWEEN #{startDate} AND #{endDate})")
    List<Map<String, Object>> findOppositeSex(@Param("gender") String gender, @Param("startDate") Date startDate,
                                              @Param("endDate") Date endDate);

    @TargetDataSource(name = "ds2")
    @Select("SELECT u.uid, u.erban_no AS erbanNo, u.nick, SUM(o.sum_gold) AS sum FROM one_day_room_send_sum o INNER " +
            "JOIN users u ON o.send_uid = u.uid WHERE u.create_time >= DATE_ADD( NOW( ), INTERVAL - 7 DAY ) AND o" +
            ".create_time BETWEEN #{startDate} AND #{endDate} GROUP BY o.send_uid HAVING sum > 50000")
    List<UserSumDTO> listNewUserGiftSendSum(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @TargetDataSource(name = "ds2")
    @Select("SELECT u.uid, u.erban_no AS erbanNo, u.nick, SUM(o.sum_gold) AS sum FROM one_day_room_send_sum o INNER " +
            "JOIN users u ON o.send_uid = u.uid WHERE u.create_time < DATE_ADD( NOW( ), INTERVAL - 7 DAY ) AND o" +
            ".create_time BETWEEN #{startDate} AND #{endDate} GROUP BY o.send_uid HAVING sum > 200000")
    List<UserSumDTO> listOldUserGiftSendSum(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @TargetDataSource(name = "ds2")
    @Select("SELECT u.uid, u.erban_no AS erbanNo, u.nick, SUM(o.sum_gold) AS sum FROM one_day_room_recv_sum o INNER " +
            "JOIN users u ON o.recv_uid = u.uid WHERE u.create_time >= DATE_ADD( NOW( ), INTERVAL - 7 DAY ) AND o" +
            ".create_time BETWEEN #{startDate} AND #{endDate} GROUP BY o.recv_uid HAVING sum > 50000")
    List<UserSumDTO> listNewUserGiftRecvSum(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @TargetDataSource(name = "ds2")
    @Select("SELECT u.uid, u.erban_no AS erbanNo, u.nick, SUM(o.sum_gold) AS sum FROM one_day_room_recv_sum o INNER " +
            "JOIN users u ON o.recv_uid = u.uid WHERE u.create_time < DATE_ADD( NOW( ), INTERVAL - 7 DAY ) AND o" +
            ".create_time BETWEEN #{startDate} AND #{endDate} GROUP BY o.recv_uid HAVING sum > 200000")
    List<UserSumDTO> listOldUserGiftRecvSum(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @TargetDataSource(name = "ds2")
    @Select("SELECT o.uid, u.erban_no AS erbanNo, u.nick, SUM(o.total_gold) AS sum FROM charge_record o INNER JOIN " +
            "users u ON o.uid = u.uid WHERE o.create_time BETWEEN #{startDate} AND #{endDate} and o.charge_status = 2" +
            " and o.channel not in ('exchange','company') and u.create_time >= DATE_ADD(NOW(),INTERVAL -7 DAY) GROUP " +
            "BY o.uid HAVING sum > #{threshold}")
    List<UserSumDTO> listUserChargeSum4New(@Param("threshold") Integer threshold, @Param("startDate") Date startDate,
                                           @Param("endDate") Date endDate);

    @TargetDataSource(name = "ds2")
    @Select("SELECT o.uid, u.erban_no AS erbanNo, u.nick, SUM(o.total_gold) AS sum FROM charge_record o INNER JOIN " +
            "users u ON o.uid = u.uid WHERE o.create_time BETWEEN #{startDate} AND #{endDate} and o.charge_status = 2" +
            " and o.channel not in ('exchange','company') and u.create_time < DATE_ADD(NOW(),INTERVAL -7 DAY) GROUP " +
            "BY o.uid HAVING sum > #{threshold}")
    List<UserSumDTO> listUserChargeSum4Old(@Param("threshold") Integer threshold, @Param("startDate") Date startDate,
                                           @Param("endDate") Date endDate);

    @TargetDataSource(name = "ds2")
    @Select("SELECT u.uid, u.erban_no AS erbanNo, u.nick, COUNT(1) AS chargeCount FROM charge_record o INNER JOIN " +
            "users u ON o.uid = u.uid WHERE o.create_time BETWEEN #{startDate} AND #{endDate} and o.charge_status = 2" +
            " and o.channel not in ('exchange','company') and u.create_time > DATE_ADD(NOW(),INTERVAL -7 DAY) GROUP " +
            "BY o.uid, o.channel, o.amount HAVING chargeCount > 15")
    List<UserChargeCountDTO> listUserChargeCount4New(@Param("startDate") Date startDate,
                                                     @Param("endDate") Date endDate);

    @TargetDataSource(name = "ds2")
    @Select("SELECT cr.uid, u.erban_no AS `erbanNo`, u.nick AS `nick`, COUNT( * ) AS `chargeCount` FROM charge_record" +
            " cr INNER JOIN users u ON cr.uid=u.uid WHERE cr.channel = 'ios_pay' AND create_time BETWEEN #{startDate}" +
            " AND #{endDate} GROUP BY cr.uid HAVING chargeCount>=3")
    List<UserChargeCountDTO> listUserIosChargeCount(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @TargetDataSource(name = "ds2")
    @Select("select a.uid,a.onlineNum,1 as seqNo from (select r.uid,r.online_num as onlineNum,r.tag_id,SUM(o" +
            ".sum_gold) as tol from one_day_room_recv_sum o INNER JOIN room r on o.uid = r.uid where o.create_time " +
            "BETWEEN #{startDate} and #{endDate} and r.tag_id in (22,23,24,25) GROUP BY o.uid ORDER BY tol desc,r" +
            ".tag_id) a GROUP BY a.tag_id")
    List<Map<String, Object>> findLastDayOne(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 获取优质推荐位列表 (home_hot_manual_recomm)
     *
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT room.uid, room.online_num, room.title ,hhmr.seq_no,srfop.score FROM ( SELECT * FROM home_hot_manual_recomm WHERE now() BETWEEN start_valid_time AND end_valid_time ) AS hhmr " +
            "INNER JOIN users ON users.uid = hhmr.uid LEFT JOIN room ON hhmr.uid = room.uid LEFT JOIN stat_room_flow_online_period srfop ON srfop.uid = room.uid " +
            "WHERE room.is_permit_room = 1 AND room.can_show = 1 AND ifnull( room.room_pwd, '' ) = '' ORDER BY hhmr.seq_no ASC,srfop.score DESC LIMIT #{size} ")
    List<HomeRoomFlowPeriod> getHomeHotManualRecommendPositionList(@Param("size") Integer size);

    /**
     * 获取热门房间列表
     * tagIds不为null即为指定分类
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<StatRoomFlowOnlinePeriod> selectRoomFlowOnlinePeriod(@Param("recommendUidList") List<Long> recommendUidList,
                                                              @Param("tagIds") List<Integer> tagIds, @Param(
                                                                      "isPermitRoom") List<Integer> isPermitRoom,
                                                              @Param("size") Integer size);

    @TargetDataSource(name = "ds2")
    List<StatRoomFlowOnlinePeriod> getGreenHome(@Param("recomUids") List<Long> recomUids, @Param("size") Integer size);

    @TargetDataSource(name = "ds2")
    @Select("SELECT * from room_robot_group_rela where `status` = 1")
    List<RoomRobotGroupRela> findRoomRobotList();

    @TargetDataSource(name = "ds2")
    @Select("SELECT * from room_robot_group where group_no = #{groupNo}")
    List<RoomRobotGroup> findRoomRobotGropuList(@Param("groupNo") Integer groupNo);

    @TargetDataSource(name = "ds2")
    @Select("SELECT SUM(IF(YEARWEEK(g.create_time) = YEARWEEK(now()) - 2, g.sum_gold, 0 ) ) secondWeek, SUM(IF" +
            "(YEARWEEK(g.create_time) = YEARWEEK(now()) - 1,g.sum_gold, 0)) firstWeek, g.uid FROM " +
            "one_day_room_send_sum g LEFT JOIN room r ON r.uid = g.uid GROUP BY g.uid")
    List<RoomFlowWeekVo> sumByTowWeeks();

    @TargetDataSource(name = "ds2")
    @Select("select * from gift_car_purse_record")
    List<GiftCarPurseRecordDTO> findAllGiftCar();

    @TargetDataSource(name = "ds2")
    @Select("SELECT * from headwear_purse_record")
    List<HeadwearPurseRecordDTO> findAllHeadwear();

    @TargetDataSource(name = "ds2")
    @Select("SELECT * FROM `room` WHERE `is_permit_room` = #{isPermitRoom} and `valid`+`online_num` > 0 ORDER BY " +
            "`online_num` desc")
    List<RoomDTO> findIsPermitRoom(@Param("isPermitRoom") Integer isPermitRoom);


    @TargetDataSource(name = "ds2")
    List<FamilyExitRecordDO> findFamilyExitRecord();

    /**
     * 房间推荐
     *
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT g.room_uid as uid, SUM(g.total_gold_num) as tol from gift_send_record g where g.room_uid is not " +
            "null GROUP BY g.room_uid  ORDER BY tol desc limit 6")
    List<HomeRoomFlowPeriod> findRoomRecomm();

    @TargetDataSource(name = "ds2")
    List<StatRoomFlowOnlinePeriod> getGreenListHome(@Param("size") Integer size);

    @TargetDataSource(name = "ds2")
    List<RoomGameConfigDTO> findRoomGameConfig();

    @TargetDataSource
    @Update("UPDATE room_game_config SET status = #{status} WHERE id = #{id} ")
    int updateByStatus(Integer id, Integer status);

    /**
     * 保存当天的钻石金币报表
     *
     * @param purseDo
     */
    @TargetDataSource
    @Insert("REPLACE INTO `stat_daily_user_purse` ( `create_date`, `gold_sum`, `diamond_sum`, " +
            "`gift_nomal_diamond_sum`, `gift_draw_diamond_sum`, `create_time` ) VALUES ( CURRENT_DATE(), #{goldSum}, " +
            "#{diamondSum}, #{giftNomalDiamondSum}, #{giftDrawDiamondSum}, NOW() )")
    void saveUserDailyPurse(StatDailyUserPurseDO purseDo);

    int saveActivationInfo(DeviceInfoDO deviceInfo);
}
