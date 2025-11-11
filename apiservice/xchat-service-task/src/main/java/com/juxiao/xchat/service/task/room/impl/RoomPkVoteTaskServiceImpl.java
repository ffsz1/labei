package com.juxiao.xchat.service.task.room.impl;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.DefMsgType;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.DateTimeUtils;
import com.juxiao.xchat.base.utils.UUIDUtils;
import com.juxiao.xchat.dao.room.dto.RoomPkVoteDTO;
import com.juxiao.xchat.dao.room.dto.RoomPkVoteResultDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.common.room.RoomPkVoteManager;
import com.juxiao.xchat.manager.external.netease.NetEaseRoomManager;
import com.juxiao.xchat.manager.external.netease.bo.Attach;
import com.juxiao.xchat.service.task.room.RoomPkVoteTaskService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

@Service
public class RoomPkVoteTaskServiceImpl implements RoomPkVoteTaskService {
    private Logger logger = LoggerFactory.getLogger(RoomPkVoteTaskService.class);
    @Autowired
    private Gson gson;
    @Autowired
    private SystemConf systemConf;
    @Autowired
    private NetEaseRoomManager neteaseRoomManager;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private RoomPkVoteManager pkvoteManager;


    @Override
    public int checkFinish() {
        String lockVal = redisManager.lock(RedisKey.room_pk_task_lock.getKey(), 5000);
        if (StringUtils.isEmpty(lockVal)) {
            return 0;
        }

        try {
            Map<String, String> pkVotes = redisManager.hgetAll(RedisKey.room_pk_vote.getKey());
            if (pkVotes == null || pkVotes.size() == 0) {
                return 0;
            }

            Iterator<Map.Entry<String, String>> iterator = pkVotes.entrySet().iterator();

            RoomPkVoteDTO pkVoteDto;
            Map.Entry<String, String> entry;
            RoomPkVoteResultDTO resultDto;
            int result = 0;
            while (iterator.hasNext()) {
                entry = iterator.next();
                pkVoteDto = gson.fromJson(entry.getValue(), RoomPkVoteDTO.class);
                if (new Date().before(DateTimeUtils.addSecond(pkVoteDto.getCreateTime(), pkVoteDto.getExpireSeconds() + 1))) {
                    continue;
                }
                result++;
                pkvoteManager.clearRoomVote(pkVoteDto);
                resultDto = pkvoteManager.getPkVoteResult(pkVoteDto);

                Attach attach = new Attach();
                attach.setFirst(DefMsgType.RoomPk);
                attach.setSecond(DefMsgType.RoomPkResult);
                attach.setData(resultDto);
                neteaseRoomManager.sendChatRoomMsg(pkVoteDto.getRoomId(), UUIDUtils.get(), systemConf.getSecretaryUid(), 100, JSON.toJSONString(attach));
                logger.info("[ 房间PK ] 发送最终结果，roomId:>{}, data:>{}", pkVoteDto.getRoomId(), JSON.toJSONString(resultDto));
            }
            return result;
        } catch (Exception e) {
            logger.error("[ 房间PK ] 处理异常：", e);
            return 0;
        } finally {
            redisManager.unlock(RedisKey.room_pk_task_lock.getKey(), lockVal);
        }
    }
}
