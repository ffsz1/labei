package com.juxiao.xchat.manager.common.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.DefMsgType;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.item.dto.GiftDTO;
import com.juxiao.xchat.dao.user.UserGiftPurseDao;
import com.juxiao.xchat.dao.user.domain.UserGiftPurseDO;
import com.juxiao.xchat.dao.user.dto.UserGiftPurseDTO;
import com.juxiao.xchat.dao.user.dto.UserPurseDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.item.constant.GiftType;
import com.juxiao.xchat.manager.common.item.mq.RoomMessage;
import com.juxiao.xchat.manager.common.mcoin.McoinManager;
import com.juxiao.xchat.manager.common.user.UserGiftPurseManager;
import com.juxiao.xchat.manager.common.user.UserPurseManager;
import com.juxiao.xchat.manager.external.im.ImRoomManager;
import com.juxiao.xchat.manager.external.netease.bo.Attach;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @class: UserGiftPurseManagerImpl.java
 * @author: chenjunsheng
 * @date 2018/6/15
 */
@Slf4j
@Service
public class UserGiftPurseManagerImpl implements UserGiftPurseManager {
    @Autowired
    private UserGiftPurseDao purseDao;
    @Autowired
    private UserPurseManager userPurseManager;
    @Autowired
    private RedisManager redisManager;

    @Autowired
    private ImRoomManager imRoomManager;

    @Autowired
    private McoinManager mcoinManager;
    @Autowired
    private Gson gson;

    @Override
    public void updateUserGiftPurse(Long uid, Integer giftId, Integer countNum) throws WebServiceException {
        String fieldKey = new StringBuilder().append(uid).append("_").append(giftId).toString();
        String lockVal = redisManager.lock(RedisKey.lock_user_gift.getKey(fieldKey), 10000);
        if (StringUtils.isBlank(lockVal)) {
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }
        try {
            /**
             * 由于在缓存前面做了加法 只需要将redis 的数据同步到mysql就行 所以不能贸然清掉缓存
             */
            UserGiftPurseDTO purseDto = this.getUserGiftPurse(uid, giftId);
            UserGiftPurseDO purseDo = new UserGiftPurseDO();
            if (purseDto == null || purseDto.getGiftPurseId() == null) {
                purseDo.setUid(uid);
                purseDo.setGiftId(giftId);
                purseDo.setCountNum(purseDto.getCountNum());// 数量已经在mq前面加了 极端情况下 mq的这个保存数据库操作 可能会导致覆盖掉原本的数据
                purseDo.setCreateTime(new Date());
                purseDao.save(purseDo);

                purseDto = new UserGiftPurseDTO();
                BeanUtils.copyProperties(purseDo, purseDto);
                redisManager.hset(RedisKey.user_gift_purse.getKey(), fieldKey, gson.toJson(purseDto));
            } else {
                purseDo.setGiftPurseId(purseDto.getGiftPurseId());
                purseDo.setGiftId(giftId);
                purseDo.setCountNum(purseDto.getCountNum());
                purseDao.update(purseDo);
            }
//            fieldKey = new StringBuilder().append(uid).append("_").append(giftId).toString();
//            redisManager.hset(RedisKey.user_gift_purse.getKey(), fieldKey, gson.toJson(purseDto));
        } catch (Exception e) {
            throw e;
        } finally {
            redisManager.unlock(RedisKey.lock_user_gift.getKey(fieldKey), lockVal);
        }
    }

    @Override
    public Map<String, Long> reduceGiftPurseCache(Long sendUid, GiftDTO gift, Integer giftNum, Long giftPrice) throws WebServiceException {
        String fieldKey = new StringBuilder().append(sendUid).append("_").append(gift.getGiftId()).toString();
        String lockVal = redisManager.lock(RedisKey.lock_user_gift.getKey(fieldKey), 10 * 1000);
        if (StringUtils.isBlank(lockVal)) {
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }
        try {
            Map<String, Long> num = new HashMap<>();
            Long afterGoldNum;
            Long useGiftPurseNum;
            Long afterGiftPurseNum;
            UserGiftPurseDTO userGiftPurse = this.getUserGiftPurse(sendUid, gift.getGiftId());
            if (userGiftPurse == null || userGiftPurse.getCountNum() == 0) {
                if (gift.getGiftType() == GiftType.DRAW ) {
                    // 神秘礼物不足,
                    throw new WebServiceException(WebServiceCode.GIFT_DRAW_NOT_ENOUGH);
                } else if (gift.getGiftType() == GiftType.XIANGQIN) {
                    throw new WebServiceException(WebServiceCode.GIFT_XQ_NOT_ENOUGH);
                }
                afterGoldNum = Math.abs(giftPrice * giftNum);
                useGiftPurseNum = 0L;
                afterGiftPurseNum = 0L;
                // 扣除赠送用户的金币，扣除成功返回200
                // 多节点部署时可能存在并发问题，需要加上分布式锁
                userPurseManager.reduceGoldCache(sendUid, afterGoldNum, false);
                // FIXME :关于是否需要等号
            } else if (userGiftPurse.getCountNum() >= giftNum) {
                afterGoldNum = 0L;
                useGiftPurseNum = giftNum.longValue();
                afterGiftPurseNum = new Long(userGiftPurse.getCountNum() - giftNum);
                userGiftPurse.setCountNum(userGiftPurse.getCountNum() - giftNum);
                redisManager.hset(RedisKey.user_gift_purse.getKey(), fieldKey, gson.toJson(userGiftPurse));
            } else {
                if (gift.getGiftType() == GiftType.DRAW ) {
                    // 神秘礼物不足,
                    throw new WebServiceException(WebServiceCode.GIFT_DRAW_NOT_ENOUGH);
                } else if (gift.getGiftType() == GiftType.XIANGQIN) {
                    throw new WebServiceException(WebServiceCode.GIFT_XQ_NOT_ENOUGH);
                }
                afterGoldNum = Math.abs(giftPrice * (giftNum - userGiftPurse.getCountNum()));
                useGiftPurseNum = userGiftPurse.getCountNum().longValue();
                afterGiftPurseNum = 0L;
                // 扣除赠送用户的金币，扣除成功返回200
                // 多节点部署时可能存在并发问题，需要加上分布式锁
                userPurseManager.reduceGoldCache(sendUid, afterGoldNum, false);
                userGiftPurse.setCountNum(0);
                redisManager.hset(RedisKey.user_gift_purse.getKey(), fieldKey, gson.toJson(userGiftPurse));
            }
            num.put("afterGoldNum", afterGoldNum);
            num.put("useGiftPurseNum", useGiftPurseNum);
            num.put("afterGiftPurseNum", afterGiftPurseNum);
            //  TODO 增加平台收入
            return num;
        } catch (Exception e) {
            throw e;
        } finally {
            redisManager.unlock(RedisKey.lock_user_gift.getKey(fieldKey), lockVal);
        }
    }

    @Override
    public Map<String, Long> addGiftPurseCache(Long uid, Integer giftId, Integer countNum) throws WebServiceException {
        Map<String, Long> num = new HashMap<>();
        String fieldKey = new StringBuilder().append(uid).append("_").append(giftId).toString();
        String lockVal = redisManager.lock(RedisKey.lock_user_gift.getKey(fieldKey), 10000);

        if (StringUtils.isBlank(lockVal)) {
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }

        try {
            UserGiftPurseDTO purseDto = this.getUserGiftPurse(uid, giftId);
            UserGiftPurseDO purseDo = new UserGiftPurseDO();
            if (purseDto == null) {
                purseDo.setUid(uid);
                purseDo.setGiftId(giftId);
                purseDo.setCountNum(countNum);
                purseDo.setCreateTime(new Date());

                purseDto = new UserGiftPurseDTO();
                BeanUtils.copyProperties(purseDo, purseDto);
            } else {
                purseDo.setGiftPurseId(purseDto.getGiftPurseId());
                purseDo.setGiftId(giftId);
                purseDo.setCountNum(purseDto.getCountNum() + countNum);

                purseDto.setCountNum(purseDo.getCountNum());

            }
            fieldKey = new StringBuilder().append(uid).append("_").append(giftId).toString();
            redisManager.hset(RedisKey.user_gift_purse.getKey(), fieldKey, gson.toJson(purseDto));
        } catch (Exception e) {
            throw e;
        } finally {
            redisManager.unlock(RedisKey.lock_user_gift.getKey(fieldKey), lockVal);
        }

        return num;
    }

    @Override
    public Map<String, Long> addCallPurseCache(Long sendUid, GiftDTO gift, Integer giftNum, Long giftPrice, Long conchNum) throws WebServiceException {
        Map<String, Long> num = new HashMap<>();
        UserPurseDTO purseDto = userPurseManager.getUserPurse(sendUid);

        userPurseManager.updateReduceGoldAddConch(sendUid, giftPrice, conchNum, false);
        num.put("afterConchNum", purseDto.getConchNum() + conchNum);

        return num;
    }

    @Override
    public UserGiftPurseDTO getUserGiftPurse(Long uid, Integer giftId) {
        String fieldKey = new StringBuilder().append(uid).append("_").append(giftId).toString();
        String giftPurStr = redisManager.hget(RedisKey.user_gift_purse.getKey(), fieldKey);
        if (StringUtils.isNotBlank(giftPurStr)) {
            return gson.fromJson(giftPurStr, UserGiftPurseDTO.class);
        }

        UserGiftPurseDTO purseDto = purseDao.getUserGiftPurse(uid, giftId);
        if (purseDto != null) {
            redisManager.hset(RedisKey.user_gift_purse.getKey(), fieldKey, gson.toJson(purseDto));
        }
        return purseDto;
    }

    /**
     * 处理捡海螺信息
     *
     * @param roomMessage
     */
    @Override
    public void handleMQRoomMessage(RoomMessage roomMessage) {
        Attach attach = new Attach();
        attach.setFirst(DefMsgType.roomMessage);
        attach.setSecond(DefMsgType.roomMessage);
        attach.setData(roomMessage.getData());
        JSONObject object = new JSONObject();
        object.put("custom", attach);
        try {
            imRoomManager.pushAllRoomMsg(object, Lists.newArrayList(roomMessage.getRoomId()));
        } catch (Exception e) {
            log.error("[ 发送捡海螺全服消息 ] handleMQRoomMessage 异常信息:{}", e);
        }
        // 删除该标识，表示消息已经消费过
        redisManager.hdel(RedisKey.mq_room_message_status.getKey(), roomMessage.getMessId());
    }

    @Override
    public Map<String, Long> reduceGiftPropPurseCache(Long uid, GiftDTO gift, Integer giftNum) throws WebServiceException {
        String fieldKey = new StringBuilder().append(uid).append("_").append(gift.getGiftId()).toString();
        String lockVal = redisManager.lock(RedisKey.lock_user_gift.getKey(fieldKey), 10 * 1000);

        if (gift.getGiftType() != GiftType.ACTIVITY) {
            throw new WebServiceException(WebServiceCode.GIFT_TYPE_ERROR);
        }

        if (StringUtils.isBlank(lockVal)) {
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }
        try {
            Map<String, Long> num = new HashMap<>();
            Long useGiftPurseNum;
            Long afterGiftPurseNum;
            UserGiftPurseDTO userGiftPurse = this.getUserGiftPurse(uid, gift.getGiftId());
            if (userGiftPurse == null || userGiftPurse.getCountNum() < giftNum) {
                throw new WebServiceException(WebServiceCode.GIFT_PROP_NOT_ENOUGH);
            } else {
                useGiftPurseNum = giftNum.longValue();
                afterGiftPurseNum = (long) (userGiftPurse.getCountNum() - giftNum);
                userGiftPurse.setCountNum(userGiftPurse.getCountNum() - giftNum);
                redisManager.hset(RedisKey.user_gift_purse.getKey(), fieldKey, gson.toJson(userGiftPurse));
            }
            num.put("useGiftPurseNum", useGiftPurseNum);
            num.put("afterGiftPurseNum", afterGiftPurseNum);
            return num;
        } catch (Exception e) {
            throw e;
        } finally {
            redisManager.unlock(RedisKey.lock_user_gift.getKey(fieldKey), lockVal);
        }
    }
}
