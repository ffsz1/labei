package com.juxiao.xchat.dao.mcoin;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.mcoin.dto.McoinMissionDTO;
import com.juxiao.xchat.dao.mcoin.dto.McoinMissionInfoDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface McoinMissionDao {

    /**
     * 保存新手任务
     *
     * @param uid
     * @param missionId
     * @param missionStatus
     */
    @TargetDataSource
    void saveOrUpdateMcoinOnceMission(@Param("uid") Long uid, @Param("missionId") Integer missionId, @Param("missionStatus") Byte missionStatus);

    /**
     * 保存每日任务
     *
     * @param uid
     * @param missionId
     * @param missionStatus
     */
    @TargetDataSource
    void saveOrUpdateMcoinDailyMission(@Param("uid") Long uid, @Param("missionId") Integer missionId, @Param("missionStatus") Byte missionStatus);

    /**
     * 保存每日签到任务
     *
     * @param uid
     * @param missionId
     */
    @TargetDataSource
    void saveOrUpdateMcoinWeeklyMission(@Param("uid") Long uid, @Param("missionId") Integer missionId);

    /**
     * 获取单个任务
     *
     * @param missionId
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT * FROM mcoin_mission WHERE id=#{missionId}")
    McoinMissionDTO getMcoinMission(@Param("missionId") Integer missionId);

    /**
     * 获取用户当前新手任务状态
     *
     * @param uid
     * @param missionId
     * @return
     */
    @TargetDataSource
    @Select("SELECT IFNULL( `mission_status`, 1 ) AS missionStatus FROM `mcoin_once_mission` WHERE `mission_id` = #{missionId} AND `uid` = #{uid}")
    Byte getUserCurrentOnceMissionStatus(@Param("uid") Long uid, @Param("missionId") Integer missionId);

    /**
     * 获取用户当前每日任务状态
     *
     * @param uid
     * @param missionId
     * @return
     */
    @TargetDataSource
    @Select("SELECT IFNULL( `mission_status`, 1 ) AS missionStatus FROM `mcoin_daily_mission` WHERE `mission_id` = #{missionId} AND `uid` = #{uid} AND `complete_date` = curdate()")
    Byte getUserCurrentDailyMissionStatus(@Param("uid") Long uid, @Param("missionId") Integer missionId);


    @TargetDataSource(name = "ds2")
    McoinMissionInfoDTO getUserCrntWeeklyMission(@Param("uid") Long uid);

    /**
     * 根据任务类型查询用户的任务列表
     *
     * @param uid
     * @param auditing
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<McoinMissionInfoDTO> listUserMcoinOnceMissions(@Param("uid") Long uid, @Param("auditing") boolean auditing);

    /**
     * 查询用户每日任务
     *
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<McoinMissionInfoDTO> listUserMcoinDailyMissions(@Param("uid") Long uid, @Param("auditing") boolean auditing);

    /**
     * 查询用户每周任务
     *
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<McoinMissionInfoDTO> listUserMcoinWeeklyMissions(@Param("uid") Long uid, @Param("auditing") boolean auditing);

    /**
     * 查询用户邀请注册任务
     * @param uid
     * @param auditing
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<McoinMissionInfoDTO> listUserMcoinShareRegisterMissions(@Param("uid") Long uid, @Param("auditing") boolean auditing);

    /**
     * 统计用户新手任务
     *
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT count(*) FROM mcoin_once_mission WHERE mission_status=2 and uid = #{uid}")
    int countUserMcoinOnceMissions(@Param("uid") Long uid);

    /**
     * 统计用户每日任务
     *
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT COUNT(*) FROM mcoin_daily_mission WHERE complete_date = CURRENT_DATE () AND `mission_status` = 2 AND uid = #{uid}")
    int countUserMcoinDailyMissionss(@Param("uid") Long uid);
}
