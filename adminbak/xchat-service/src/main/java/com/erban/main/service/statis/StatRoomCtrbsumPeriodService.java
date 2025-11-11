package com.erban.main.service.statis;

import com.erban.main.model.StatRoomCtrbSumPeriodExample;
import com.erban.main.model.StatRoomFlowOnlinePeriod;
import com.erban.main.model.StatRoomFlowOnlinePeriodExample;
import com.erban.main.mybatismapper.StatRoomCtrbSumPeriodMapper;
import com.erban.main.mybatismapper.StatRoomCtrbSumPeriodMapperMgr;
import com.erban.main.mybatismapper.StatRoomFlowOnlinePeriodMapper;
import com.google.common.collect.Maps;
import com.xchat.common.utils.BlankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by liuguofu on 2017/10/7.
 */
@Service
public class StatRoomCtrbsumPeriodService {
    private static final Logger logger = LoggerFactory.getLogger(StatRoomCtrbsumPeriodService.class);

    @Autowired
    private StatRoomCtrbSumPeriodMapper statRoomCtrbSumPeriodMapper;
    @Autowired
    private StatRoomCtrbSumPeriodMapperMgr statRoomCtrbSumPeriodMapperMgr;
    @Autowired
    private StatRoomFlowOnlinePeriodMapper statRoomFlowOnlinePeriodMapper;


    /**
     * 删除周期内保存的数据
     */
    public void  deleteAllPriodData(){
        logger.info("do home data---->deleteAllPriodData....................");
        StatRoomCtrbSumPeriodExample statRoomCtrbSumPeriodExample=new StatRoomCtrbSumPeriodExample();
//        statRoomCtrbSumPeriodExample.createCriteria().andUidIsNotNull();
        statRoomCtrbSumPeriodMapper.deleteByExample(statRoomCtrbSumPeriodExample);
        statRoomFlowOnlinePeriodMapper.deleteByExample(new StatRoomFlowOnlinePeriodExample());
    }

    /**
     * 周期内统计的数据保存到DB
     */
    public void genPriodDataHalfHour(){
        logger.info("do home data---->genPriodDataHalfHour....................");
        statRoomCtrbSumPeriodMapperMgr.genPriodDataHalfHour();

        logger.info("do home data---->genRoomFlowOnlinePeriod....................");
        Long totalFlow = statRoomCtrbSumPeriodMapperMgr.selectTotalFlow();
        Long totalOnline = statRoomCtrbSumPeriodMapperMgr.selectTotalOnline();
        statRoomCtrbSumPeriodMapperMgr.genRoomFlowOnlinePeriod(totalFlow, totalOnline);
        logger.info("do home data---->finish....................");
    }

    public List<StatRoomFlowOnlinePeriod> getRoomFlowOnlinePeriodFromDB(List<Long> uids, List<Integer> tagIds, Integer pageNum, Integer pageSize, List<Integer> isPermitRoom) {
        Map<String,Object> param = Maps.newHashMap();
        if(!BlankUtil.isBlank(uids)){
            param.put("recomUids", uids);
        }
        if (!BlankUtil.isBlank(tagIds)) {
            param.put("tagIds", tagIds);
        }
        if (!BlankUtil.isBlank(isPermitRoom)) {
            param.put("isPermitRoom", isPermitRoom);
        }
        param.put("skip",(pageNum - 1) * pageSize);
        param.put("size", pageSize);
        return statRoomCtrbSumPeriodMapperMgr.selectRoomFlowOnlinePeriod(param);
    }

    public List<StatRoomFlowOnlinePeriod> getRoomForFirstCharge(List<Long> uids, Integer pageNum, Integer pageSize) {
        Map<String,Object> param = Maps.newHashMap();
        if(!BlankUtil.isBlank(uids)){
            param.put("recomUids", uids);
        }
        param.put("skip",(pageNum - 1) * pageSize);
        param.put("size", pageSize);
        return statRoomCtrbSumPeriodMapperMgr.getRoomForFirstCharge(param);
    }

    public List<StatRoomFlowOnlinePeriod> getGreenHome(List<Long> uids, Integer pageNum, Integer pageSize) {
        Map<String,Object> param = Maps.newHashMap();
        if(!BlankUtil.isBlank(uids)){
            param.put("recomUids", uids);
        }
        param.put("skip",(pageNum - 1) * pageSize);
        param.put("size", pageSize);
        return statRoomCtrbSumPeriodMapperMgr.getGreenHome(param);
    }

}
