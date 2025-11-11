package com.juxiao.xchat.service.task.room.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.DateTimeUtils;
import com.juxiao.xchat.base.utils.DateUtils;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.item.dto.RoomDrawGiftDayDTO;
import com.juxiao.xchat.dao.user.UserDrawGiftDayDao;
import com.juxiao.xchat.dao.user.UserDrawGiftRecordDao;
import com.juxiao.xchat.dao.user.UserDrawGiftRoomDayDao;
import com.juxiao.xchat.dao.user.domain.UserDrawGiftDayDO;
import com.juxiao.xchat.dao.user.domain.UserDrawGiftRankDO;
import com.juxiao.xchat.dao.user.domain.UserDrawGiftRoomDayDO;
import com.juxiao.xchat.dao.user.dto.UserDrawGiftDayDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.item.constant.GiftType;
import com.juxiao.xchat.manager.common.level.LevelManager;
import com.juxiao.xchat.manager.common.level.vo.LevelVO;
import com.juxiao.xchat.service.task.room.GiftDrawService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class GiftDrawServiceImpl implements GiftDrawService {
    @Autowired
    private Gson gson;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserDrawGiftRecordDao drawGiftRecordDao;
    @Autowired
    private UserDrawGiftRoomDayDao drawGiftRoomDayDao;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private LevelManager levelManager;

    @Autowired
    private UserDrawGiftDayDao userDrawGiftDayDao;


    @Override
    public void refreshRank(Integer type) throws WebServiceException {
//        1 今天
//        2.昨天
//        3.本周
        Date now = new Date();
        Date endDate = DateTimeUtils.setTime(now, 23, 59, 59, 999);
        Date startDate;
        if (type == 1) {
            startDate = DateTimeUtils.addDay(now, 0);
        } else if (type == 2) {
            startDate = DateTimeUtils.addDay(now, -1);
        } else if (type == 3) {
            startDate = DateUtils.getCurrentMonday();
        } else {
            return;
        }

        startDate = DateTimeUtils.setTime(startDate, 0, 0, 0);

        List<UserDrawGiftRankDO> drawRanks = drawGiftRecordDao.listRoomRankByGiftType(GiftType.DRAW, startDate, endDate);
        redisManager.hset(RedisKey.gift_draw_rank.getKey(), type+"_"+GiftType.DRAW, gson.toJson(drawRanks));

        List<UserDrawGiftRankDO> xqRanks = drawGiftRecordDao.listRoomRankByGiftType(GiftType.XIANGQIN,startDate, endDate);
        redisManager.hset(RedisKey.gift_draw_rank.getKey(), type+"_"+GiftType.XIANGQIN, gson.toJson(xqRanks));

        List<Map<String, Object>> result = jdbcTemplate.queryForList("select a.uid, u.erban_no as erbanNo, u.nick, u.gender, u.avatar, 0 as experLevel, 0 as charmLevel, SUM(b.gold_price * a.gift_num) as tol from bill_gift_draw a INNER JOIN users u on a.uid = u.uid INNER JOIN gift b on a.gift_id = b.gift_id where a.create_time >= ? and a.create_time <= ? GROUP BY a.uid ORDER BY tol desc limit 20", startDate, endDate);
        LevelVO levelVO;
        LevelVO levelCharmVO;
        Object uid;
        for (Map<String, Object> re : result) {
            uid = re.get("uid");
            if (uid != null) {
                levelVO = levelManager.getLevelExperience(Long.valueOf(uid.toString()));
                if (levelVO != null) {
                    re.put("experLevel", Integer.valueOf(levelVO.getLevelName().substring(2)));
                }
                levelCharmVO = levelManager.getLevelCharm(Long.valueOf(uid.toString()));
                if (levelCharmVO != null) {
                    re.put("charmLevel", Integer.valueOf(levelCharmVO.getLevelName().substring(2)));
                }
            }
        }
        redisManager.hset(RedisKey.gift_draw_rank.getKey(), type.toString(), gson.toJson(result));
    }

    @Override
    public void deleteDrawGiftNum() {
        redisManager.del(RedisKey.gift_draw_event_num.getKey());
        redisManager.del(RedisKey.gift_draw_input.getKey());
        redisManager.del(RedisKey.gift_draw_output.getKey());
        redisManager.del(RedisKey.user_gift_draw_output.getKey());
        redisManager.del(RedisKey.user_gift_draw_input.getKey());

        redisManager.del(RedisKey.xqgift_draw_input.getKey());
        redisManager.del(RedisKey.xqgift_draw_output.getKey());
        redisManager.del(RedisKey.xquser_gift_draw_output.getKey());
        redisManager.del(RedisKey.xquser_gift_draw_input.getKey());
    }

    @Override
    public void countGiftDayDraw() {
        Date now = new Date();
        Date startDate = DateTimeUtils.setTime(now, 0, 0, 0);
        Date endDate = DateTimeUtils.setTime(now, 23, 59, 59);
        List<RoomDrawGiftDayDTO> list = drawGiftRecordDao.listDayRoomDrawGift(startDate, endDate);

        UserDrawGiftRoomDayDO roomDayDo;
        for (RoomDrawGiftDayDTO drawGift : list) {
            roomDayDo = new UserDrawGiftRoomDayDO();
            BeanUtils.copyProperties(drawGift, roomDayDo);
            roomDayDo.setCreateDate(startDate);
            drawGiftRoomDayDao.saveOrUpdate(roomDayDo);
        }
    }

    /**
     * 统计用户当天的捡海螺流水
     */
    @Override
    public void countUserDayDraw() {
        Date now = new Date();
        Date yesterday = DateTimeUtils.addDay(now, -1);
        Date startDate = DateTimeUtils.setTime(yesterday, 0, 0, 0);
        Date endDate = DateTimeUtils.setTime(yesterday, 23, 59, 59);
        List<UserDrawGiftDayDTO> list = userDrawGiftDayDao.listDayUsersDrawGift(startDate, endDate);
        UserDrawGiftDayDO userDrawGiftDayDO;
        for (UserDrawGiftDayDTO drawGift : list) {
            userDrawGiftDayDO = new UserDrawGiftDayDO();
            userDrawGiftDayDO.setCreateDate(startDate);
            userDrawGiftDayDO.setInputNum(drawGift.getInputNum());
            userDrawGiftDayDO.setOutNum(drawGift.getOutputNum());
            userDrawGiftDayDO.setUid(drawGift.getUid());
            userDrawGiftDayDao.saveOrUpdate(userDrawGiftDayDO);
        }
    }
}
