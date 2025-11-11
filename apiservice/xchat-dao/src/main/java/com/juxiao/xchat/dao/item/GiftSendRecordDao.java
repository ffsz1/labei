package com.juxiao.xchat.dao.item;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.item.domain.GiftSendRecordDO;
import com.juxiao.xchat.dao.item.domain.RankActDO;
import com.juxiao.xchat.dao.item.dto.RoomFlowRecordDTO;
import com.juxiao.xchat.dao.item.dto.RoomFlowRecordDetailDTO;
import com.juxiao.xchat.dao.item.dto.UserTotalGoldDTO;
import com.juxiao.xchat.dao.item.query.RoomRankQuery;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 礼物发送记录表数据库接口
 *
 * @class: GiftSendRecordDao.java
 * @author: chenjunsheng
 * @date 2018/6/6
 */
public interface GiftSendRecordDao {

    /**
     * 保存
     *
     * @param recordDo
     */
    @TargetDataSource
    void save(GiftSendRecordDO recordDo);

    /**
     * 获取用户发送礼物金币总数
     *
     * @param uid
     * @return
     * @author: chenjunsheng
     * @date 2018/6/6
     */
    @TargetDataSource(name = "ds2")
    UserTotalGoldDTO getUserSendTotalGold(@Param("uid") Long uid);

    /**
     * 获取用户收到礼物价值的总金币
     *
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    UserTotalGoldDTO getUserReceiveTotalGold(@Param("uid") Long uid);

    /**
     * 获取分类排行信息
     *
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<RankActDO> roomRank(RoomRankQuery query);

    /**
     * @param roomUid
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT sum(sum_gold) as totalGoldNum, date_format(create_time,'%Y-%m-%d') as createTime FROM one_day_room_send_sum WHERE uid=#{roomUid} GROUP BY create_time ORDER BY create_time DESC")
    List<RoomFlowRecordDTO> listRoomFlowGroupBy(@Param("roomUid") Long roomUid);

    /**
     *
     * @param roomUid
     * @param beginDate
     * @param endDate
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT u1.nick AS sendNick,u2.nick AS receiveNick,g.total_gold_num AS totalGoldNum,g.gift_num AS giftNum,f.gift_name AS giftName,f.pic_url AS giftUrl,date_format( g.create_time, '%H:%i' ) AS time FROM gift_send_record g LEFT JOIN users u1 ON g.uid = u1.uid LEFT JOIN users u2 ON u2.uid = recive_uid LEFT JOIN gift f ON f.gift_id = g.gift_id WHERE g.room_uid=#{roomUid} and g.create_time <= #{endDate} and g.create_time >= #{beginDate} ORDER BY g.create_time ASC")
    List<RoomFlowRecordDetailDTO> listRoomFlowRecordDetail(@Param("roomUid") Long roomUid, @Param("beginDate") String beginDate, @Param("endDate") String endDate);

    /**
     * 保存打call记录
     *
     * @param recordDo
     */
    @TargetDataSource
    void saveCall(GiftSendRecordDO recordDo);

    /**
     * 保存活动礼物记录
     *
     * @param recordDo
     */
    @TargetDataSource
    void saveProp(GiftSendRecordDO recordDo);
}
