package com.erban.main.service.gift;

import com.erban.main.config.SystemConfig;
import com.erban.main.model.*;
import com.erban.main.mybatismapper.GiftCarGetRecordMapper;
import com.erban.main.mybatismapper.GiftCarPurseRecordMapper;
import com.erban.main.param.neteasepush.NeteaseSendMsgParam;
import com.erban.main.service.SendSysMsgService;
import com.erban.main.service.base.CacheBaseService;
import com.erban.main.service.record.BillRecordService;
import com.erban.main.service.user.UserPurseService;
import com.erban.main.service.user.UserPurseUpdateService;
import com.erban.main.service.user.UsersService;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.GiftCarVo;
import com.xchat.common.UUIDUitl;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class GiftCarService extends CacheBaseService<GiftCar, GiftCar> {
    @Autowired
    private UsersService usersService;
    @Autowired
    private GiftCarPurseService giftCarPurseService;
    @Autowired
    private GiftCarPurseRecordMapper giftCarPurseRecordMapper;
    @Autowired
    private UserPurseService userPurseService;
    @Autowired
    private UserPurseUpdateService userPurseUpdateService;
    @Autowired
    private BillRecordService billRecordService;
    @Autowired
    private SendSysMsgService sendSysMsgService;
    @Autowired
    private GiftCarGetRecordMapper giftCarGetRecordMapper;

    @Override
    public GiftCar getOneByJedisId(String jedisId) {
        return getOne(RedisKey.gift_car.getKey(), jedisId, "select * from gift_car where car_id = ? ", jedisId);
    }

    @Override
    public GiftCar entityToCache(GiftCar entity) {
        return entity;
    }

    public String refreshCatListCache(String jedisCode) {
        return refreshListCacheByCode(null, jedisCode, "getCarId", "select * from gift_car where car_status = 1 order by seq_no asc ");
    }

    public List<String> addPurse(String str, Long uid) {
        List<String> ids = new ArrayList<>();
        if (StringUtils.isNotBlank(str)) {
            ids = StringUtils.splitToList(str, ",");
        }
        String purse = giftCarPurseService.getPurse(uid);// 如果座驾无效了，但用户购买过而且还没有到期，就把已购买的座驾放到前面显示
        if (StringUtils.isNotBlank(purse)) {
            String[] pList = purse.split(",");
            GiftCarPurseRecord giftCarPurseRecord;
            for (String p : pList) {
                if (!ids.contains(p)) {
                    giftCarPurseRecord = giftCarPurseService.getOneByJedisId(uid.toString(), p);
                    if (giftCarPurseRecord != null && giftCarPurseRecord.getCarDate() != 0) {
                        ids.add(0, p);
                    }
                }
            }
        }
        return ids;
    }

    public List<GiftCarVo> getCat(Long uid, Integer pageNum, Integer pageSize) {
        List<String> ids;
        String str = jedisService.get(RedisKey.gift_car_list.getKey());
        if (StringUtils.isEmpty(str)) {
            str = refreshCatListCache(RedisKey.gift_car_list.getKey());
        }
        ids = addPurse(str, uid);
        if (ids == null || ids.size() == 0) {
            return new ArrayList<>();
        }
        if (pageNum == null || pageNum < 1) pageNum = 1;
        if (pageSize == null || pageSize < 1) pageSize = 10;
        Integer size = ids.size();
        Integer skip = (pageNum - 1) * pageSize;
        if (skip < size) {
            if (skip + pageSize > size) {
                return toVo(getList(ids.subList(skip, size)), uid);
            } else {
                return toVo(getList(ids.subList(skip, skip + pageSize)), uid);
            }
        }
        return new ArrayList<>();
    }

    public List<GiftCarVo> toVo(List<GiftCar> giftCarList, Long uid) {
        if (giftCarList.size() == 0) {
            return new ArrayList<>();
        }
        GiftCarVo giftCarVo;
        GiftCarPurseRecord giftCarPurseRecord;
        List<GiftCarVo> giftCarVoList = new ArrayList<>();
        for (GiftCar giftCar : giftCarList) {
            giftCarVo = new GiftCarVo();
            giftCarVo.setCarId(giftCar.getCarId());
            giftCarVo.setCarName(giftCar.getCarName());
            giftCarVo.setGoldPrice(giftCar.getGoldPrice());
            giftCarVo.setPicUrl(giftCar.getPicUrl());
            giftCarVo.setHasGifPic(giftCar.getHasGifPic());
            giftCarVo.setGifUrl(giftCar.getGifUrl());
            giftCarVo.setHasVggPic(giftCar.getHasVggPic());
            giftCarVo.setVggUrl(giftCar.getVggUrl());
            giftCarVo.setEffectiveTime(giftCar.getEffectiveTime());
            giftCarVo.setTimeLimit(giftCar.getIsTimeLimit());
            giftCarPurseRecord = giftCarPurseService.getOneByJedisId(uid.toString(), giftCar.getCarId().toString());
            if (giftCarPurseRecord == null) {
                giftCarVo.setIsPurse(0);
                giftCarVo.setDays(0);
                giftCarVo.setDaysRemaining(0);
            } else {
                if (giftCarPurseRecord.getCarDate() == 0) {
                    giftCarVo.setIsPurse(0);
                } else {
                    if (giftCarPurseRecord.getIsUse().intValue() == 1) {
                        giftCarVo.setIsPurse(2);
                    } else {
                        giftCarVo.setIsPurse(1);
                    }
                }
                giftCarVo.setDays(giftCarPurseRecord.getCarDate());
                giftCarVo.setDaysRemaining(giftCarPurseRecord.getCarDate());
            }
            giftCarVoList.add(giftCarVo);
        }
        return giftCarVoList;
    }

    public BusiResult purse(Long uid, Integer carId, Integer type) {
        Users users = usersService.getUsersByUid(uid);
        if (users == null) {
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        GiftCar giftCar = getOneByJedisId(carId.toString());
        if (giftCar == null) {
            return new BusiResult(BusiStatus.GIFTCATNOTEXISTS);
        }
        String lockVal = null;
        try {
            lockVal = jedisLockService.lock(RedisKey.lock_user_purse.getKey(uid.toString()), 10 * 1000);
            if (StringUtils.isEmpty(lockVal)) {
                return new BusiResult(BusiStatus.SERVERBUSY);
            }
            //#################################加锁start#################################
            UserPurse userPurse = userPurseService.getPurseByUid(uid);
            Long goldNum = userPurse.getGoldNum();
            // 判断余额是否足够
            if (goldNum < giftCar.getGoldPrice()) {
                return new BusiResult(BusiStatus.PURSEMONEYNOTENOUGH);
            }
            UserPurse userPurseAfter = userPurseService.updateGoldBySendGiftCache(userPurse, giftCar.getGoldPrice());
            if (userPurseAfter == null) {
                return new BusiResult(BusiStatus.PURSEMONEYNOTENOUGH);
            }
            // 更新送礼物用户的钱包，减金币
            int result = userPurseUpdateService.reduceGoldNumFromDB(uid, giftCar.getGoldPrice());
            if (result != 1) {
                logger.error("reduceGoldFromDB uid:" + uid);
            }
        } catch (Exception e) {
            logger.error("reduceGoldFromCache uid:" + uid, e);
            return new BusiResult(BusiStatus.SERVERERROR);
        } finally {
            jedisLockService.unlock(RedisKey.lock_user_purse.getKey(uid.toString()), lockVal);
        }
        //#################################结束锁end#################################
        doReset(uid);
        String purse = giftCarPurseService.getPurse(uid);
        if (StringUtils.isNotBlank(purse)) {
            if (!StringUtils.splitToList(purse, ",").contains(carId.toString())) {
                purse += "," + carId;
            }
        } else {
            purse = "" + carId;
        }
        jedisService.hwrite(RedisKey.gift_car_purse_list.getKey(), uid.toString(), purse);
        GiftCarPurseRecord giftCarPurseRecord = giftCarPurseService.getOneByJedisId(uid.toString(), carId.toString());
        if (giftCarPurseRecord == null) {
            giftCarPurseRecord = new GiftCarPurseRecord();
            giftCarPurseRecord.setUid(uid);
            giftCarPurseRecord.setCarId(carId.longValue());
            giftCarPurseRecord.setTotalGoldNum(giftCar.getGoldPrice());
            giftCarPurseRecord.setCarDate(giftCar.getEffectiveTime().intValue());
            giftCarPurseRecord.setIsUse(new Byte("1"));
            giftCarPurseRecord.setCreateTime(new Date());
            giftCarPurseRecordMapper.insertSelective(giftCarPurseRecord);
        } else {
            giftCarPurseRecord.setIsUse(new Byte("1"));
            giftCarPurseRecord.setTotalGoldNum(giftCarPurseRecord.getTotalGoldNum() + giftCar.getGoldPrice());
            giftCarPurseRecord.setCarDate(giftCarPurseRecord.getCarDate() + giftCar.getEffectiveTime().intValue());
            giftCarPurseRecordMapper.updateByPrimaryKeySelective(giftCarPurseRecord);
        }
        jedisService.hset(RedisKey.gift_car_purse.getKey(), uid + "_" + carId, gson.toJson(giftCarPurseService.entityToCache(giftCarPurseRecord)));
        BillRecord billRecord = new BillRecord();
        billRecord.setBillId(UUIDUitl.get());
        billRecord.setUid(uid);
        billRecord.setObjId(giftCarPurseRecord.getRecordId().toString());
        billRecord.setObjType(Constant.BillType.purseCar);
        billRecord.setGoldNum(giftCar.getGoldPrice());
        billRecord.setCreateTime(new Date());
        billRecordService.insertBillRecord(billRecord);
        GiftCarGetRecord giftCarGetRecord = new GiftCarGetRecord();
        giftCarGetRecord.setUid(uid);
        giftCarGetRecord.setCarId(carId.longValue());
        giftCarGetRecord.setCarDate(giftCar.getEffectiveTime().intValue());
        giftCarGetRecord.setType(new Byte("1"));
        giftCarGetRecord.setCreateTime(new Date());
        giftCarGetRecordMapper.insert(giftCarGetRecord);
        // 发送消息给用户
        NeteaseSendMsgParam neteaseSendMsgParam = new NeteaseSendMsgParam();
        neteaseSendMsgParam.setFrom(SystemConfig.secretaryUid);
        neteaseSendMsgParam.setOpe(0);
        neteaseSendMsgParam.setType(0);
        neteaseSendMsgParam.setTo(uid.toString());
        neteaseSendMsgParam.setBody("恭喜您成功购买座驾【" + giftCar.getCarName() + "】,快点坐上去游玩吧！");
        sendSysMsgService.sendMsg(neteaseSendMsgParam);
        return new BusiResult(BusiStatus.SUCCESS, "购买成功", "");
    }

    public void doReset(Long uid) {
        List<GiftCarPurseRecord> tList = jdbcTemplate.query("select * from gift_car_purse_record where uid = ? and is_use = 1", new BeanPropertyRowMapper<>(GiftCarPurseRecord.class), uid);
        if (tList != null && tList.size() != 0) {
            GiftCarPurseRecord giftCarPurseRecord1 = tList.get(0);
            giftCarPurseRecord1.setIsUse(new Byte("0"));
            giftCarPurseRecordMapper.updateByPrimaryKeySelective(giftCarPurseRecord1);
            jedisService.hset(RedisKey.gift_car_purse.getKey(), uid.toString() + "_" + giftCarPurseRecord1.getCarId().toString(), gson.toJson(giftCarPurseService.entityToCache(giftCarPurseRecord1)));
        }
    }

    public BusiResult use(Long uid, Integer carId) {
        Users users = usersService.getUsersByUid(uid);
        if (users == null) {
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        if (carId == -1) {// 取消座驾选择
            doReset(uid);
        } else {
            GiftCar giftCar = getOneByJedisId(carId.toString());
            if (giftCar == null) {
                return new BusiResult(BusiStatus.GIFTCATNOTEXISTS);
            }
            GiftCarPurseRecord giftCarPurseRecord = giftCarPurseService.getOneByJedisId(uid.toString(), carId.toString());
            if (giftCarPurseRecord == null || giftCarPurseRecord.getCarDate() == 0) {
                return new BusiResult(BusiStatus.GIFTCATPURSENOTEXISTS);
            }
            doReset(uid);
            giftCarPurseRecord.setIsUse(new Byte("1"));
            giftCarPurseRecordMapper.updateByPrimaryKeySelective(giftCarPurseRecord);
            jedisService.hset(RedisKey.gift_car_purse.getKey(), uid + "_" + carId.toString(), gson.toJson(giftCarPurseService.entityToCache(giftCarPurseRecord)));
        }
        return new BusiResult(BusiStatus.SUCCESS);
    }

    public BusiResult give(Long uid, Integer carId, Long targetUid) {
        Users users = usersService.getUsersByUid(uid);
        if (users == null) {
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        Users users1 = usersService.getUsersByUid(targetUid);
        if (users1 == null) {
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        GiftCar giftCar = getOneByJedisId(carId.toString());
        if (giftCar == null) {
            return new BusiResult(BusiStatus.GIFTCATNOTEXISTS);
        }
        if (giftCar.getCarStatus().intValue() == 2 || giftCar.getGoldPrice().intValue() == 0) {
            return new BusiResult(BusiStatus.NOAUTHORITY, "此类座驾不支持赠送", "");
        }
        String lockVal = null;
        try {
            lockVal = jedisLockService.lock(RedisKey.lock_user_purse.getKey(uid.toString()), 10 * 1000);
            if (StringUtils.isEmpty(lockVal)) {
                return new BusiResult(BusiStatus.SERVERBUSY);
            }
            //#################################加锁start#################################
            UserPurse userPurse = userPurseService.getPurseByUid(uid);
            Long goldNum = userPurse.getGoldNum();
            // 判断余额是否足够
            if (goldNum < giftCar.getGoldPrice()) {
                return new BusiResult(BusiStatus.PURSEMONEYNOTENOUGH);
            }
            UserPurse userPurseAfter = userPurseService.updateGoldBySendGiftCache(userPurse, giftCar.getGoldPrice());
            if (userPurseAfter == null) {
                return new BusiResult(BusiStatus.PURSEMONEYNOTENOUGH);
            }
            // 更新送礼物用户的钱包，减金币
            int result = userPurseUpdateService.reduceGoldNumFromDB(uid, giftCar.getGoldPrice());
            if (result != 1) {
                logger.error("reduceGoldFromDB uid:" + uid);
            }
        } catch (Exception e) {
            logger.error("reduceGoldFromCache uid:" + uid, e);
            return new BusiResult(BusiStatus.SERVERERROR);
        } finally {
            jedisLockService.unlock(RedisKey.lock_user_purse.getKey(uid.toString()), lockVal);
        }
        //#################################结束锁end#################################
        String purse = giftCarPurseService.getPurse(users1.getUid());
        if (StringUtils.isNotBlank(purse)) {
            if (!StringUtils.splitToList(purse, ",").contains(carId.toString())) {
                purse += "," + carId;
            }
        } else {
            purse = "" + carId;
        }
        jedisService.hwrite(RedisKey.gift_car_purse_list.getKey(), users1.getUid().toString(), purse);
        GiftCarPurseRecord giftCarPurseRecord = giftCarPurseService.getOneByJedisId(users1.getUid().toString(), carId.toString());
        if (giftCarPurseRecord == null) {
            giftCarPurseRecord = new GiftCarPurseRecord();
            giftCarPurseRecord.setUid(users1.getUid());
            giftCarPurseRecord.setCarId(carId.longValue());
            giftCarPurseRecord.setTotalGoldNum(giftCar.getGoldPrice());
            giftCarPurseRecord.setCarDate(giftCar.getEffectiveTime().intValue());
            giftCarPurseRecord.setIsUse(new Byte("0"));
            giftCarPurseRecord.setCreateTime(new Date());
            giftCarPurseRecordMapper.insertSelective(giftCarPurseRecord);
        } else {
            giftCarPurseRecord.setTotalGoldNum(giftCarPurseRecord.getTotalGoldNum() + giftCar.getGoldPrice());
            giftCarPurseRecord.setCarDate(giftCarPurseRecord.getCarDate() + giftCar.getEffectiveTime().intValue());
            giftCarPurseRecordMapper.updateByPrimaryKeySelective(giftCarPurseRecord);
        }
        jedisService.hset(RedisKey.gift_car_purse.getKey(), users1.getUid() + "_" + carId.toString(), gson.toJson(giftCarPurseService.entityToCache(giftCarPurseRecord)));
        BillRecord billRecord = new BillRecord();
        billRecord.setBillId(UUIDUitl.get());
        billRecord.setUid(uid);
        billRecord.setObjId(giftCarPurseRecord.getRecordId().toString());
        billRecord.setObjType(Constant.BillType.purseCar);
        billRecord.setGoldNum(giftCar.getGoldPrice());
        billRecord.setCreateTime(new Date());
        billRecordService.insertBillRecord(billRecord);
        GiftCarGetRecord giftCarGetRecord = new GiftCarGetRecord();
        giftCarGetRecord.setUid(users1.getUid());
        giftCarGetRecord.setCarId(carId.longValue());
        giftCarGetRecord.setCarDate(giftCar.getEffectiveTime().intValue());
        giftCarGetRecord.setType(new Byte("5"));
        giftCarGetRecord.setCreateTime(new Date());
        giftCarGetRecordMapper.insert(giftCarGetRecord);
        // 发送消息给用户
        NeteaseSendMsgParam neteaseSendMsgParam = new NeteaseSendMsgParam();
        neteaseSendMsgParam.setFrom(SystemConfig.secretaryUid);
        neteaseSendMsgParam.setOpe(0);
        neteaseSendMsgParam.setType(0);
        neteaseSendMsgParam.setTo(users1.getUid().toString());
        neteaseSendMsgParam.setBody(users.getNick() + "赠送给您座驾【" + giftCar.getCarName() + "】,快点坐上去游玩吧！");
        sendSysMsgService.sendMsg(neteaseSendMsgParam);
        return new BusiResult(BusiStatus.SUCCESS, "赠送成功", "");
    }

    public void refreshAll() {
        Map<String, String> map = jedisService.hgetAll(RedisKey.gift_car_purse.getKey());
        if (map == null || map.size() == 0) {
            return;
        }
        GiftCar giftCar;
        for (String key : map.keySet()) {
            try {
                GiftCarPurseRecord giftCarPurseRecord = gson.fromJson(map.get(key), GiftCarPurseRecord.class);
                if (giftCarPurseRecord.getCarDate() <= 0) {
                    continue;
                }

                giftCarPurseRecord.setCarDate(giftCarPurseRecord.getCarDate() - 1);
                giftCarPurseRecordMapper.updateByPrimaryKeySelective(giftCarPurseRecord);
                jedisService.hset(RedisKey.gift_car_purse.getKey(), giftCarPurseRecord.getUid().toString() + "_" + giftCarPurseRecord.getCarId().toString(), gson.toJson(giftCarPurseService.entityToCache(giftCarPurseRecord)));
                if (giftCarPurseRecord.getCarDate() > 0) {
                    continue;
                }
                giftCar = getOneByJedisId(giftCarPurseRecord.getCarId().toString());
                // 发送消息给用户
                NeteaseSendMsgParam neteaseSendMsgParam = new NeteaseSendMsgParam();
                neteaseSendMsgParam.setFrom(SystemConfig.secretaryUid);
                neteaseSendMsgParam.setOpe(0);
                neteaseSendMsgParam.setType(0);
                neteaseSendMsgParam.setTo(giftCarPurseRecord.getUid().toString());
                neteaseSendMsgParam.setBody("您购买的座驾【" + giftCar.getCarName() + "】已到期,请到座驾管理续费！");
                sendSysMsgService.sendMsg(neteaseSendMsgParam);
            } catch (Exception e) {
                logger.error("refreshGiftCar error", e);
            }
        }
    }

    public void puseFree(Long uid, Integer carId, int effectiveTime) {
        String purse = giftCarPurseService.getPurse(uid);
        if (StringUtils.isNotBlank(purse)) {
            if (!StringUtils.splitToList(purse, ",").contains(carId.toString())) {
                purse += "," + carId;
            }
        } else {
            purse = "" + carId;
        }
        jedisService.hwrite(RedisKey.gift_car_purse_list.getKey(), uid.toString(), purse);

        GiftCarPurseRecord giftCarPurseRecord = giftCarPurseService.getOneByJedisId(uid.toString(), carId.toString());
        if (giftCarPurseRecord == null) {
            giftCarPurseRecord = new GiftCarPurseRecord();
            giftCarPurseRecord.setTotalGoldNum(0L);
            giftCarPurseRecord.setUid(uid);
            giftCarPurseRecord.setCarId(carId.longValue());
            giftCarPurseRecord.setCarDate(effectiveTime);
            giftCarPurseRecord.setIsUse(new Byte("0"));
            giftCarPurseRecord.setCreateTime(new Date());
            giftCarPurseRecordMapper.insertSelective(giftCarPurseRecord);
        } else {
            giftCarPurseRecord.setCarDate(giftCarPurseRecord.getCarDate() + effectiveTime);
            giftCarPurseRecordMapper.updateByPrimaryKeySelective(giftCarPurseRecord);
        }
        jedisService.hset(RedisKey.gift_car_purse.getKey(), uid.toString() + "_" + carId.toString(), gson.toJson(giftCarPurseService.entityToCache(giftCarPurseRecord)));

        GiftCarGetRecord giftCarGetRecord = new GiftCarGetRecord();
        giftCarGetRecord.setUid(uid);
        giftCarGetRecord.setCarId(carId.longValue());
        giftCarGetRecord.setCarDate(effectiveTime);
        giftCarGetRecord.setType(new Byte("7"));
        giftCarGetRecord.setCreateTime(new Date());
        giftCarGetRecordMapper.insert(giftCarGetRecord);
    }

}
