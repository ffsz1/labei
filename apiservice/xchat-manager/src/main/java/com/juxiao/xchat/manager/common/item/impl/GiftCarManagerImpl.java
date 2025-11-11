package com.juxiao.xchat.manager.common.item.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.UUIDUtils;
import com.juxiao.xchat.dao.bill.BillItemCarDao;
import com.juxiao.xchat.dao.bill.BillRecordDao;
import com.juxiao.xchat.dao.bill.domain.BillItemCarDO;
import com.juxiao.xchat.dao.bill.domain.BillRecordDO;
import com.juxiao.xchat.dao.bill.enumeration.BillRecordType;
import com.juxiao.xchat.dao.item.GiftCarDao;
import com.juxiao.xchat.dao.item.GiftCarGetRecordDao;
import com.juxiao.xchat.dao.item.GiftCarPurseRecordDao;
import com.juxiao.xchat.dao.item.domain.GiftCarGetRecordDO;
import com.juxiao.xchat.dao.item.domain.GiftCarPurseRecordDO;
import com.juxiao.xchat.dao.item.dto.GiftCarDTO;
import com.juxiao.xchat.dao.item.dto.GiftCarPurseRecordDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.common.item.GiftCarManager;
import com.juxiao.xchat.manager.external.async.AsyncNetEaseTrigger;
import com.juxiao.xchat.manager.external.netease.NetEaseMsgManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 座驾操作通用类
 *
 * @class: GiftCarPurseManagerImpl.java
 * @author: chenjunsheng
 * @date 2018/6/8
 */
@Service
public class GiftCarManagerImpl implements GiftCarManager {
    @Autowired
    private BillRecordDao recordDao;
    @Autowired
    private GiftCarDao carDao;
    @Autowired
    private GiftCarPurseRecordDao carPurseRecordDao;
    @Autowired
    private GiftCarGetRecordDao carGetRecordDao;
    @Autowired
    private Gson gson;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private SystemConf systemConf;
    @Autowired
    private NetEaseMsgManager neteaseMsgManager;
    @Autowired
    private BillItemCarDao itemCarDao;

    @Autowired
    private AsyncNetEaseTrigger asyncNetEaseTrigger;

    /**
     * 用户获得座驾
     *
     * @param uid
     * @param carId
     * @param carDate
     */
    @Override
    public void saveUserCar(Long uid, Integer carId, Integer carDate, Integer type, String message) {
        List<String> carPurses = this.listUserCarids(uid);
        if (carPurses == null) {
            carPurses = Lists.newArrayList(String.valueOf(carId));
        } else if (!carPurses.contains(String.valueOf(carId))) {
            //carPurses.add(String.valueOf(carId));
        	carPurses.add(0,String.valueOf(carId));
        }
        redisManager.hset(RedisKey.gift_car_purse_list.getKey(), String.valueOf(uid), StringUtils.join(carPurses, ","));

        GiftCarPurseRecordDTO recordDto = this.getUserCarPurseRecord(uid, carId.longValue());
        GiftCarPurseRecordDO recordDo = new GiftCarPurseRecordDO();
        if (recordDto == null) {
            recordDo.setUid(uid);
            recordDo.setCarId(carId.longValue());
            recordDo.setTotalGoldNum(0L);
            recordDo.setIsUse(Byte.parseByte(type == 1 ? "1" : "0"));
            recordDo.setCarDate(carDate);
            recordDo.setCreateTime(new Date());
            carPurseRecordDao.save(recordDo);

            recordDto = new GiftCarPurseRecordDTO();
            BeanUtils.copyProperties(recordDo, recordDto);
        } else {
            if (type == 1) {
                recordDo.setIsUse(new Byte("1"));
            }
            recordDo.setRecordId(recordDto.getRecordId());
            recordDo.setCarDate(recordDto.getCarDate() + carDate);
            carPurseRecordDao.update(recordDo);
            recordDto.setIsUse(recordDo.getIsUse());
            recordDto.setCarDate(recordDo.getCarDate());
        }
        redisManager.hset(RedisKey.gift_car_purse.getKey(), uid + "_" + carId, gson.toJson(recordDto));

        if (type == 1) {
            BillItemCarDO itemCarDo = new BillItemCarDO();
            itemCarDo.setRecordId(recordDto.getRecordId());
            itemCarDo.setUid(uid);
            itemCarDo.setGoldCost(0);
            itemCarDo.setCreateTime(new Date());
            itemCarDao.save(itemCarDo);

            // FIXME: 兼容管理后台
            BillRecordDO billRecord = new BillRecordDO();
            billRecord.setBillId(UUIDUtils.get());
            billRecord.setUid(uid);
            billRecord.setObjId(recordDto.getRecordId().toString());
            billRecord.setObjType(BillRecordType.purseCar);
            billRecord.setGoldNum(0L);
            billRecord.setCreateTime(new Date());
            recordDao.save(billRecord);
        }

        GiftCarGetRecordDO carGetDo = new GiftCarGetRecordDO();
        carGetDo.setUid(uid);
        carGetDo.setCarId(carId.longValue());
        carGetDo.setCarDate(carDate);
        carGetDo.setType(type.byteValue());
        carGetDo.setCreateTime(new Date());
        carGetRecordDao.save(carGetDo);

        if (message != null) {
            asyncNetEaseTrigger.sendMsg(String.valueOf(uid),message);
        }
    }
    /**
     * 用户获得座驾 limself 2020/10/21
     * @param fromUid 赠送人
     * @param uid 接受人
     * @param carId
     * @param carDate
     */
    @Override
    public void saveUserCarWithSelfMsg(Long fromUid,Long uid, Integer carId, Integer carDate, Integer type, String message) {
        List<String> carPurses = this.listUserCarids(uid);
        if (carPurses == null) {
            carPurses = Lists.newArrayList(String.valueOf(carId));
        } else if (!carPurses.contains(String.valueOf(carId))) {
            //carPurses.add(String.valueOf(carId));
            carPurses.add(0,String.valueOf(carId));
        }
        redisManager.hset(RedisKey.gift_car_purse_list.getKey(), String.valueOf(uid), StringUtils.join(carPurses, ","));

        GiftCarPurseRecordDTO recordDto = this.getUserCarPurseRecord(uid, carId.longValue());
        GiftCarPurseRecordDO recordDo = new GiftCarPurseRecordDO();
        if (recordDto == null) {
            recordDo.setUid(uid);
            recordDo.setCarId(carId.longValue());
            recordDo.setTotalGoldNum(0L);
            recordDo.setIsUse(Byte.parseByte(type == 1 ? "1" : "0"));
            recordDo.setCarDate(carDate);
            recordDo.setCreateTime(new Date());
            carPurseRecordDao.save(recordDo);

            recordDto = new GiftCarPurseRecordDTO();
            BeanUtils.copyProperties(recordDo, recordDto);
        } else {
            if (type == 1) {
                recordDo.setIsUse(new Byte("1"));
            }
            recordDo.setRecordId(recordDto.getRecordId());
            recordDo.setCarDate(recordDto.getCarDate() + carDate);
            carPurseRecordDao.update(recordDo);
            recordDto.setIsUse(recordDo.getIsUse());
            recordDto.setCarDate(recordDo.getCarDate());
        }
        redisManager.hset(RedisKey.gift_car_purse.getKey(), uid + "_" + carId, gson.toJson(recordDto));

        if (type == 1) {
            BillItemCarDO itemCarDo = new BillItemCarDO();
            itemCarDo.setRecordId(recordDto.getRecordId());
            itemCarDo.setUid(uid);
            itemCarDo.setGoldCost(0);
            itemCarDo.setCreateTime(new Date());
            itemCarDao.save(itemCarDo);

            // FIXME: 兼容管理后台
            BillRecordDO billRecord = new BillRecordDO();
            billRecord.setBillId(UUIDUtils.get());
            billRecord.setUid(uid);
            billRecord.setObjId(recordDto.getRecordId().toString());
            billRecord.setObjType(BillRecordType.purseCar);
            billRecord.setGoldNum(0L);
            billRecord.setCreateTime(new Date());
            recordDao.save(billRecord);
        }

        GiftCarGetRecordDO carGetDo = new GiftCarGetRecordDO();
        carGetDo.setUid(uid);
        carGetDo.setCarId(carId.longValue());
        carGetDo.setCarDate(carDate);
        carGetDo.setType(type.byteValue());
        carGetDo.setCreateTime(new Date());
        carGetRecordDao.save(carGetDo);

/*        if (message != null) {
           // asyncNetEaseTrigger.sendMsg(String.valueOf(uid),message);
            asyncNetEaseTrigger.sendMsg(String.valueOf(fromUid),String.valueOf(uid),message);
        }*/
    }

    @Override
    public GiftCarDTO getGiftCar(Integer carId) {
        String giftCarStr = redisManager.hget(RedisKey.gift_car.getKey(), String.valueOf(carId));
        if (StringUtils.isNotBlank(giftCarStr)) {
            GiftCarDTO carDTO = gson.fromJson(giftCarStr, GiftCarDTO.class);
            if (carDTO != null && carDTO.getAllowPurse() != null) {
                return carDTO;
            }
        }

        GiftCarDTO carDto = carDao.getGiftCar(carId);
        if (carDto != null) {
            redisManager.hset(RedisKey.gift_car.getKey(), String.valueOf(carId), gson.toJson(carDto));
        }
        return carDto;
    }

    /**
     * 获取用户某个座驾
     *
     * @param uid
     * @param carId
     * @return
     */
    @Override
    public GiftCarPurseRecordDTO getUserCarPurseRecord(Long uid, Long carId) {
        String carpurseStr = redisManager.hget(RedisKey.gift_car_purse.getKey(), uid + "_" + carId);
        if (StringUtils.isNotBlank(carpurseStr)) {
            return gson.fromJson(carpurseStr, GiftCarPurseRecordDTO.class);
        }

        GiftCarPurseRecordDTO recordDto = carPurseRecordDao.getUserCarPurseRecord(uid, carId);
        if (recordDto != null) {
            redisManager.hset(RedisKey.gift_car_purse.getKey(), uid + "_" + carId, gson.toJson(recordDto));
        }
        return recordDto;
    }

    /**
     * 获取用户座驾（ID）
     *
     * @param uid
     * @return
     */
    @Override
    public List<String> listUserCarids(Long uid) {
        String str = redisManager.hget(RedisKey.gift_car_purse_list.getKey(), String.valueOf(uid));
        if (StringUtils.isNotBlank(str)) {
            return Lists.newArrayList(str.split(","));
        }

        List<Integer> list = carPurseRecordDao.listUserPurseCarids(uid);
        List<String> carids = Lists.newArrayList();
        if (list != null) {
            for (Integer carId : list) {
                carids.add(String.valueOf(carId));
            }
            redisManager.hset(RedisKey.gift_car_purse_list.getKey(), uid.toString(), StringUtils.join(carids, ","));
        }

        return carids;
    }

    @Override
    public List<String> listAllGiftCarids() {
        String carids = redisManager.get(RedisKey.gift_car_list.getKey());
        if (StringUtils.isNotBlank(carids)) {
            return Lists.newArrayList(carids.split(","));
        }

        List<String> list = carDao.listAllCarids();
        if (list != null && list.size() > 0) {
            redisManager.set(RedisKey.gift_car_list.getKey(), StringUtils.join(list, ","));
        }
        return list;
    }

    @Override
    public GiftCarDTO getUserGiftCar(Long uid) {
        List<String> carIdPurse = this.listUserCarids(uid);
        if (carIdPurse == null || carIdPurse.size() == 0) {
            return null;
        }
        GiftCarPurseRecordDTO purseRecord;
        for (String carId : carIdPurse) {
            purseRecord = this.getUserCarPurseRecord(uid, Long.valueOf(carId));
            if (purseRecord == null) {
                continue;
            }
            if (purseRecord.getIsUse() == null || purseRecord.getIsUse().intValue() != 1) {
                continue;
            }

            if (purseRecord.getCarDate() == null || purseRecord.getCarDate() == 0) {
                continue;
            }

            return this.getGiftCar(Integer.valueOf(carId));
        }
        return null;
    }


    @Override
    public List<String> listByMall() {
        String carIds = redisManager.get(RedisKey.gift_car_mall.getKey());
        if (StringUtils.isNotBlank(carIds)) {
            return Lists.newArrayList(carIds.split(","));
        }
        List<String> list = carDao.listByMall();
        if (list != null && list.size() > 0) {
            redisManager.set(RedisKey.gift_car_mall.getKey(), StringUtils.join(list, ","));
        }
        return list;
    }
}
