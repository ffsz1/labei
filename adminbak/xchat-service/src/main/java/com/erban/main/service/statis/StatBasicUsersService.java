package com.erban.main.service.statis;

import com.erban.main.model.StatBasicRoom;
import com.erban.main.model.StatBasicUsers;
import com.erban.main.model.StatBasicUsersExample;
import com.erban.main.mybatismapper.StatBasicUsersMapper;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.GetTimeUtils;
import com.xchat.common.utils.StringUtils;
import com.xchat.oauth2.service.service.JedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class StatBasicUsersService {
    private static final Logger logger = LoggerFactory.getLogger(StatBasicUsersService.class);
    @Autowired
    private JedisService jedisService;
    @Autowired
    private StatBasicUsersMapper statbasicUsersMapper;
    @Autowired
    private StatBasicRoomService statBasicRoomService;

    public BusiResult addBasicUser(Long uid, Long roomUid) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        if (uid.longValue() == roomUid) {
            return new BusiResult(BusiStatus.SUCCESS);
        }
        insertUsers(uid, roomUid);

        // 大于60分钟停止
        String minute = jedisService.hget(RedisKey.daily_room_time.getKey(), uid.toString());
        if (StringUtils.isEmpty(minute)) {
            jedisService.hincr(RedisKey.daily_room_time.getKey(), uid.toString());
        } else {
            try {
                if (Long.valueOf(minute) < 60) {
                    jedisService.hincr(RedisKey.daily_room_time.getKey(), uid.toString());
                }
            } catch (Exception e) {
            }
        }


        return busiResult;
    }

    private void insertUsers(Long uid, Long roomUid) {
        StatBasicUsers statBasicUsers = new StatBasicUsers();
        statBasicUsers.setRoomUid(roomUid);
        statBasicUsers.setUid(uid);
        statBasicUsers.setCreateTime(new Date());
        statbasicUsersMapper.insertSelective(statBasicUsers);
    }

    public List<StatBasicRoom> queryBasicUsers() {
        logger.info("正在查询房间当天进入人数");
        Date date = new Date(System.currentTimeMillis() - 87400000);
        StatBasicUsersExample example = new StatBasicUsersExample();
        example.createCriteria().andCreateTimeBetween(GetTimeUtils.getTimesnights(date, 0), GetTimeUtils.getTimesnight(0));
        example.setOrderByClause("uid asc");
        List<StatBasicUsers> statBasicUsersList = statbasicUsersMapper.selectByExample(example);
        for (int x = 0; x < statBasicUsersList.size(); x++) {
            if (x != 0) {
                if (statBasicUsersList.get(x).getUid().equals(statBasicUsersList.get(x - 1).getUid()) &&
                        statBasicUsersList.get(x).getRoomUid().equals(statBasicUsersList.get(x - 1).getRoomUid())) {
                    statBasicUsersList.remove(x);
                    x--;
                }
            }
        }
        List<StatBasicRoom> statBasicRoomList = statBasicRoomService.updateRoom(statBasicUsersList);
        return statBasicRoomList;
    }
}
