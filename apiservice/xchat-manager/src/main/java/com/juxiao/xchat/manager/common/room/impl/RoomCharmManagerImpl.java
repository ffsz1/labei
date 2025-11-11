package com.juxiao.xchat.manager.common.room.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.DefMsgType;
import com.juxiao.xchat.base.constant.redis.ImRedisKey;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.room.RoomConfDao;
import com.juxiao.xchat.dao.room.dto.RoomConfDTO;
import com.juxiao.xchat.dao.room.dto.RoomDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.room.RoomCharmManager;
import com.juxiao.xchat.manager.common.room.RoomManager;
import com.juxiao.xchat.manager.common.room.vo.RoomUserCharmVO;
import com.juxiao.xchat.manager.external.im.ImRoomManager;
import com.juxiao.xchat.manager.external.im.bo.Custom;
import com.juxiao.xchat.manager.external.im.bo.ImRoomMessage;
import com.juxiao.xchat.manager.mq.ActiveMqManager;
import com.juxiao.xchat.manager.mq.constant.MqDestinationKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RoomCharmManagerImpl implements RoomCharmManager {
    @Autowired
    private RoomConfDao roomConfDao;
    @Autowired
    private ActiveMqManager activeMqManager;
    @Autowired
    private ImRoomManager imroomManager;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private RoomManager roomManager;

    @Async
    @Override
    public void saveRoomCharm(Long roomUid, Long uid, Integer charmValue) {
        if (roomUid == null || uid == null || charmValue == null) {
            return;
        }
//        // 房主不进行魅力值统计
//        if (roomUid.longValue() == uid.longValue()) {
//            return;
//        }

        RoomDTO roomDto = roomManager.getUserRoom(roomUid);
        if (roomDto == null || roomDto.getCharmOpen() == 0) { // 判断是否开启(0.隐藏 1.显示)
            return;
        }

        String lockVal = redisManager.lock(RedisKey.room_charm_lock.getKey(roomUid.toString()), 5000);
        if (StringUtils.isBlank(lockVal)) {
            return;
        }

        try {
            // 封装成方法，优化异常栈
            this.saveRoomCharmValue(roomUid, uid, charmValue);
            this.sendDelayRoomCharm(roomUid, null);
        } finally {
            redisManager.unlock(RedisKey.room_charm_lock.getKey(roomUid.toString()), lockVal);
        }
    }

    @Override
    public void saveRoomCharm(Long roomUid, Long[] uids, Integer charmValue) {
        if (roomUid == null || uids == null || charmValue == null) {
            return;
        }

        RoomDTO roomDto = roomManager.getUserRoom(roomUid);
        if (roomDto == null || roomDto.getCharmOpen() == 0) { // 判断是否开启(0.隐藏 1.显示)
            return;
        }

        // 判断是否开启
//        RoomConfDTO roomConfDto = roomConfDao.getRoomConf(roomUid);
//        log.info("[魅力值统计] roomConfDto:{}",new Gson().toJson(roomConfDto));
//        if (roomConfDto == null || roomConfDto.getCharmEnable() == null) {
//            return;
//        }
//
//        if (roomConfDto.getCharmEnable() != 2) {
//            return;
//        }

        String lockVal = redisManager.lock(RedisKey.room_charm_lock.getKey(roomUid.toString()), 5000);
        if (StringUtils.isBlank(lockVal)) {
            return;
        }

        try {
            for (Long uid : uids) {
//                if (roomUid.equals(uid)) {
//                    continue;
//                }
                this.saveRoomCharmValue(roomUid, uid, charmValue);
            }
        } finally {
            redisManager.unlock(RedisKey.room_charm_lock.getKey(roomUid.toString()), lockVal);
        }
        this.sendDelayRoomCharm(roomUid, null);
    }

    @Override
    public void updateMicHandler(Long roomUid, Long uid, Byte type) {
        if (type == 2) {
            // 下麦处理
            redisManager.zrem(RedisKey.room_charm_add_zset.getKey(roomUid.toString()), uid.toString());
            redisManager.zrem(RedisKey.room_charm_zset.getKey(roomUid.toString()), uid.toString());
        } else {
            return;
        }

        this.sendRoomAllCharm(roomUid, null);
    }

    @Override
    public void clearMicCharm(Long roomUid, Long uid) throws WebServiceException{
        RoomDTO roomDto = roomManager.getUserRoom(roomUid);
        if (roomDto == null) {
            log.warn("[ 房间魅力值 ] 房间为空, roomUid:>{}", roomUid);
            return;
        }

        if (!imroomManager.isRoomManager(roomDto.getRoomId(), uid)) {
            // 管理员信息验证
            throw new WebServiceException(WebServiceCode.ROOM_NO_AUTHORITY);
        }

        Map<String, String> micinfo = redisManager.select(4).hgetAll(ImRedisKey.room_map_queue_mem_key.getKey(String.valueOf(roomDto.getRoomId())));
        if (micinfo == null || micinfo.size() == 0) {
            log.warn("[ 房间魅力值 ] 无人上麦, roomUid:>{}", roomUid);
            return;
        }

        for (Map.Entry<String, String> entry : micinfo.entrySet()) {
            redisManager.zrem(RedisKey.room_charm_add_zset.getKey(roomUid.toString()), entry.getValue());
            redisManager.zadd(RedisKey.room_charm_zset.getKey(roomUid.toString()), entry.getValue(), 0);
        }
        redisManager.expire(RedisKey.room_charm_zset.getKey(roomUid.toString()), 8, TimeUnit.SECONDS);

        this.sendRoomAllCharm(roomUid, null);
    }

    @Override
    public void sendDelayRoomCharm(Long roomUid, Long uid) {
        // 隔5s钟发送一次
        String lockKey = RedisKey.room_charm_mqlock_string.getKey(roomUid.toString());
        String timestamp = redisManager.get(lockKey);
        if (StringUtils.isNotBlank(timestamp)) {
            if (StringUtils.isNumeric(timestamp) && System.currentTimeMillis() - Long.valueOf(timestamp) < 5000) {
                return;
            }
            redisManager.del(lockKey);
        }

        redisManager.set(lockKey, String.valueOf(System.currentTimeMillis()), 5, TimeUnit.SECONDS);

        JSONObject object = new JSONObject();
        object.put("roomUid", roomUid);
        if (uid != null) {
            object.put("uid", uid);
        }
        activeMqManager.sendDelayQueueMessage(MqDestinationKey.ROOM_CHARM_SEND_MSG, object.toJSONString(), 5000);
    }

    @Override
    public void sendRoomAddCharm(Long roomUid) {
        if (roomUid == null) {
            log.warn("[ 房间魅力值 ] 房主为空");
            return;
        }

        RoomDTO roomDto = roomManager.getUserRoom(roomUid);
        if (roomDto == null) {
            log.warn("[ 房间魅力值 ] 房间为空, roomUid:>{}", roomUid);
            return;
        }

        String redisKey = RedisKey.room_charm_add_zset.getKey(roomUid.toString());
        Set<String> members = redisManager.zrevRangeByScore(redisKey, Integer.MAX_VALUE, 0);
        if (members == null || members.size() == 0) {
            log.warn("[ 房间魅力值 ] 无增量信息, roomUid:>{}", roomUid);
            return;
        }

        Map<String, String> micinfo = redisManager.select(4).hgetAll(ImRedisKey.room_map_queue_mem_key.getKey(String.valueOf(roomDto.getRoomId())));
        if (micinfo == null || micinfo.size() == 0) {
            log.warn("[ 房间魅力值 ] 无人上麦, roomUid:>{}", roomUid);
            return;
        }

        Map<String, RoomUserCharmVO> latestCharm = this.getLatestCharm(roomUid, micinfo, Lists.newArrayList(members), true);

        ImRoomMessage msginfo = new ImRoomMessage();
        Map<String, Object> data = new HashMap<>(8);
        data.put("roomId", roomDto.getRoomId());
        data.put("timestamps", System.currentTimeMillis());
        data.put("latestCharm", latestCharm);
        msginfo.setRoomId(String.valueOf(roomDto.getRoomId()));
        msginfo.setCustom(new Custom(DefMsgType.RoomCharm, 1, data));
        try {
            imroomManager.pushRoomCustomMsg(msginfo);
        } catch (Exception e) {
            log.error("发送消息异常，消息:>{}，异常信息：", msginfo, e);
        }
    }

    @Override
    public void sendRoomAllCharm(Long roomUid, Long uid) {
        if(uid != null && uid == 0){
            log.warn("[ 房间魅力值 ] uid为空或者为0");
            return ;
        }
        if (roomUid == null) {
            log.warn("[ 房间魅力值 ] 房主为空");
            return;
        }

        RoomDTO roomDto = roomManager.getUserRoom(roomUid);
        if (roomDto == null) {
            log.warn("[ 房间魅力值 ] 房间为空, roomUid:>{}", roomUid);
            return;
        }

        Map<String, String> micinfo = redisManager.select(4).hgetAll(ImRedisKey.room_map_queue_mem_key.getKey(String.valueOf(roomDto.getRoomId())));
//        if (micinfo == null || micinfo.size() == 0) {
//            log.warn("[ 房间魅力值 ] 无人上麦, roomUid:>{}", roomUid);
//            return;
//        }

        String redisKey = RedisKey.room_charm_zset.getKey(roomUid.toString());
        Set<String> members = redisManager.zrevRangeByScore(redisKey, Integer.MAX_VALUE, 0);
//        if (members == null || members.size() == 0) {
//            log.warn("[ 房间魅力值 ] 无信息, roomUid:>{}", roomUid);
//            return;
//        }

        Map<String, RoomUserCharmVO> latestCharm = this.getLatestCharm(roomUid, micinfo, Lists.newArrayList(members), false);
        ImRoomMessage msginfo = new ImRoomMessage();
        Map<String, Object> data = new HashMap<>(8);
        data.put("roomId", roomDto.getRoomId());
        data.put("timestamps", System.currentTimeMillis());
        data.put("latestCharm", latestCharm);
        msginfo.setRoomId(String.valueOf(roomDto.getRoomId()));
        msginfo.setCustom(new Custom(DefMsgType.RoomCharm, 1, data));
        if (uid != null) {
            msginfo.setUid(uid);
            try {
                imroomManager.pushUserMsg(msginfo);
            } catch (Exception e) {
                log.error("[ 发送房间消息 ] 异常，消息:>{}，异常信息：", msginfo, e);
            }
        } else {
            try {
                imroomManager.pushRoomCustomMsg(msginfo);
            } catch (Exception e) {
                log.error("[ 发送房间消息 ] 异常，消息:>{}，异常信息：", msginfo, e);
            }
        }
    }

    @Override
    public void deleteRoomCharm(Long roomUid, Long uid) {
        if (roomUid == null || uid == null) {
            return;
        }

        redisManager.zrem(RedisKey.room_charm_zset.getKey(roomUid.toString()), uid.toString());
        redisManager.zrem(RedisKey.room_charm_add_zset.getKey(roomUid.toString()), uid.toString());
    }

    private void saveRoomCharmValue(Long roomUid, Long uid, Integer charmValue) {
        // 保存所有在房间用户魅力值
        String redisKey = RedisKey.room_charm_zset.getKey(roomUid.toString());
        redisManager.zincrby(redisKey, charmValue.doubleValue(), uid.toString());
        redisManager.expire(redisKey, 1, TimeUnit.DAYS);

        // 保存有魅力值变动的用户
        Double score = redisManager.zscore(redisKey, uid.toString());
        redisKey = RedisKey.room_charm_add_zset.getKey(roomUid.toString());
        redisManager.zadd(redisKey, score == null ? 0 : score, uid.toString());
        redisManager.expire(redisKey, 1, TimeUnit.DAYS);
    }


    /**
     * 获取麦上用户魅力值
     *
     * @param roomUid    房主
     * @param micinfo    麦上用户
     * @param members    需要推送的成员信息
     * @param isClearAdd 是否清空增量部分
     * @return
     */
    private Map<String, RoomUserCharmVO> getLatestCharm(Long roomUid, Map<String, String> micinfo, List<String> members, boolean isClearAdd) {
        // 标记麦上最大魅力值用户个数
        int micCharmMaxCount = 0;
        // 麦上最大魅力值
        double charmMax = 0;
        String redisKey = RedisKey.room_charm_zset.getKey(roomUid.toString());
        for (Map.Entry<String, String> entry : micinfo.entrySet()) {
            Double score = redisManager.zscore(redisKey, entry.getValue());
            if (score == null) {
                continue;
            }

            if (charmMax < score) {
                charmMax = score;
                micCharmMaxCount = 0;
                continue;
            }

            if (charmMax == score) {
                micCharmMaxCount++;
            }
        }


        Map<String, RoomUserCharmVO> latestCharm = new HashMap<>(8);
        for (String member : members) {
            if (!micinfo.containsValue(member)) {
                continue;
            }

            Double score = redisManager.zscore(redisKey, member);
            if (score == null || score < 0) {
                latestCharm.put(member, new RoomUserCharmVO(0, false));
                continue;
            }

            latestCharm.put(member, new RoomUserCharmVO(score.intValue(), micCharmMaxCount == 0 && score == charmMax));
        }

        if (isClearAdd) {
            redisManager.zremRangeByScore(RedisKey.room_charm_add_zset.getKey(roomUid.toString()), charmMax - 1, charmMax - 1);
        }
        return latestCharm;
    }

    /**
     * 发送用户魅力值
     *
     * @param roomUid 房主UID
     * @param uid     用户ID
     */
    @Override
    public void sendRoomCharm(Long roomUid, Long uid) {
        JSONObject object = new JSONObject();
        object.put("roomUid", roomUid);
        if (uid != null) {
            object.put("uid", uid);
        }
        activeMqManager.sendQueueMessage(MqDestinationKey.ROOM_CHARM_SEND_MSG, object.toJSONString());
    }
}
