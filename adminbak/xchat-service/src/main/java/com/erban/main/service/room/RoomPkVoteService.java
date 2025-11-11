package com.erban.main.service.room;

import com.alibaba.fastjson.JSON;
import com.erban.main.config.SystemConfig;
import com.erban.main.model.RoomPkVote;
import com.erban.main.model.Users;
import com.erban.main.model.dto.RoomPkVoteDTO;
import com.erban.main.service.SendChatRoomMsgService;
import com.erban.main.service.common.JedisLockService;
import com.erban.main.service.user.UsersService;
import com.google.gson.Gson;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.utils.DateTimeUtil;
import com.xchat.oauth2.service.service.JedisService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoomPkVoteService {
    private final Logger logger = LoggerFactory.getLogger(RoomPkVoteService.class);
    private final Gson gson = new Gson();
    @Autowired
    private JedisService jedisService;
    @Autowired
    private JedisLockService jedisLockService;
    @Autowired
    private SendChatRoomMsgService msgService;
    @Autowired
    private UsersService usersService;

    /**
     * 检查并处理结束的PK投票
     *
     * @return
     */
    public int checkFinish() {
        String lockVal = jedisLockService.lock(RedisKey.room_pk_task_lock.getKey(), 5000);
        if (StringUtils.isEmpty(lockVal)) {
            return 0;
        }

        try {
            Map<String, String> pkVotes = jedisService.hgetAll(RedisKey.room_pk_vote.getKey());
            if (pkVotes == null || pkVotes.size() == 0) {
                return 0;
            }

            Iterator<Map.Entry<String, String>> iterator = pkVotes.entrySet().iterator();
            RoomPkVote pkVote;
            Map.Entry<String, String> entry;
            RoomPkVoteDTO voteDto;
            int result = 0;
            while (iterator.hasNext()) {
                entry = iterator.next();
                pkVote = gson.fromJson(entry.getValue(), RoomPkVote.class);
                if (new Date().before(DateTimeUtil.addSecond(pkVote.getCreateTime(), pkVote.getExpireSeconds() + 1))) {
                    continue;
                }
                result++;
                this.clearRoomVote(pkVote);
                voteDto = this.getPkVoteResult(pkVote);
                try {
                    msgService.sendSendChatRoomMsg(pkVote.getRoomId(), SystemConfig.secretaryUid, Constant.DefMsgType.RoomPk, Constant.DefMsgType.RoomPkResult, voteDto);
                    logger.info("[ 房间PK ] 发送最终结果，roomId:>{}, data:>{}", pkVote.getRoomId(), JSON.toJSONString(voteDto));
                } catch (Exception e) {
                    logger.error("[ 房间PK ] 发送结束消息异常：", e);
                }
            }
            return result;
        } catch (Exception e) {
            logger.error("[ 房间PK ] 处理异常：", e);
            return 0;
        } finally {
            jedisLockService.unlock(RedisKey.room_pk_task_lock.getKey(), lockVal);
        }
    }

    private void clearRoomVote(RoomPkVote pkVote) {
        String voteFieldKey = pkVote.getId() + "_" + pkVote.getUid();
        String pkVoteFieldKey = pkVote.getId() + "_" + pkVote.getPkUid();
        jedisService.hdel(RedisKey.room_pk_vote.getKey(), String.valueOf(pkVote.getRoomId()));
        jedisService.hdel(RedisKey.room_pk_vote_set.getKey(), voteFieldKey);
        jedisService.hdel(RedisKey.room_pk_vote_set.getKey(), pkVoteFieldKey);
        jedisService.hdel(RedisKey.room_pk_vote_count.getKey(), voteFieldKey);
        jedisService.hdel(RedisKey.room_pk_vote_count.getKey(), pkVoteFieldKey);
    }


    private RoomPkVoteDTO getPkVoteResult(RoomPkVote pkVote) {
        RoomPkVoteDTO voteDto = new RoomPkVoteDTO();
        long timestamps = System.currentTimeMillis();
        long duration = (DateTimeUtil.addSecond(pkVote.getCreateTime(), pkVote.getExpireSeconds()).getTime() - timestamps) / 1000;
        voteDto.setVoteId(pkVote.getId());
        voteDto.setTimestamps(timestamps);
        voteDto.setDuration(duration <= 0 ? 0 : (int) duration);
        voteDto.setPkType(pkVote.getPkType());
        voteDto.setCreateTime(pkVote.getCreateTime());

        Users users = usersService.getUsersByUid(pkVote.getUid());
        voteDto.setUid(pkVote.getUid());
        voteDto.setNick(users == null ? "" : users.getNick());
        voteDto.setAvatar(users == null ? "" : users.getAvatar());
        voteDto.setVoteCount(pkVote.getVoteCount());

        users = usersService.getUsersByUid(pkVote.getPkUid());
        voteDto.setPkUid(pkVote.getPkUid());
        voteDto.setPkNick(users == null ? "" : users.getNick());
        voteDto.setPkAvatar(users == null ? "" : users.getAvatar());
        voteDto.setPkVoteCount(pkVote.getPkVoteCount());
        return voteDto;
    }


}
