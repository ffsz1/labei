package com.juxiao.xchat.service.record.room.impl;

import com.google.common.collect.Lists;
import com.juxiao.xchat.base.utils.DESUtils;
import com.juxiao.xchat.base.utils.DateFormatUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.item.GiftSendRecordDao;
import com.juxiao.xchat.dao.item.dto.RoomDrawRecordDTO;
import com.juxiao.xchat.dao.item.dto.RoomFlowRecordDTO;
import com.juxiao.xchat.dao.item.dto.RoomFlowRecordDetailDTO;
import com.juxiao.xchat.dao.room.dto.RoomDTO;
import com.juxiao.xchat.dao.user.UserDrawGiftRoomDayDao;
import com.juxiao.xchat.dao.user.query.DrawGiftRecordQuery;
import com.juxiao.xchat.manager.common.room.RoomManager;
import com.juxiao.xchat.service.record.room.RoomFlowServie;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 房间流水service
 */
@Service
public class RoomFlowServieImpl implements RoomFlowServie {
    private static final String STAT_FLOW_SECRET = "roomFlow";
    private static final Logger logger = LoggerFactory.getLogger(RoomFlowServie.class);
    @Autowired
    private GiftSendRecordDao giftSendRecordDao;
    @Autowired
    private UserDrawGiftRoomDayDao drawGiftRoomDayDao;
    @Autowired
    private RoomManager roomManager;

    /**
     * 最大天数
     */
    private final int MAX_DAYS = 14;

    @Override
    public List<RoomFlowRecordDTO> listRoomFlowRecord(String roomUid) throws Exception {
        if (StringUtils.isBlank(roomUid)) {
            logger.warn("[ 房间流水列表 ]roomUid 不合法,roomUid:{}", roomUid);
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }
        String roomUidStr;
        try {
            roomUidStr = DESUtils.DESAndBase64Decrypt(roomUid, STAT_FLOW_SECRET);
        } catch (Exception e) {
            logger.error("[ 房间流水列表 ]解密房主uid:>{}失败：", roomUid, e);
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        if (StringUtils.isBlank(roomUidStr) || !roomUidStr.matches("[0-9]*")) {
            logger.warn("[ 房间流水列表 ]roomUid 不合法,roomUid:{}", roomUidStr);
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        List<RoomFlowRecordDTO> list = giftSendRecordDao.listRoomFlowGroupBy(Long.valueOf(roomUidStr));
        if (list == null || list.size() == 0) {
            throw new WebServiceException(WebServiceCode.STAT_ROOMFLOW_NOTEXIT);
        }

        List<RoomFlowRecordDTO> rets = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            rets.add(list.get(i));
            if (i == list.size() - 1) {
                break;
            }
            long time1 = list.get(i).getCreateTime().getTime();
            long time2 = list.get(i + 1).getCreateTime().getTime();
            long n = (time1 - time2) / (24 * 60 * 60 * 1000);

            if (n > 1) {//间隔n-1天
                for (int j = 1; j <= n - 1; j++) {
                    RoomFlowRecordDTO giftSendRecordVo3 = new RoomFlowRecordDTO();
                    giftSendRecordVo3.setCreateTime(new Date(time1 - 24 * 60 * 60 * 1000 * j));
                    giftSendRecordVo3.setDate(DateFormatUtils.YYYY_MM_DD.date2Str(giftSendRecordVo3.getCreateTime()));
                    giftSendRecordVo3.setTotalGoldNum(0L);
                    rets.add(giftSendRecordVo3);
                }
            }
        }
        return rets;
    }

    @Override
    public List<RoomFlowRecordDetailDTO> listRoomFlowDetail(String roomUid, String date) throws Exception {
        if (StringUtils.isBlank(roomUid) || StringUtils.isBlank(date)) {
            logger.warn("[ 房间流水详情 ]接口入参错误，roomUid:>{}，date:>{}", roomUid, date);
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        try {
            DateFormatUtils.YYYY_MM_DD.str2Date(date);
        } catch (ParseException e) {
            logger.warn("[ 房间流水详情 ]传入的日期格式不合法，roomUid:>{}，date:>{}", roomUid, date);
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        String roomUidStr = DESUtils.DESAndBase64Decrypt(roomUid, STAT_FLOW_SECRET);
        if (StringUtils.isBlank(roomUidStr) || !roomUidStr.matches("[0-9]*")) {
            logger.warn("[ 房间流水详情 ]roomUid 不合法，roomUid:>{}", roomUidStr);
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        String beginDate = date + " 00:00:00";
        String endDate = date + " 23:59:59";
        long roomUidL = Long.parseLong(roomUidStr);
        List<RoomFlowRecordDetailDTO> details = giftSendRecordDao.listRoomFlowRecordDetail(roomUidL, beginDate, endDate);
        if (details == null || details.size() == 0) {
            throw new WebServiceException(WebServiceCode.STAT_ROOMFLOW_DETAILNOTEXIT);
        }

        return details;
    }

    @Override
    public List<RoomDrawRecordDTO> listRoomDraw(String roomUidStr) throws WebServiceException {
        if (StringUtils.isBlank(roomUidStr)) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        try {
            roomUidStr = DESUtils.DESAndBase64Decrypt(roomUidStr, STAT_FLOW_SECRET);
        } catch (Exception e) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        if (StringUtils.isBlank(roomUidStr) || !StringUtils.isNumeric(roomUidStr)) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        RoomDTO roomDto = roomManager.getUserRoom(Long.valueOf(roomUidStr));
        if (roomDto == null) {
            return Lists.newArrayList();
        }

        logger.info("[查询房间流水]roomId:{}", roomDto.getRoomId());
        List<RoomDrawRecordDTO> list = drawGiftRoomDayDao.listRoomDrawRecord(new DrawGiftRecordQuery(roomDto.getRoomId(), DateUtils.addDays(new Date(), -MAX_DAYS)));
        if (list == null || list.size() == 0) {
            return Lists.newArrayList();
        }

        final List<RoomDrawRecordDTO> rets = new ArrayList<>(list.size());
        rets.addAll(list);

        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                break;
            }

            long time1 = list.get(i).getCreateTime().getTime();
            long time2 = list.get(i + 1).getCreateTime().getTime();
            long n = (time1 - time2) / (24 * 60 * 60 * 1000);
            if (n > 1) {//间隔n-1天
                for (int j = 1; j <= n - 1; j++) {
                    RoomDrawRecordDTO recordDto = new RoomDrawRecordDTO();
                    recordDto.setCreateTime(new Date(time1 - 24 * 60 * 60 * 1000 * j));
                    recordDto.setTotalGoldNum(0L);
                    rets.add(recordDto);
                }
            }
        }

        rets.sort(Comparator.comparing(RoomDrawRecordDTO::getDate).reversed());
        return rets;
    }

//    /**
//     * 查询房间流水记录
//     *
//     * @param roomUidStr
//     * @return
//     */
//    private List<RoomDrawRecordDTO> listRecord(String roomUidStr) {
//        logger.info("[查询房间流水]roomStr:{}", roomUidStr);
//        RoomDTO roomDto = roomManager.getUserRoom(Long.valueOf(roomUidStr));
//        if (roomDto == null) {
//            return Lists.newArrayList();
//        }
//        String cacheValue = redisManager.hget(RedisKey.room_draw_gift_record.getKey(), roomDTO.getUid().toString());
//        if (StringUtils.isBlank(cacheValue)) {
//            DrawGiftRecordQuery query = new DrawGiftRecordQuery(roomDTO.getRoomId(), DateUtils.addDays(new Date(), - MAX_DAYS));
//            List<RoomDrawRecordDTO> list = drawGiftRecordDao.listRoomDraw(query);
//            logger.info("[查询房间流水]缓存为空,roomStr:{}, listSize:{}", roomUidStr, list.size());
//            if (list == null || list.isEmpty()) {
//                return Lists.newArrayList();
//            }
//            redisManager.hset(RedisKey.room_draw_gift_record.getKey(), roomDTO.getUid().toString(), gson.toJson(list));
//            return list;
//        } else {
//            List<RoomDrawRecordDTO> cacheList = gson.fromJson(cacheValue, new TypeToken<List<RoomDrawRecordDTO>>(){}.getType());
//            if (cacheList == null || cacheList.isEmpty()) {
//                return Lists.newArrayList();
//            }
//            Date date = new Date();
//            date = DateUtils.setHours(date, 0);
//            date = DateUtils.setMinutes(date, 0);
//            date = DateUtils.setSeconds(date, 0);
//            // 获取第一条数据,最新的数据
//            RoomDrawRecordDTO dto = cacheList.get(0);
//            if (DateUtils.isSameDay(new Date(), dto.getCreateTime())) {
//                // 同一天, 今天更新过了
//                // 查询今天的流水
//                DrawGiftRecordQuery query = new DrawGiftRecordQuery(roomDTO.getRoomId(), date);
//                List<RoomDrawRecordDTO> list = drawGiftRecordDao.listRoomDraw(query);
//                if (list != null && !list.isEmpty()) {
//                    cacheList.set(0, list.get(0));
//                    redisManager.hset(RedisKey.room_draw_gift_record.getKey(), roomDTO.getUid().toString(), gson.toJson(cacheList));
//                }
//            } else {
//                // 没有今天的数据, 查询两天的数据
//                date = DateUtils.addDays(date, - 1);
//                DrawGiftRecordQuery query = new DrawGiftRecordQuery(roomDTO.getRoomId(), date);
//                List<RoomDrawRecordDTO> list = drawGiftRecordDao.listRoomDraw(query);
//                if (list != null) {
//                    if (list.size() == 2) {
//                        RoomDrawRecordDTO dto2 = list.get(1);
//                        if (DateUtils.isSameDay(dto2.getCreateTime(), cacheList.get(0).getCreateTime())) {
//                            // 更新缓存中前一天的数据
//                            cacheList.set(0, dto2);
//                        }
//                    }
//                    list.addAll(cacheList);
//                    cacheList = list;
//                    if (cacheList.size() > MAX_DAYS) {
//                        // 超出最大长度
//                        cacheList = cacheList.subList(0, MAX_DAYS);
//                    }
//                    redisManager.hset(RedisKey.room_draw_gift_record.getKey(), roomDTO.getUid().toString(), gson.toJson(cacheList));
//                }
//            }
//            return cacheList;
//        }
//    }
}
