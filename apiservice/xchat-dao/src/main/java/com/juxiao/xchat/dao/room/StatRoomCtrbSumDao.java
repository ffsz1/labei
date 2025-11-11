package com.juxiao.xchat.dao.room;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.room.domain.StatRoomCtrbSumDO;
import com.juxiao.xchat.dao.room.dto.StatRoomCtrbSumDTO;
import com.juxiao.xchat.dao.room.domain.StatRoomFlowDo;
import com.juxiao.xchat.dao.room.dto.StatRoomUserCtrbSumDTO;
import com.juxiao.xchat.dao.room.query.RoomContributionQuery;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @class: StatRoomCtrbSumDao.java
 * @author: chenjunsheng
 * @date 2018/6/5
 */
public interface StatRoomCtrbSumDao {

    /**
     * @param sumDo
     */
    @TargetDataSource
    void save(StatRoomCtrbSumDO sumDo);

    /**
     * @return
     */
    @TargetDataSource
    @Update("UPDATE `stat_room_ctrb_sum` SET `sum_gold`=`sum_gold`+#{goldSum,jdbcType=INTEGER} WHERE `ctrb_id` = #{ctrbId,jdbcType=INTEGER}")
    int updateCtrbGoldSum(@Param("ctrbId") Integer ctrbId, @Param("goldSum") Integer goldSum);

    /**
     * @param uid
     * @param ctrbUid
     * @return
     */
    @TargetDataSource(name = "ds2")
    StatRoomCtrbSumDTO getUserRoomCtrb(@Param("uid") Long uid, @Param("ctrbUid") Long ctrbUid);

    /**
     * 查询房间魅力值贡献榜（限制前20名）
     *
     * @param query
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<StatRoomUserCtrbSumDTO> listRoomWealthCtrb(RoomContributionQuery query);

    /**
     * 查询相亲房间魅力值贡献榜
     *
     * @param query
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<StatRoomUserCtrbSumDTO> listXqRoomWealthCtrb(RoomContributionQuery query);

    /**
     * 查询房间魅力值贡献榜（限制前20名）
     *
     * @param query
     * @return
     */
    @TargetDataSource(name = "ds2")
    StatRoomUserCtrbSumDTO getRoomCharmCtrbTop(RoomContributionQuery query);

    /**
     * 查询房间财富贡献榜单（限制前20名）
     *
     * @param query
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<StatRoomUserCtrbSumDTO> listRoomCharmCtrb(RoomContributionQuery query);

    /**
     * 查询相亲房间财富贡献榜单（限制前20名）
     *
     * @param query
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<StatRoomUserCtrbSumDTO> listXqRoomCharmCtrb(RoomContributionQuery query);


    /**
     * 查询房间财富榜单第一名
     *
     * @param query
     * @return
     */
    @TargetDataSource(name = "ds2")
    StatRoomUserCtrbSumDTO getRoomWealthCtrbTop(RoomContributionQuery query);


    /**
     * 获取房主流水
     * @param uid 房主uid
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @TargetDataSource(name = "ds2")
    StatRoomFlowDo getRoomOwnerFlow(@Param("uid") Long uid, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 获取房主分成记录
     * @param uid
     * @param date
     * @return
     */
    @TargetDataSource(name = "ds2")
    StatRoomFlowDo getRoomOwnerShareRecord(@Param("uid") Long uid, @Param("date") String date);

    /**
     * 保存房主分成记录
     * @return
     */
    @TargetDataSource(name = "ds2")
    int saveShare(StatRoomFlowDo statRoomFlowDo);

    /**
     * 获取房主流水(不管是否牌照房)
     * @param uid 房主uid
     * @param startTime 开始时间(可为空)
     * @param endTime 结束时间(可为空)
     * @return
     */
    @TargetDataSource(name = "ds2")
    Long getRoomOwnerFlowV2(@Param("uid") Long uid, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 获取个人在指定房间的流水
     * @param roomUid 房主uid
     * @param uid 用户uid
     * @param startTime 开始时间(可为空)
     * @param endTime 结束时间(可为空)
     * @return
     */
    @TargetDataSource(name = "ds2")
    Long getUserInRoomFlow(@Param("roomUid") Long roomUid, @Param("uid") Long uid, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 获取公会流水
     * @param guildId 公会id
     * @param startTime 开始时间(可为空)
     * @param endTime 结束时间(可为空)
     * @return
     */
    @TargetDataSource(name = "ds2")
    Long getGuildFlow(@Param("guildId") Long guildId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 获取厅流水
     * @param hallId 厅id
     * @param startTime 开始时间(可为空)
     * @param endTime 结束时间(可为空)
     * @return
     */
    @TargetDataSource(name = "ds2")
    Long getGuildHallFlow(@Param("hallId") Long hallId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 获取厅流水
     * @param uid 用户uid
     * @param hallId 厅id
     * @param startTime 开始时间(可为空)
     * @param endTime 结束时间(可为空)
     * @return
     */
    @TargetDataSource(name = "ds2")
    Long getGuildHallMemberFlow(@Param("uid") Long uid, @Param("hallId") Long hallId, @Param("startTime") String startTime
            , @Param("endTime") String endTime);
}
