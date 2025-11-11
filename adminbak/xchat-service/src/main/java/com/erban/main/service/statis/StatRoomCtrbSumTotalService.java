package com.erban.main.service.statis;

import com.beust.jcommander.internal.Maps;
import com.erban.main.model.*;
import com.erban.main.mybatismapper.StatRoomCtrbSumTotalMapper;
import com.erban.main.mybatismapper.StatRoomCtrbSumTotalMapperMgr;
import com.xchat.oauth2.service.service.JedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuguofu on 2017/10/1.
 */
@Service
public class StatRoomCtrbSumTotalService {
    private static final Logger logger = LoggerFactory.getLogger(StatRoomCtrbSumTotalService.class);
    @Autowired
    private StatRoomCtrbSumTotalMapper statRoomCtrbSumTotalMapper;

    @Autowired
    private StatRoomCtrbSumTotalMapperMgr statRoomCtrbSumTotalMapperMgr;

    @Autowired
    private JedisService jedisService;



    /**
     * 房间流水统计
     * @param uid 收礼物人房间UID
     * @param flowSumTotal
     */
    public void addAndUpdateStatRoomCtrbSumTotalList(Long uid,Long flowSumTotal){
        logger.info("addAndUpdateStatRoomCtrbSumTotalList uid="+uid+"&flowSumTotal="+flowSumTotal);
        StatRoomCtrbSumTotal statRoomCtrbSumTotal=statRoomCtrbSumTotalMapper.selectByPrimaryKey(uid);
        Date date=new Date();
        if(statRoomCtrbSumTotal==null){
            logger.info("addAndUpdateStatRoomCtrbSumTotalList uid="+uid+"&flowSumTotal="+flowSumTotal+" isEmpty");
            statRoomCtrbSumTotal=new StatRoomCtrbSumTotal();
            statRoomCtrbSumTotal.setUid(uid);
            statRoomCtrbSumTotal.setCreateTime(date);
            statRoomCtrbSumTotal.setFlowSumTotal(flowSumTotal);
            statRoomCtrbSumTotalMapper.insert(statRoomCtrbSumTotal);
        }else{
            logger.info("addAndUpdateStatRoomCtrbSumTotalList uid="+uid+"&flowSumTotal="+flowSumTotal+" isNotEmpty");
            Map<String,Object> param=new HashMap<>();
            param.put("uid",uid);
            param.put("flowSumTotal",flowSumTotal);
            statRoomCtrbSumTotalMapperMgr.updateSumGoldTotalByCtrbId(param);
        }
    }

    public Map<Long,StatRoomCtrbSumTotal> getStatRoomCtrbSumTotalList(List<Long> uids){
        StatRoomCtrbSumTotalExample statRoomCtrbSumTotalExample=new StatRoomCtrbSumTotalExample();
        statRoomCtrbSumTotalExample.createCriteria().andUidIn(uids);
        List<StatRoomCtrbSumTotal> statRoomoCtrbSumTotalList=statRoomCtrbSumTotalMapper.selectByExample(statRoomCtrbSumTotalExample);
        Map<Long,StatRoomCtrbSumTotal> data= Maps.newHashMap();
        if(CollectionUtils.isEmpty(statRoomoCtrbSumTotalList)){
            return null;
        }else{
            for(StatRoomCtrbSumTotal statRoomCtrbSumTotal:statRoomoCtrbSumTotalList){
                Long uid=statRoomCtrbSumTotal.getUid();
                data.put(uid,statRoomCtrbSumTotal);
            }
        }
        return data;
    }



}
