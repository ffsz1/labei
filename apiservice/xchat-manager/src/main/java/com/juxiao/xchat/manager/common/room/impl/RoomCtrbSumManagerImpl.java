package com.juxiao.xchat.manager.common.room.impl;

import com.juxiao.xchat.dao.room.StatRoomCtrbSumDao;
import com.juxiao.xchat.dao.room.domain.StatRoomCtrbSumDO;
import com.juxiao.xchat.dao.room.dto.StatRoomCtrbSumDTO;
import com.juxiao.xchat.manager.common.room.RoomCtrbSumManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @class: RoomCtrbSumManagerImpl.java
 * @author: chenjunsheng
 * @date 2018/6/20
 */
public class RoomCtrbSumManagerImpl implements RoomCtrbSumManager {

    @Autowired
    private StatRoomCtrbSumDao roomCtrbSumDao;

    @Override
    public void updateRoomCtrbSum(Long uid, Long ctrbUid, Long sumGold) {
//        StatRoomCtrbSumDTO roomCtrbSumDto = roomCtrbSumDao.getUserRoomCtrb(uid, ctrbUid);
//
//
//        Date date = new Date();
//        if (roomCtrbSumDto == null) {
//            StatRoomCtrbSumDO statRoomCtrbSum = new StatRoomCtrbSumDO();
//            statRoomCtrbSum.setUid(uid);
//            statRoomCtrbSum.setSumGold(sumGold);
//            statRoomCtrbSum.setCtrbUid(ctrbUid);
//            statRoomCtrbSum.setCreateTime(date);
//            statRoomCtrbSum.setUpdateTime(date);
//            roomCtrbSumDao.save(statRoomCtrbSum);
//        } else {
//            logger.info("addAndUpdateStatRoomCtrbSumList uid=" + uid + "&ctrbUid=" + ctrbUid + "&sumGold=" + sumGold + " isNotEmpty");
//            StatRoomCtrbSum statRoomCtrbSum = statRoomoCtrbSumList.get(0);
//            Map<String, Object> param = new HashMap<>();
//            param.put("ctrbId", statRoomCtrbSum.getCtrbId());
//            param.put("sumGold", sumGold);
//            roomCtrbSumDao.updateCtrbGoldSum(
//        }
    }

    @Override
    public void updateRoomTotalCtrbSum(Long uid, Long flowSumTotal) {
//        logger.info("addAndUpdateStatRoomCtrbSumTotalList uid=" + uid + "&flowSumTotal=" + flowSumTotal);
//        StatRoomCtrbSumTotal statRoomCtrbSumTotal = statRoomCtrbSumTotalMapper.selectByPrimaryKey(uid);
//        Date date = new Date();
//        if (statRoomCtrbSumTotal == null) {
//            logger.info("addAndUpdateStatRoomCtrbSumTotalList uid=" + uid + "&flowSumTotal=" + flowSumTotal + " isEmpty");
//            statRoomCtrbSumTotal = new StatRoomCtrbSumTotal();
//            statRoomCtrbSumTotal.setUid(uid);
//            statRoomCtrbSumTotal.setCreateTime(date);
//            statRoomCtrbSumTotal.setFlowSumTotal(flowSumTotal);
//            statRoomCtrbSumTotalMapper.insert(statRoomCtrbSumTotal);
//        } else {
//            logger.info("addAndUpdateStatRoomCtrbSumTotalList uid=" + uid + "&flowSumTotal=" + flowSumTotal + " isNotEmpty");
//            Map<String, Object> param = new HashMap<>();
//            param.put("uid", uid);
//            param.put("flowSumTotal", flowSumTotal);
//            statRoomCtrbSumTotalMapperMgr.updateSumGoldTotalByCtrbId(param);
//        }
    }
}
