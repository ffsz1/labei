package com.juxiao.xchat.service.task.sysconf.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.DateTimeUtils;
import com.juxiao.xchat.dao.item.GiftCarPurseRecordDao;
import com.juxiao.xchat.dao.item.HeadwearPurseRecordDao;
import com.juxiao.xchat.dao.item.domain.GiftCarPurseRecordDO;
import com.juxiao.xchat.dao.item.domain.HeadwearPurseRecordDO;
import com.juxiao.xchat.dao.item.dto.GiftCarDTO;
import com.juxiao.xchat.dao.item.dto.GiftCarPurseRecordDTO;
import com.juxiao.xchat.dao.item.dto.HeadwearDTO;
import com.juxiao.xchat.dao.item.dto.HeadwearPurseRecordDTO;
import com.juxiao.xchat.dao.room.HomeHotManualRecommDao;
import com.juxiao.xchat.dao.room.domain.HomeHotManualRecommDO;
import com.juxiao.xchat.dao.room.dto.HomeRoomFlowPeriod;
import com.juxiao.xchat.dao.sysconf.dto.SysConfDTO;
import com.juxiao.xchat.dao.sysconf.enumeration.SysConfigId;
import com.juxiao.xchat.dao.task.TaskDao;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.common.item.GiftCarManager;
import com.juxiao.xchat.manager.common.item.HeadwearManager;
import com.juxiao.xchat.manager.common.room.RoomManager;
import com.juxiao.xchat.manager.common.room.vo.RoomVo;
import com.juxiao.xchat.manager.common.sysconf.SysConfManager;
import com.juxiao.xchat.manager.external.async.AsyncNetEaseTrigger;
import com.juxiao.xchat.manager.external.netease.NetEaseMsgManager;
import com.juxiao.xchat.service.task.sysconf.SysService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class SysTaskServiceImpl implements SysService {
    private final Logger logger = LoggerFactory.getLogger(SysTaskServiceImpl.class);
    @Autowired
    private Gson gson;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private GiftCarPurseRecordDao carPurseRecordDao;
    @Autowired
    private HeadwearPurseRecordDao headwearPurseRecordDao;
    @Autowired
    private HomeHotManualRecommDao homeHotManualRecommDao;
    @Autowired
    private TaskDao taskDao;

    @Autowired
    private SystemConf systemConf;


    @Autowired
    private GiftCarManager giftCarManager;
    @Autowired
    private HeadwearManager headwearManager;
    @Autowired
    private NetEaseMsgManager neteaseMsgManager;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private SysConfManager sysConfManager;

    @Autowired
    private RoomManager roomManager;

    @Autowired
    private AsyncNetEaseTrigger asyncNetEaseTrigger;


    @Override
    public void refreshGiftCar() {
        Map<String, String> map = redisManager.hgetAll(RedisKey.gift_car_purse.getKey());
        if (map == null || map.size() == 0) {
            return;
        }
        GiftCarDTO giftCar;
        GiftCarPurseRecordDTO giftCarPurseRecord;
        GiftCarPurseRecordDO recordDo;
        for (String key : map.keySet()) {
            try {
                giftCarPurseRecord = gson.fromJson(map.get(key), GiftCarPurseRecordDTO.class);
                if (giftCarPurseRecord.getCarDate() > 0) {
                    giftCarPurseRecord.setCarDate(giftCarPurseRecord.getCarDate() - 1);
                    recordDo = new GiftCarPurseRecordDO();
                    recordDo.setRecordId(giftCarPurseRecord.getRecordId());
                    recordDo.setCarDate(giftCarPurseRecord.getCarDate());
                    carPurseRecordDao.update(recordDo);
                    redisManager.hset(RedisKey.gift_car_purse.getKey(), giftCarPurseRecord.getUid().toString() + "_" + giftCarPurseRecord.getCarId().toString(), gson.toJson(giftCarPurseRecord));
                    if (giftCarPurseRecord.getCarDate() == 0) {
                        giftCar = giftCarManager.getGiftCar(giftCarPurseRecord.getCarId().intValue());
                        // 发送消息给用户
                        if (Boolean.TRUE.equals(giftCar.getAllowPurse())) {
                            asyncNetEaseTrigger.sendMsg(String.valueOf(giftCarPurseRecord.getUid()),"您购买的座驾【" + giftCar.getCarName() + "】已到期,请到座驾管理续费！");
                        } else {
                            // 不允许购买的,不提示续费
                            asyncNetEaseTrigger.sendMsg(String.valueOf(giftCarPurseRecord.getUid()),"您购买的座驾【" + giftCar.getCarName() + "】已到期! ");
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("[ 刷新座驾失败 ]", e);
            }
        }
    }

    @Override
    public void refreshHeadwear() {
        Map<String, String> map = redisManager.hgetAll(RedisKey.headwear_purse.getKey());
        if (map == null || map.size() == 0) {
            return;
        }
        HeadwearDTO headwear;
        HeadwearPurseRecordDTO headwearPurseRecord;
        HeadwearPurseRecordDO headwearPurseRecordDO;
        for (String key : map.keySet()) {
            try {
                headwearPurseRecord = gson.fromJson(map.get(key), HeadwearPurseRecordDTO.class);
                if (headwearPurseRecord.getHeadwearDate() > 0 && headwearPurseRecord.getHeadwearId() != 1) {// 等级头饰不用刷新，是永久的
                    headwearPurseRecord.setHeadwearDate(headwearPurseRecord.getHeadwearDate() - 1);
                    headwearPurseRecordDO = new HeadwearPurseRecordDO();
                    headwearPurseRecordDO.setRecordId(headwearPurseRecord.getRecordId());
                    headwearPurseRecordDO.setHeadwearDate(headwearPurseRecord.getHeadwearDate());
                    headwearPurseRecordDao.update(headwearPurseRecordDO);
                    redisManager.hset(RedisKey.headwear_purse.getKey(), headwearPurseRecord.getUid().toString() + "_" + headwearPurseRecord.getHeadwearId().toString(), gson.toJson(headwearPurseRecord));
                    if (headwearPurseRecord.getHeadwearDate() == 0) {
                        headwear = headwearManager.getHeadwear(headwearPurseRecord.getHeadwearId().intValue());
                        if (Boolean.TRUE.equals(headwear.getAllowPurse())) {
                            asyncNetEaseTrigger.sendMsg(String.valueOf(headwearPurseRecord.getUid()),"您购买的头饰【" + headwear.getHeadwearName() + "】已到期,请到头饰管理续费！");
                        } else {
                            // 不允许购买的,不提示续费
                            asyncNetEaseTrigger.sendMsg(String.valueOf(headwearPurseRecord.getUid()),"您购买的头饰【" + headwear.getHeadwearName() + "】已到期! ");
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("[ 刷新头饰失败 ]", e);
            }
        }
    }

    @Override
    public void checkGiftCar() {
        try {
            List<GiftCarPurseRecordDTO> list = taskDao.findAllGiftCar();
            GiftCarPurseRecordDTO cache;
            for (GiftCarPurseRecordDTO dto : list) {
                cache = giftCarManager.getUserCarPurseRecord(dto.getUid(), dto.getCarId());
                if (cache == null || dto.getCarDate().equals(cache.getCarDate())) {
                    logger.error("[ 座驾差异 ], uid:{} id:{}", dto.getUid(), dto.getCarId());
                }
            }
        } catch (Exception e) {
            logger.error("[ 检查座驾信息 ]", e);
        }
    }

    @Override
    public void checkHeadwear() {
        try {
            List<HeadwearPurseRecordDTO> list = taskDao.findAllHeadwear();
            HeadwearPurseRecordDTO cache;
            for (HeadwearPurseRecordDTO dto : list) {
                cache = headwearManager.getHeadwearRecord(dto.getUid(), dto.getHeadwearId().intValue());
                if (cache == null || dto.getHeadwearDate() != cache.getHeadwearDate()) {
                    logger.error("[ 头饰差异 ], uid:{} id:{}", dto.getUid(), dto.getHeadwearId());
                }
            }
        } catch (Exception e) {
            logger.error("[ 检查头饰信息 ]", e);
        }
    }

    @Override
    public void checkSaveLastHourRecom() {
        try {
            Date startValidTime = new Date();
            Date endValidTime = DateTimeUtils.addHours(startValidTime, 1);
            Date date = DateTimeUtils.addHours(startValidTime, -1);
            Integer tol = 30000;
            SysConfDTO hourRecom = sysConfManager.getSysConf(SysConfigId.hour_recom);
            if (hourRecom != null) {
                try {
                    tol = Integer.valueOf(hourRecom.getConfigValue());
                } catch (Exception e) {
                    logger.error("推荐位金额配置错误");
                }
            }
            List<HomeRoomFlowPeriod> list = jdbcTemplate.query("select r.uid,r.online_num as onlineNum,1 as seqNo,SUM(o.total_gold_num) as tol from gift_send_record o INNER JOIN room r on o.room_uid = r.uid where o.create_time BETWEEN ? and ? GROUP BY o.room_uid HAVING tol >= ? ORDER BY tol desc", new BeanPropertyRowMapper<>(HomeRoomFlowPeriod.class), date, startValidTime, tol);
            if (list != null && list.size() > 0) {
                HomeHotManualRecommDO homeHotManualRecomm;
                for (HomeRoomFlowPeriod l : list) {
                    homeHotManualRecomm = new HomeHotManualRecommDO();
                    homeHotManualRecomm.setUid(l.getUid());
                    homeHotManualRecomm.setSeqNo(1);
                    homeHotManualRecomm.setStatus(new Byte("1"));
                    homeHotManualRecomm.setStartValidTime(startValidTime);
                    homeHotManualRecomm.setEndValidTime(endValidTime);
                    homeHotManualRecomm.setCreateTime(startValidTime);
                    homeHotManualRecomm.setType(2);
                    homeHotManualRecommDao.save(homeHotManualRecomm);
                }
            }
        } catch (Exception e) {
            logger.error("[ 每小时消费金额达三万金币以上进入推荐位 ]", e);
        }

    }

    @Override
    public void checkSaveQuickLastHourRecom() {
        try {
            Date startValidTime = new Date();
            Date endValidTime = DateTimeUtils.addHours(startValidTime, 1);
            List<RoomVo> list = jdbcTemplate.query("select uid,room_id as roomId,online_num as onlineNum,valid,title,type,is_permit_room as isPermitRoom,tag_id as tagId from room  where online_num >= 15", new BeanPropertyRowMapper<>(RoomVo.class));
            if (list != null && list.size() > 0) {
                list = filteRobotRoom(list);
                if(list.size() > 0){
                    list.forEach(item ->{
                        HomeHotManualRecommDO homeHotManualRecomm = new HomeHotManualRecommDO();
                        homeHotManualRecomm.setUid(item.getUid());
                        homeHotManualRecomm.setSeqNo(1);
                        homeHotManualRecomm.setStatus(new Byte("1"));
                        homeHotManualRecomm.setStartValidTime(startValidTime);
                        homeHotManualRecomm.setEndValidTime(endValidTime);
                        homeHotManualRecomm.setCreateTime(startValidTime);
                        homeHotManualRecomm.setType(3);
                        homeHotManualRecommDao.save(homeHotManualRecomm);
                    });
                }
            }
        } catch (Exception e) {
            logger.error("[ 每小时房间活人大于等于50人以上进入推荐位 ]", e);
        }
    }

    /**
     * 过滤存在机器人
     * @param rooms rooms
     * @return List<RoomVo>
     */
    private List<RoomVo> filteRobotRoom(List<RoomVo> rooms) {
        Iterator<RoomVo> it = rooms.iterator();
        while (it.hasNext()) {
            RoomVo roomVo = it.next();
            int robotNum = roomManager.getRobotNum(roomVo.getUid());
            if (robotNum <= 0) {
                continue;
            }

            if (roomVo.getOnlineNum() < (robotNum + 50)) {
                it.remove();
            }
        }
        return rooms;

    }
}
