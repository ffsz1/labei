package com.erban.main.mybatismapper;

import com.erban.main.dto.RoomFlowEchartsDTO;
import com.erban.main.dto.RoomFlowEchartsParam;
import com.erban.main.model.*;
import com.erban.main.param.admin.RoomFlowParam;
import com.erban.main.vo.admin.StatRoomFlowVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface GiftSendRecordMapperExpand {

    //获取GiftSendRecord的giftId以及该礼物的次数
    List<GiftSendRecordVo>getGiftSendRecordVoList();

    // 获取今天礼物的记录
    List<GiftSendRecord> getGiftRecordOfToayBySendUid(Long uid);
    List<GiftSendRecord> getGiftRecordOfToayByRecvUid(Long uid);
    List<GiftSendRecord> getGiftRecordOfToayByRoomUid(Long uid);
    // 获取当周礼物的记录
    List<GiftSendRecord> getGiftRecordOfWeekBySendUid(Long uid);
    List<GiftSendRecord> getGiftRecordOfWeekByRecvUid(Long uid);
    List<GiftSendRecord> getGiftRecordOfWeekByRoomUid(Long uid);
    // 获取所有礼物的记录
    List<GiftSendRecord> getGiftRecordOfTotalBySendUid(Long uid);
    List<GiftSendRecord> getGiftRecordOfTotalByRecvUid(Long uid);
    List<GiftSendRecord> getGiftRecordOfTotalByRoomUid(Long uid);

    List<GiftSendRecordVo2> getTotalDiamondListByDate(Date startTime,Date endTime);

    GiftSendRecordVo3 getTotalGoldNumByUid(Long uid);

    GiftSendRecordVo3 getTotalGoldNumByReceiveUid(Long uid);
    List<GiftSendRecordVo3> getTotalGoldNumByUidList();
    List<GiftSendRecordVo3> getTotalGoldNumByReceiveUidList();

    List<StatRoomFlowVo> statRoomFlow(RoomFlowParam roomFlowParam);

    List<GiftSendRecord> sumRoomFlow(List<Long> uid);

    List<GiftSendRecordVo3> getRoomFlowGroupBy(Long roomUid);
    List<GiftSendRecordVo4> getRoomFlowDetail(String beginDate, String endDate, Long roomUid );

    /**
     * 按照星期统计房间两个星期的流水
     * @param beginDate 开始时间
     * @return
     */
    List<RoomFlowWeekVo> sumByTowWeeks(@Param("beginDate") String beginDate, @Param("rooms") List<StatRoomFlowVo> rooms);

    Long countGoldPrice(@Param("roomId") Long roomId);


    /**
     * 统计房间流水
     */
    List<RoomFlowEchartsDTO> roomFlow(RoomFlowEchartsParam param);

    Long countStatRoomTotalFlow(@Param("uid")Long uid);
}
