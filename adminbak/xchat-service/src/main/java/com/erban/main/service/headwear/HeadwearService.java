package com.erban.main.service.headwear;

import com.erban.main.config.SystemConfig;
import com.erban.main.model.*;
import com.erban.main.model.level.LevelExerpenceVo;
import com.erban.main.mybatismapper.HeadwearGetRecordMapper;
import com.erban.main.mybatismapper.HeadwearPurseRecordMapper;
import com.erban.main.param.neteasepush.NeteaseSendMsgParam;
import com.erban.main.service.SendSysMsgService;
import com.erban.main.service.base.CacheBaseService;
import com.erban.main.service.level.LevelExperienceService;
import com.erban.main.service.record.BillRecordService;
import com.erban.main.service.user.UserPurseService;
import com.erban.main.service.user.UserPurseUpdateService;
import com.erban.main.service.user.UsersService;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.HeadwearVo;
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
public class HeadwearService extends CacheBaseService<Headwear, Headwear> {
    @Autowired
    private UsersService usersService;
    @Autowired
    private HeadwearPurseService headwearPurseService;
    @Autowired
    private HeadwearPurseRecordMapper headwearPurseRecordMapper;
    @Autowired
    private UserPurseService userPurseService;
    @Autowired
    private UserPurseUpdateService userPurseUpdateService;
    @Autowired
    private BillRecordService billRecordService;
    @Autowired
    private SendSysMsgService sendSysMsgService;
    @Autowired
    private HeadwearGetRecordMapper headwearGetRecordMapper;
    @Autowired
    private LevelExperienceService levelExperienceService;

    @Override
    public Headwear getOneByJedisId(String jedisId) {
        return getOne(RedisKey.headwear.getKey(), jedisId, "select * from headwear where headwear_id = ? ", jedisId);
    }

    @Override
    public Headwear entityToCache(Headwear entity) {
        return entity;
    }

    public String refreshCatListCache(String jedisCode) {
        return refreshListCacheByCode(null, jedisCode, "getHeadwearId", "select * from headwear where headwear_status = 1 order by seq_no asc ");
    }

    public List<String> addPurse(String str, Long uid) {
        List<String> ids = new ArrayList<>();
        if (StringUtils.isNotBlank(str)) {
            ids = StringUtils.splitToList(str, ",");
        }
        String purse = headwearPurseService.getPurse(uid);// 如果头饰无效了，但用户购买过而且还没有到期，就把已购买的头饰放到前面显示
        if (StringUtils.isNotBlank(purse)) {
            String[] pList = purse.split(",");
            HeadwearPurseRecord headwearPurseRecord;
            for (String p : pList) {
                if (!ids.contains(p)) {
                    headwearPurseRecord = headwearPurseService.getOneByJedisId(uid.toString(), p);
                    if (headwearPurseRecord != null && headwearPurseRecord.getHeadwearDate() != 0) {
                        ids.add(0, p);
                    }
                }
            }
        }
        return ids;
    }

    public List<HeadwearVo> getHeadwear(Long uid, Integer pageNum, Integer pageSize) {
        List<String> ids;
        String str = jedisService.get(RedisKey.headwear_list.getKey());
        if (StringUtils.isEmpty(str)) {
            str = refreshCatListCache(RedisKey.headwear_list.getKey());
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

    public List<HeadwearVo> toVo(List<Headwear> headwearList, Long uid) {
        if (headwearList.size() == 0) {
            return new ArrayList<>();
        }
        Integer level = 0;
        LevelExerpenceVo levelExerpenceVo = levelExperienceService.getLevelExperience(uid);
        if (levelExerpenceVo != null) {
            level = Integer.valueOf(levelExerpenceVo.getLevelName().substring(2));
        }
        HeadwearVo headwearVo;
        HeadwearPurseRecord headwearPurseRecord;
        List<HeadwearVo> headwearVoList = new ArrayList<>();
        for (Headwear headwear : headwearList) {
            headwearVo = new HeadwearVo();
            headwearVo.setHeadwearId(headwear.getHeadwearId());
            headwearVo.setGoldPrice(headwear.getGoldPrice());
            if (headwear.getHeadwearId() == 1) {
                headwearVo.setPicUrl("http://res.91fb.com/headwear_level_" + level.toString() + ".png");
                headwearVo.setHeadwearName(level.toString() + headwear.getHeadwearName());
            } else {
                headwearVo.setPicUrl(headwear.getPicUrl());
                headwearVo.setHeadwearName(headwear.getHeadwearName());
            }
            headwearVo.setHasGifPic(headwear.getHasGifPic());
            headwearVo.setGifUrl(headwear.getGifUrl());
            headwearVo.setHasVggPic(headwear.getHasVggPic());
            headwearVo.setVggUrl(headwear.getVggUrl());
            headwearVo.setEffectiveTime(headwear.getEffectiveTime());
            headwearVo.setTimeLimit(headwear.getIsTimeLimit());
            headwearPurseRecord = headwearPurseService.getOneByJedisId(uid.toString(), headwear.getHeadwearId().toString());
            if (headwearPurseRecord == null) {
                headwearVo.setIsPurse(0);
                headwearVo.setDays(0);
                headwearVo.setDaysRemaining(0);
            } else {
                if (headwearPurseRecord.getHeadwearDate() == 0) {
                    headwearVo.setIsPurse(0);
                } else {
                    if (headwearPurseRecord.getIsUse().intValue() == 1) {
                        headwearVo.setIsPurse(2);
                    } else {
                        headwearVo.setIsPurse(1);
                    }
                }
                headwearVo.setDays(headwearPurseRecord.getHeadwearDate());
                headwearVo.setDaysRemaining(headwearPurseRecord.getHeadwearDate());
            }
            headwearVoList.add(headwearVo);
        }
        return headwearVoList;
    }

    public BusiResult purse(Long uid, Integer headwearId, Integer type) {
        Users users = usersService.getUsersByUid(uid);
        if (users == null) {
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        Headwear headwear = getOneByJedisId(headwearId.toString());
        if (headwear == null) {
            return new BusiResult(BusiStatus.NOTEXISTS, "该头饰不存在~", "");
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
            if (goldNum < headwear.getGoldPrice()) {
                return new BusiResult(BusiStatus.PURSEMONEYNOTENOUGH);
            }
            UserPurse userPurseAfter = userPurseService.updateGoldBySendGiftCache(userPurse, headwear.getGoldPrice());
            if (userPurseAfter == null) {
                return new BusiResult(BusiStatus.PURSEMONEYNOTENOUGH);
            }
            // 更新送礼物用户的钱包，减金币
            int result = userPurseUpdateService.reduceGoldNumFromDB(uid, headwear.getGoldPrice());
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
        String purse = headwearPurseService.getPurse(uid);
        if (StringUtils.isNotBlank(purse)) {
            if (!StringUtils.splitToList(purse, ",").contains(headwearId.toString())) {
                purse += "," + headwearId;
            }
        } else {
            purse = "" + headwearId;
        }
        jedisService.hwrite(RedisKey.headwear_purse_list.getKey(), uid.toString(), purse);
        HeadwearPurseRecord headwearPurseRecord = headwearPurseService.getOneByJedisId(uid.toString(), headwearId.toString());
        if (headwearPurseRecord == null) {
            headwearPurseRecord = new HeadwearPurseRecord();
            headwearPurseRecord.setUid(uid);
            headwearPurseRecord.setHeadwearId(headwearId.longValue());
            headwearPurseRecord.setTotalGoldNum(headwear.getGoldPrice());
            headwearPurseRecord.setHeadwearDate(headwear.getEffectiveTime().intValue());
            headwearPurseRecord.setIsUse(new Byte("1"));
            headwearPurseRecord.setCreateTime(new Date());
            headwearPurseRecordMapper.insertSelective(headwearPurseRecord);
        } else {
            headwearPurseRecord.setIsUse(new Byte("1"));
            headwearPurseRecord.setTotalGoldNum(headwearPurseRecord.getTotalGoldNum() + headwear.getGoldPrice());
            headwearPurseRecord.setHeadwearDate(headwearPurseRecord.getHeadwearDate() + headwear.getEffectiveTime().intValue());
            headwearPurseRecordMapper.updateByPrimaryKeySelective(headwearPurseRecord);
        }
        jedisService.hset(RedisKey.headwear_purse.getKey(), uid.toString() + "_" + headwearId.toString(), gson.toJson(headwearPurseService.entityToCache(headwearPurseRecord)));
        BillRecord billRecord = new BillRecord();
        billRecord.setBillId(UUIDUitl.get());
        billRecord.setUid(uid);
        billRecord.setObjId(headwearPurseRecord.getRecordId().toString());
        billRecord.setObjType(Constant.BillType.purseHeadwear);
        billRecord.setGoldNum(headwear.getGoldPrice());
        billRecord.setCreateTime(new Date());
        billRecordService.insertBillRecord(billRecord);
        HeadwearGetRecord headwearGetRecord = new HeadwearGetRecord();
        headwearGetRecord.setUid(uid);
        headwearGetRecord.setHeadwearId(headwearId.longValue());
        headwearGetRecord.setHeadwearDate(headwear.getEffectiveTime().intValue());
        headwearGetRecord.setType(new Byte("1"));
        headwearGetRecord.setCreateTime(new Date());
        headwearGetRecordMapper.insert(headwearGetRecord);
        // 发送消息给用户
        NeteaseSendMsgParam neteaseSendMsgParam = new NeteaseSendMsgParam();
        neteaseSendMsgParam.setFrom(SystemConfig.secretaryUid);
        neteaseSendMsgParam.setOpe(0);
        neteaseSendMsgParam.setType(0);
        neteaseSendMsgParam.setTo(uid.toString());
        neteaseSendMsgParam.setBody("恭喜您成功购买头饰【" + headwear.getHeadwearName() + "】,快点去搭配吧！");
        sendSysMsgService.sendMsg(neteaseSendMsgParam);
        return new BusiResult(BusiStatus.SUCCESS, "购买成功", "");
    }

    public void doReset(Long uid) {
        List<HeadwearPurseRecord> tList = jdbcTemplate.query("select * from headwear_purse_record where uid = ? and is_use = 1", new BeanPropertyRowMapper<>(HeadwearPurseRecord.class), uid);
        if (tList != null && tList.size() != 0) {
            HeadwearPurseRecord headwearPurseRecord = tList.get(0);
            headwearPurseRecord.setIsUse(new Byte("0"));
            headwearPurseRecordMapper.updateByPrimaryKeySelective(headwearPurseRecord);
            jedisService.hset(RedisKey.headwear_purse.getKey(), uid.toString() + "_" + headwearPurseRecord.getHeadwearId().toString(), gson.toJson(headwearPurseService.entityToCache(headwearPurseRecord)));
        }
    }

    public BusiResult use(Long uid, Integer headwearId) {
        Users users = usersService.getUsersByUid(uid);
        if (users == null) {
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        if (headwearId == -1) {// 取消座驾选择
            doReset(uid);
        } else {
            Headwear headwear = getOneByJedisId(headwearId.toString());
            if (headwear == null) {
                return new BusiResult(BusiStatus.NOTEXISTS, "该头饰不存在~", "");
            }
            HeadwearPurseRecord headwearPurseRecord = headwearPurseService.getOneByJedisId(uid.toString(), headwearId.toString());
            if (headwearPurseRecord == null || headwearPurseRecord.getHeadwearDate() == 0) {
                return new BusiResult(BusiStatus.GIFTCATPURSENOTEXISTS, "您还没有购买过该头饰~", "");
            }
            doReset(uid);
            headwearPurseRecord.setIsUse(new Byte("1"));
            headwearPurseRecordMapper.updateByPrimaryKeySelective(headwearPurseRecord);
            jedisService.hset(RedisKey.headwear_purse.getKey(), uid.toString() + "_" + headwearId.toString(), gson.toJson(headwearPurseService.entityToCache(headwearPurseRecord)));
        }
        return new BusiResult(BusiStatus.SUCCESS);
    }

    public BusiResult give(Long uid, Integer headwearId, Long targetUid) {
        Users users = usersService.getUsersByUid(uid);
        if (users == null) {
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        Users users1 = usersService.getUsersByUid(targetUid);
        if (users1 == null) {
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        Headwear headwear = getOneByJedisId(headwearId.toString());
        if (headwear == null) {
            return new BusiResult(BusiStatus.NOTEXISTS, "该头饰不存在~", "");
        }
        if (headwear.getHeadwearStatus().intValue() == 2 || headwear.getGoldPrice().intValue() == 0) {
            return new BusiResult(BusiStatus.NOAUTHORITY, "此类头饰不支持赠送", "");
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
            if (goldNum < headwear.getGoldPrice()) {
                return new BusiResult(BusiStatus.PURSEMONEYNOTENOUGH);
            }
            UserPurse userPurseAfter = userPurseService.updateGoldBySendGiftCache(userPurse, headwear.getGoldPrice());
            if (userPurseAfter == null) {
                return new BusiResult(BusiStatus.PURSEMONEYNOTENOUGH);
            }
            // 更新送礼物用户的钱包，减金币
            int result = userPurseUpdateService.reduceGoldNumFromDB(uid, headwear.getGoldPrice());
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
        String purse = headwearPurseService.getPurse(users1.getUid());
        if (StringUtils.isNotBlank(purse)) {
            if (!StringUtils.splitToList(purse, ",").contains(headwearId.toString())) {
                purse += "," + headwearId;
            }
        } else {
            purse = "" + headwearId;
        }
        jedisService.hwrite(RedisKey.headwear_purse_list.getKey(), users1.getUid().toString(), purse);
        HeadwearPurseRecord headwearPurseRecord = headwearPurseService.getOneByJedisId(users1.getUid().toString(), headwearId.toString());
        if (headwearPurseRecord == null) {
            headwearPurseRecord = new HeadwearPurseRecord();
            headwearPurseRecord.setUid(users1.getUid());
            headwearPurseRecord.setHeadwearId(headwearId.longValue());
            headwearPurseRecord.setTotalGoldNum(headwear.getGoldPrice());
            headwearPurseRecord.setHeadwearDate(headwear.getEffectiveTime().intValue());
            headwearPurseRecord.setIsUse(new Byte("0"));
            headwearPurseRecord.setCreateTime(new Date());
            headwearPurseRecordMapper.insertSelective(headwearPurseRecord);
        } else {
            headwearPurseRecord.setTotalGoldNum(headwearPurseRecord.getTotalGoldNum() + headwear.getGoldPrice());
            headwearPurseRecord.setHeadwearDate(headwearPurseRecord.getHeadwearDate() + headwear.getEffectiveTime().intValue());
            headwearPurseRecordMapper.updateByPrimaryKeySelective(headwearPurseRecord);
        }
        jedisService.hset(RedisKey.headwear_purse.getKey(), users1.getUid().toString() + "_" + headwearId.toString(), gson.toJson(headwearPurseService.entityToCache(headwearPurseRecord)));
        BillRecord billRecord = new BillRecord();
        billRecord.setBillId(UUIDUitl.get());
        billRecord.setUid(uid);
        billRecord.setObjId(headwearPurseRecord.getRecordId().toString());
        billRecord.setObjType(Constant.BillType.purseHeadwear);
        billRecord.setGoldNum(headwear.getGoldPrice());
        billRecord.setCreateTime(new Date());
        billRecordService.insertBillRecord(billRecord);
        HeadwearGetRecord headwearGetRecord = new HeadwearGetRecord();
        headwearGetRecord.setUid(users1.getUid());
        headwearGetRecord.setHeadwearId(headwearId.longValue());
        headwearGetRecord.setHeadwearDate(headwear.getEffectiveTime().intValue());
        headwearGetRecord.setType(new Byte("5"));
        headwearGetRecord.setCreateTime(new Date());
        headwearGetRecordMapper.insert(headwearGetRecord);
        // 发送消息给用户
        NeteaseSendMsgParam neteaseSendMsgParam = new NeteaseSendMsgParam();
        neteaseSendMsgParam.setFrom(SystemConfig.secretaryUid);
        neteaseSendMsgParam.setOpe(0);
        neteaseSendMsgParam.setType(0);
        neteaseSendMsgParam.setTo(users1.getUid().toString());
        neteaseSendMsgParam.setBody(users.getNick() + "赠送给您头饰【" + headwear.getHeadwearName() + "】,快点去搭配吧！");
        sendSysMsgService.sendMsg(neteaseSendMsgParam);
        return new BusiResult(BusiStatus.SUCCESS, "赠送成功", "");
    }

    public void refreshAll() {
        Map<String, String> map = jedisService.hgetAll(RedisKey.headwear_purse.getKey());
        if (map == null || map.size() == 0) {
            return;
        }
        Headwear headwear;
        for (String key : map.keySet()) {
            try {
                HeadwearPurseRecord headwearPurseRecord = gson.fromJson(map.get(key), HeadwearPurseRecord.class);
                if (headwearPurseRecord.getHeadwearDate() > 0 && headwearPurseRecord.getHeadwearId() != 1) {// 等级头饰不用刷新，是永久的
                    headwearPurseRecord.setHeadwearDate(headwearPurseRecord.getHeadwearDate() - 1);
                    headwearPurseRecordMapper.updateByPrimaryKeySelective(headwearPurseRecord);
                    jedisService.hset(RedisKey.headwear_purse.getKey(), headwearPurseRecord.getUid() + "_" + headwearPurseRecord.getHeadwearId(), gson.toJson(headwearPurseService.entityToCache(headwearPurseRecord)));
                    if (headwearPurseRecord.getHeadwearDate() == 0) {
                        headwear = getOneByJedisId(headwearPurseRecord.getHeadwearId().toString());
                        // 发送消息给用户
                        NeteaseSendMsgParam neteaseSendMsgParam = new NeteaseSendMsgParam();
                        neteaseSendMsgParam.setFrom(SystemConfig.secretaryUid);
                        neteaseSendMsgParam.setOpe(0);
                        neteaseSendMsgParam.setType(0);
                        neteaseSendMsgParam.setTo(headwearPurseRecord.getUid().toString());
                        neteaseSendMsgParam.setBody("您购买的头饰【" + headwear.getHeadwearName() + "】已到期,请到头饰管理续费！");
                        sendSysMsgService.sendMsg(neteaseSendMsgParam);
                    }
                }
            } catch (Exception e) {
                logger.error("refreshGiftCar error", e);
            }
        }
    }

    public void purse4Free(Long uid, Integer headwearId, int effectiveTime) {
        String purse = headwearPurseService.getPurse(uid);
        if (StringUtils.isNotBlank(purse)) {
            if (!StringUtils.splitToList(purse, ",").contains(headwearId.toString())) {
                purse += "," + headwearId;
            }
        } else {
            purse = "" + headwearId;
        }
        jedisService.hwrite(RedisKey.headwear_purse_list.getKey(), uid.toString(), purse);

        HeadwearPurseRecord headwearPurseRecord = headwearPurseService.getOneByJedisId(uid.toString(), headwearId.toString());
        if (headwearPurseRecord == null) {
            headwearPurseRecord = new HeadwearPurseRecord();
            headwearPurseRecord.setUid(uid);
            headwearPurseRecord.setHeadwearId(headwearId.longValue());
            headwearPurseRecord.setTotalGoldNum(0L);
            headwearPurseRecord.setHeadwearDate(effectiveTime);
            headwearPurseRecord.setIsUse(new Byte("0"));
            headwearPurseRecord.setCreateTime(new Date());
            headwearPurseRecordMapper.insertSelective(headwearPurseRecord);
        } else {
            headwearPurseRecord.setHeadwearDate(headwearPurseRecord.getHeadwearDate() + effectiveTime);
            headwearPurseRecordMapper.updateByPrimaryKeySelective(headwearPurseRecord);
        }

        jedisService.hset(RedisKey.headwear_purse.getKey(), uid.toString() + "_" + headwearId.toString(), gson.toJson(headwearPurseService.entityToCache(headwearPurseRecord)));

        HeadwearGetRecord headwearGetRecord = new HeadwearGetRecord();
        headwearGetRecord.setUid(uid);
        headwearGetRecord.setHeadwearId(headwearId.longValue());
        headwearGetRecord.setHeadwearDate(effectiveTime);
        headwearGetRecord.setType(new Byte("7"));
        headwearGetRecord.setCreateTime(new Date());
        headwearGetRecordMapper.insert(headwearGetRecord);
    }

}
