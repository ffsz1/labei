package com.juxiao.xchat.manager.common.item.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.UUIDUtils;
import com.juxiao.xchat.dao.bill.BillItemHeadwearDao;
import com.juxiao.xchat.dao.bill.BillRecordDao;
import com.juxiao.xchat.dao.bill.domain.BillItemHeadwearDO;
import com.juxiao.xchat.dao.bill.domain.BillRecordDO;
import com.juxiao.xchat.dao.bill.enumeration.BillRecordType;
import com.juxiao.xchat.dao.item.HeadwearDao;
import com.juxiao.xchat.dao.item.HeadwearGetRecordDao;
import com.juxiao.xchat.dao.item.HeadwearPurseRecordDao;
import com.juxiao.xchat.dao.item.domain.HeadwearGetRecordDO;
import com.juxiao.xchat.dao.item.domain.HeadwearPurseRecordDO;
import com.juxiao.xchat.dao.item.dto.HeadwearDTO;
import com.juxiao.xchat.dao.item.dto.HeadwearPurseRecordDTO;
import com.juxiao.xchat.dao.item.query.UserHeadwearRecordQuery;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.common.item.HeadwearManager;
import com.juxiao.xchat.manager.external.async.AsyncNetEaseTrigger;
import com.juxiao.xchat.manager.external.netease.NetEaseMsgManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @class: HeadwearManagerImpl.java
 * @author: chenjunsheng
 * @date 2018/6/15
 */
@Service
public class HeadwearManagerImpl implements HeadwearManager {
    @Autowired
    private SystemConf systemConf;
    @Autowired
    private BillRecordDao recordDao;
    @Autowired
    private HeadwearDao headwearDao;
    @Autowired
    private HeadwearGetRecordDao headwearGetRecordDao;
    @Autowired
    private HeadwearPurseRecordDao headwearPurseRecordDao;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private NetEaseMsgManager neteaseMsgManager;
    @Autowired
    private Gson gson;
    @Autowired
    private BillItemHeadwearDao billHeadwearDao;
    @Autowired
    private AsyncNetEaseTrigger asyncNetEaseTrigger;

    @Override
    public void saveUserHeadwear(Long uid, Integer headwearId, Integer headwearDate, Integer type, String message) {
        List<String> headwearids = this.listUserHeadwearid(uid);
        if (headwearids == null) {
            headwearids = Lists.newArrayList(String.valueOf(headwearId));
        } else if (!headwearids.contains(String.valueOf(headwearId))) {
            //headwearids.add(String.valueOf(headwearId));
            headwearids.add(0, String.valueOf(headwearId));
        }
        redisManager.hset(RedisKey.headwear_purse_list.getKey(), String.valueOf(uid), StringUtils.join(headwearids, ","));

        HeadwearPurseRecordDTO headwearPurseRecordDTO = getHeadwearRecord(uid, headwearId);
        HeadwearPurseRecordDO recordDo = new HeadwearPurseRecordDO();
        if (headwearPurseRecordDTO == null) {
            recordDo.setUid(uid);
            recordDo.setHeadwearId(headwearId.longValue());
            recordDo.setTotalGoldNum(0L);
            recordDo.setIsUse(Byte.parseByte(type == 1 ? "1" : "0"));
            recordDo.setHeadwearDate(headwearDate);
            recordDo.setCreateTime(new Date());
            headwearPurseRecordDao.save(recordDo);

            headwearPurseRecordDTO = new HeadwearPurseRecordDTO();
            BeanUtils.copyProperties(recordDo, headwearPurseRecordDTO);
        } else {
            if (type == 1) {
                recordDo.setIsUse(type.byteValue());
            }
            recordDo.setRecordId(headwearPurseRecordDTO.getRecordId());
            recordDo.setHeadwearDate(headwearPurseRecordDTO.getHeadwearDate() + headwearDate);
            headwearPurseRecordDao.update(recordDo);
            headwearPurseRecordDTO.setIsUse(recordDo.getIsUse());
            headwearPurseRecordDTO.setHeadwearDate(recordDo.getHeadwearDate());
        }
        redisManager.hset(RedisKey.headwear_purse.getKey(), uid + "_" + headwearId, gson.toJson(headwearPurseRecordDTO));

        if (type == 1) {
            BillItemHeadwearDO headwearDo = new BillItemHeadwearDO();
            headwearDo.setRecordId(headwearPurseRecordDTO.getRecordId());
            headwearDo.setUid(uid);
            headwearDo.setGoldCost(0);
            headwearDo.setCreateTime(new Date());
            billHeadwearDao.save(headwearDo);

            // FIXME: 兼容管理后台
            BillRecordDO billRecord = new BillRecordDO();
            billRecord.setBillId(UUIDUtils.get());
            billRecord.setUid(uid);
            billRecord.setObjId(headwearPurseRecordDTO.getRecordId().toString());
            billRecord.setObjType(BillRecordType.purseHeadwear);
            billRecord.setGoldNum(0L);
            billRecord.setCreateTime(new Date());
            recordDao.save(billRecord);
        }

        HeadwearGetRecordDO getRecordDo = new HeadwearGetRecordDO();
        getRecordDo.setUid(uid);
        getRecordDo.setHeadwearId(headwearId.longValue());
        getRecordDo.setHeadwearDate(headwearDate);
        getRecordDo.setType(type.byteValue());
        getRecordDo.setCreateTime(new Date());
        headwearGetRecordDao.save(getRecordDo);

        if (message != null) {
            asyncNetEaseTrigger.sendMsg(String.valueOf(uid),message);
        }
    }
    @Override
    public void saveUserHeadwearWithSelfMsg(Long fromUid,Long uid, Integer headwearId, Integer headwearDate, Integer type, String message) {
        List<String> headwearids = this.listUserHeadwearid(uid);
        if (headwearids == null) {
            headwearids = Lists.newArrayList(String.valueOf(headwearId));
        } else if (!headwearids.contains(String.valueOf(headwearId))) {
            //headwearids.add(String.valueOf(headwearId));
            headwearids.add(0, String.valueOf(headwearId));
        }
        redisManager.hset(RedisKey.headwear_purse_list.getKey(), String.valueOf(uid), StringUtils.join(headwearids, ","));

        HeadwearPurseRecordDTO headwearPurseRecordDTO = getHeadwearRecord(uid, headwearId);
        HeadwearPurseRecordDO recordDo = new HeadwearPurseRecordDO();
        if (headwearPurseRecordDTO == null) {
            recordDo.setUid(uid);
            recordDo.setHeadwearId(headwearId.longValue());
            recordDo.setTotalGoldNum(0L);
            recordDo.setIsUse(Byte.parseByte(type == 1 ? "1" : "0"));
            recordDo.setHeadwearDate(headwearDate);
            recordDo.setCreateTime(new Date());
            headwearPurseRecordDao.save(recordDo);

            headwearPurseRecordDTO = new HeadwearPurseRecordDTO();
            BeanUtils.copyProperties(recordDo, headwearPurseRecordDTO);
        } else {
            if (type == 1) {
                recordDo.setIsUse(type.byteValue());
            }
            recordDo.setRecordId(headwearPurseRecordDTO.getRecordId());
            recordDo.setHeadwearDate(headwearPurseRecordDTO.getHeadwearDate() + headwearDate);
            headwearPurseRecordDao.update(recordDo);
            headwearPurseRecordDTO.setIsUse(recordDo.getIsUse());
            headwearPurseRecordDTO.setHeadwearDate(recordDo.getHeadwearDate());
        }
        redisManager.hset(RedisKey.headwear_purse.getKey(), uid + "_" + headwearId, gson.toJson(headwearPurseRecordDTO));

        if (type == 1) {
            BillItemHeadwearDO headwearDo = new BillItemHeadwearDO();
            headwearDo.setRecordId(headwearPurseRecordDTO.getRecordId());
            headwearDo.setUid(uid);
            headwearDo.setGoldCost(0);
            headwearDo.setCreateTime(new Date());
            billHeadwearDao.save(headwearDo);

            // FIXME: 兼容管理后台
            BillRecordDO billRecord = new BillRecordDO();
            billRecord.setBillId(UUIDUtils.get());
            billRecord.setUid(uid);
            billRecord.setObjId(headwearPurseRecordDTO.getRecordId().toString());
            billRecord.setObjType(BillRecordType.purseHeadwear);
            billRecord.setGoldNum(0L);
            billRecord.setCreateTime(new Date());
            recordDao.save(billRecord);
        }

        HeadwearGetRecordDO getRecordDo = new HeadwearGetRecordDO();
        getRecordDo.setUid(uid);
        getRecordDo.setHeadwearId(headwearId.longValue());
        getRecordDo.setHeadwearDate(headwearDate);
        getRecordDo.setType(type.byteValue());
        getRecordDo.setCreateTime(new Date());
        headwearGetRecordDao.save(getRecordDo);

/*        if (message != null) {
            asyncNetEaseTrigger.sendMsg(String.valueOf(fromUid),String.valueOf(uid),message);
        }*/
    }
    @Override
    public HeadwearDTO getHeadwear(Integer headwearId) {
        String headwearDtoStr = redisManager.hget(RedisKey.headwear.getKey(), String.valueOf(headwearId));
        if (StringUtils.isNotBlank(headwearDtoStr)) {
            try {
                HeadwearDTO headwearDTO = gson.fromJson(headwearDtoStr, HeadwearDTO.class);
                if (headwearDTO != null && headwearDTO.getAllowPurse() != null) {
                    return headwearDTO;
                }
            } catch (Exception e) {
            }
        }

        HeadwearDTO headwearDto = headwearDao.getHeadwear(headwearId);
        if (headwearDto != null) {
            redisManager.hset(RedisKey.headwear.getKey(), String.valueOf(headwearId), gson.toJson(headwearDto));
        }

        return headwearDto;
    }

    @Override
    public HeadwearDTO getUserHeadwear(Long uid) {
        List<String> headwearIds = this.listUserHeadwearid(uid);
        HeadwearPurseRecordDTO recordDto;
        for (String headwearId : headwearIds) {
            recordDto = this.getHeadwearRecord(uid, Integer.valueOf(headwearId));
            if (recordDto == null) {
                continue;
            }

            if (recordDto.getIsUse() == null || recordDto.getIsUse().intValue() != 1) {
                continue;
            }

            if (recordDto.getHeadwearDate() == null || recordDto.getHeadwearDate() == 0) {
                continue;
            }

            return this.getHeadwear(Integer.valueOf(headwearId));
        }
        return null;
    }

    @Override
    public HeadwearPurseRecordDTO getHeadwearRecord(Long uid, Integer headwearId) {
        String fieldKey = uid + "_" + headwearId;
        String headwearRecordStr = redisManager.hget(RedisKey.headwear_purse.getKey(), fieldKey);
        if (StringUtils.isNotBlank(headwearRecordStr)) {
            return gson.fromJson(headwearRecordStr, HeadwearPurseRecordDTO.class);
        }

        HeadwearPurseRecordDTO recordDto = headwearPurseRecordDao.getUserHeadwearPurseRecord(new UserHeadwearRecordQuery(uid, headwearId));
        if (recordDto != null) {
            redisManager.hset(RedisKey.headwear_purse.getKey(), fieldKey, gson.toJson(recordDto));
        }
        return recordDto;
    }

    @Override
    public List<String> listUserHeadwearid(Long uid) {
        String str = redisManager.hget(RedisKey.headwear_purse_list.getKey(), String.valueOf(uid));
        if (StringUtils.isNotBlank(str)) {
            return Lists.newArrayList(str.split(","));
        }

        List<HeadwearPurseRecordDTO> list = headwearPurseRecordDao.listUserHeadwearRecord(uid);
        List<String> headwearids = Lists.newArrayList();
        if (list != null) {
            for (HeadwearPurseRecordDTO recordDto : list) {
                if (recordDto.getHeadwearId() == null) {
                    continue;
                }
                headwearids.add(String.valueOf(recordDto.getHeadwearId()));
            }
            redisManager.hset(RedisKey.headwear_purse_list.getKey(), String.valueOf(uid), StringUtils.join(headwearids, ","));
        }

        return headwearids;
    }

    @Override
    public List<String> listAllHeadwearids() {
        String headwearids = redisManager.get(RedisKey.headwear_list.getKey());
        if (StringUtils.isNotBlank(headwearids)) {
            return Lists.newArrayList(headwearids.split(","));
        }

        List<String> list = headwearDao.listAllHeadwearids();
        if (list != null && list.size() > 0) {
            redisManager.set(RedisKey.headwear_list.getKey(), StringUtils.join(list, ","));
        }
        return list;
    }

    @Override
    public List<String> listByMall() {
        String headwearids = redisManager.get(RedisKey.headwear_mall.getKey());
        if (StringUtils.isNotBlank(headwearids)) {
            return Lists.newArrayList(headwearids.split(","));
        }
        List<String> list = headwearDao.listByMall();
        if (list != null && list.size() > 0) {
            redisManager.set(RedisKey.headwear_mall.getKey(), StringUtils.join(list, ","));
        }
        return list;
    }
}
