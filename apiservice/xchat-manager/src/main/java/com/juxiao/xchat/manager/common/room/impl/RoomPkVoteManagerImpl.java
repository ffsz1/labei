package com.juxiao.xchat.manager.common.room.impl;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.DateTimeUtils;
import com.juxiao.xchat.dao.room.RoomPkVoteDao;
import com.juxiao.xchat.dao.room.dto.RoomDTO;
import com.juxiao.xchat.dao.room.dto.RoomPkVoteDTO;
import com.juxiao.xchat.dao.room.dto.RoomPkVoteResultDTO;
import com.juxiao.xchat.dao.room.enumeration.RoomPkVoteType;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.room.RoomManager;
import com.juxiao.xchat.manager.common.room.RoomPkVoteManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @class: RoomPkVoteManagerImpl.java
 * @author: chenjunsheng
 * @date 2018/7/3
 */
@Service
public class RoomPkVoteManagerImpl implements RoomPkVoteManager {
    private final Logger logger = LoggerFactory.getLogger(RoomPkVoteManager.class);
    @Autowired
    private Gson gson;
    @Autowired
    private RoomPkVoteDao pkvoteDao;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private RoomManager roomManager;
    @Autowired
    private UsersManager usersManager;

    /**
     * 刷礼物进行投票
     *
     * @param roomUid
     * @param recvUid
     * @param goldNum
     */
    @Override
    public void goldVote(Long roomUid, Long recvUid, Long goldNum) {
        logger.info("[ 房间PK ] 增加票数处理开始，roomId:>{},recvUid:>{},goldNum:>{}", roomUid, recvUid, goldNum);
        try {
            if (roomUid == null || recvUid == null || goldNum == null) {
                return;
            }

            RoomDTO roomDto = roomManager.getUserRoom(roomUid);
            if (roomDto == null || roomDto.getRoomId() == null) {
                return;
            }

            long roomId = roomDto.getRoomId();
            RoomPkVoteDTO pkVote = this.getRoomPkVote(roomId);
            if (pkVote == null) {
                logger.warn("[ 房间PK ] 房间无此投票，roomId:>{}", roomId);
                return;
            }

            if (!RoomPkVoteType.GOLD_COUNT_PK.compareToValue(pkVote.getPkType())) {
                logger.warn("[ 房间PK ] 礼物金币投票，不支持的类型，roomId:>{}，pkType:>{}", roomId, pkVote.getPkType());
                return;
            }

            if (new Date().after(DateTimeUtils.addSecond(pkVote.getCreateTime(), pkVote.getExpireSeconds() - 1))) {
                logger.warn("[ 房间PK ] 礼物金币投票，不支持的类型，roomId:>{}，pkType:>{}", roomId, pkVote.getPkType());
                return;
            }

            if (recvUid.longValue() != pkVote.getUid().longValue() && recvUid.longValue() != pkVote.getPkUid().longValue()) {
                return;
            }

            redisManager.hincrBy(RedisKey.room_pk_vote_count.getKey(), pkVote.getId() + "_" + recvUid, goldNum);
            String goldAmountStr = redisManager.hget(RedisKey.room_pk_vote_count.getKey(), pkVote.getId() + "_" + recvUid);
            if (recvUid.longValue() == pkVote.getUid().longValue()) {
                pkvoteDao.updateAddVote(pkVote.getId(), goldNum.intValue());
                pkVote.setVoteCount(Integer.valueOf(goldAmountStr));
            } else if (recvUid.longValue() == pkVote.getPkUid().longValue()) {
                pkvoteDao.updateAddPkVote(pkVote.getId(), goldNum.intValue());
                pkVote.setPkVoteCount(Integer.valueOf(goldAmountStr));
            }
            redisManager.hset(RedisKey.room_pk_vote.getKey(), String.valueOf(roomId), gson.toJson(pkVote));
        } catch (Exception e) {
            logger.error("[]", e);
        }

    }

    @Override
    public RoomPkVoteDTO getRoomPkVote(Long roomId) {
        String pkvoteStr = redisManager.hget(RedisKey.room_pk_vote.getKey(), String.valueOf(roomId));
        if (StringUtils.isNotBlank(pkvoteStr)) {
            try {
                RoomPkVoteDTO pkVote = gson.fromJson(pkvoteStr, RoomPkVoteDTO.class);
                return pkVote;
            } catch (Exception e) {
            }
        }

        RoomPkVoteDTO pkVote = pkvoteDao.getEffectivePkVote(roomId);
        if (pkVote != null) {
            redisManager.hset(RedisKey.room_pk_vote.getKey(), String.valueOf(roomId), gson.toJson(pkVote));
        }
        return pkVote;
    }

    @Override
    public void clearRoomVote(RoomPkVoteDTO voteDto) {
        String voteFieldKey = voteDto.getId() + "_" + voteDto.getUid();
        String pkVoteFieldKey = voteDto.getId() + "_" + voteDto.getPkUid();
        redisManager.hdel(RedisKey.room_pk_vote.getKey(), String.valueOf(voteDto.getRoomId()));
        redisManager.hdel(RedisKey.room_pk_vote_set.getKey(), voteFieldKey);
        redisManager.hdel(RedisKey.room_pk_vote_set.getKey(), pkVoteFieldKey);
        redisManager.hdel(RedisKey.room_pk_vote_count.getKey(), voteFieldKey);
        redisManager.hdel(RedisKey.room_pk_vote_count.getKey(), pkVoteFieldKey);
    }

    @Override
    public Set<Long> getRoomVoteSet(Long voteId, Long uid) {
        String pklist = redisManager.hget(RedisKey.room_pk_vote_set.getKey(), voteId + "_" + uid);
        if (StringUtils.isNotBlank(pklist)) {
            try {
                return gson.fromJson(pklist, new TypeToken<HashSet<Long>>() {
                }.getType());
            } catch (Exception e) {
            }
        }
        return Sets.newHashSet();
    }

    @Override
    public RoomPkVoteResultDTO getPkVoteResult(RoomPkVoteDTO voteDto) {
        RoomPkVoteResultDTO resultDto = new RoomPkVoteResultDTO();
        long timestamps = System.currentTimeMillis();
        Long duration = (DateTimeUtils.addSecond(voteDto.getCreateTime(), voteDto.getExpireSeconds()).getTime() - timestamps) / 1000;
        resultDto.setVoteId(voteDto.getId());
        resultDto.setTimestamps(timestamps);
        resultDto.setDuration(duration <= 0 ? 0 : duration.intValue());
        resultDto.setPkType(voteDto.getPkType());

        UsersDTO users = usersManager.getUser(voteDto.getUid());
        resultDto.setUid(voteDto.getUid());
        resultDto.setNick(users.getNick());
        resultDto.setAvatar(users.getAvatar());
        resultDto.setVoteCount(voteDto.getVoteCount());
        resultDto.setPkUid(voteDto.getPkUid());

        users = usersManager.getUser(voteDto.getPkUid());
        resultDto.setPkNick(users.getNick());
        resultDto.setPkAvatar(users.getAvatar());
        resultDto.setPkVoteCount(voteDto.getPkVoteCount());
        return resultDto;
    }
}
