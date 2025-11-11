package com.erban.main.service.activity;

import com.erban.main.model.level.LevelCharmVo;
import com.erban.main.model.level.LevelExerpenceVo;
import com.erban.main.service.base.CacheListBaseService;
import com.erban.main.service.level.LevelCharmService;
import com.erban.main.service.level.LevelExperienceService;
import com.erban.main.vo.activity.RankActVo;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AprilFoolsDayActivityService extends CacheListBaseService<RankActVo,RankActVo> {
    @Autowired
    private LevelExperienceService levelExperienceService;
    @Autowired
    private LevelCharmService levelCharmService;

    @Override
    public List<RankActVo> getListByJedisId(String jedisId) {
        if("1".equals(jedisId)){
            return getList(RedisKey.april_fools_day.getKey(), jedisId, "select u.uid,u.erban_no as erbanNo,u.nick,u.avatar,SUM(gsr.total_gold_num) as total from gift_send_record gsr INNER JOIN users u on gsr.recive_uid = u.uid where gsr.gift_id = 18 GROUP BY gsr.recive_uid ORDER BY total desc limit 20 ");
        }else if("2".equals(jedisId)){
            return getList(RedisKey.april_fools_day.getKey(), jedisId, "select u.uid,u.erban_no as erbanNo,u.nick,u.avatar,COUNT(DISTINCT gsr.uid) as total from gift_send_record gsr INNER JOIN users u on gsr.recive_uid = u.uid where gsr.gift_id = 18 GROUP BY gsr.recive_uid ORDER BY total desc limit 20 ");
        }else if("3".equals(jedisId)){
            return getList(RedisKey.april_fools_day.getKey(), jedisId, "select u.uid,u.erban_no as erbanNo,u.nick,u.avatar,SUM(gsr.total_gold_num) as total from gift_send_record gsr INNER JOIN users u on gsr.uid = u.uid where gsr.gift_id = 18 GROUP BY gsr.uid ORDER BY total desc limit 20 ");
        }else {
            return new ArrayList<>();
        }
    }

    @Override
    public RankActVo entityToCache(RankActVo entity) {
        LevelExerpenceVo levelExerpenceVo = levelExperienceService.getLevelExperience(entity.getUid());
        if (levelExerpenceVo != null) {
            entity.setExperLevel(Integer.valueOf(levelExerpenceVo.getLevelName().substring(2)));
        }
        LevelCharmVo levelCharmVo = levelCharmService.getLevelCharm(entity.getUid());
        if (levelCharmVo != null) {
            entity.setCharmLevel(Integer.valueOf(levelCharmVo.getLevelName().substring(2)));
        }
        return entity;
    }

    public void refreshAll(){
        refresh(RedisKey.april_fools_day.getKey(), "1", "select u.uid,u.erban_no as erbanNo,u.nick,u.avatar,SUM(gsr.total_gold_num) as total from gift_send_record gsr INNER JOIN users u on gsr.recive_uid = u.uid where gsr.gift_id = 18 GROUP BY gsr.recive_uid ORDER BY total desc limit 20 ");
        refresh(RedisKey.april_fools_day.getKey(), "2", "select u.uid,u.erban_no as erbanNo,u.nick,u.avatar,COUNT(DISTINCT gsr.uid) as total from gift_send_record gsr INNER JOIN users u on gsr.recive_uid = u.uid where gsr.gift_id = 18 GROUP BY gsr.recive_uid ORDER BY total desc limit 20 ");
        refresh(RedisKey.april_fools_day.getKey(), "3", "select u.uid,u.erban_no as erbanNo,u.nick,u.avatar,SUM(gsr.total_gold_num) as total from gift_send_record gsr INNER JOIN users u on gsr.uid = u.uid where gsr.gift_id = 18 GROUP BY gsr.uid ORDER BY total desc limit 20 ");
    }

    public BusiResult queryList(Integer type) {
        return new BusiResult(BusiStatus.SUCCESS, getListByJedisId(type.toString()));
    }

}
