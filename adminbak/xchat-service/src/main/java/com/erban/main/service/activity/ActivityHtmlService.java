package com.erban.main.service.activity;

import com.erban.main.model.level.LevelCharmVo;
import com.erban.main.model.level.LevelExerpenceVo;
import com.erban.main.service.SysConfService;
import com.erban.main.service.base.CacheListBaseService;
import com.erban.main.service.level.LevelCharmService;
import com.erban.main.service.level.LevelExperienceService;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.activity.RankActVo;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ActivityHtmlService extends CacheListBaseService<RankActVo, RankActVo> {
    @Autowired
    private LevelExperienceService levelExperienceService;
    @Autowired
    private LevelCharmService levelCharmService;
    @Autowired
    private SysConfService sysConfService;

    private static String sql98 = "SELECT u.uid,u.erban_no as erbanNo,u.nick,u.avatar,SUM(o.sum_gold) as total FROM one_day_room_recv_sum o INNER JOIN users u on o.uid = u.uid INNER JOIN room r on o.uid = r.uid where r.is_permit_room = 1 and (o.create_time BETWEEN ? and ?) GROUP BY o.uid ORDER BY total DESC LIMIT 0, 50";
    private static String sql99 = "SELECT u.uid,u.erban_no as erbanNo,u.nick,u.avatar,SUM(o.sum_gold) as total FROM one_day_room_recv_sum o INNER JOIN users u on o.uid = u.uid INNER JOIN room r on o.uid = r.uid where r.is_permit_room = 3 and (o.create_time BETWEEN ? and ?) GROUP BY o.uid ORDER BY total DESC LIMIT 0, 50";

    private static final String sql36 = "select u.uid,u.erban_no as erbanNo,u.nick,u.avatar,SUM(gsr.total_gold_num) as total from gift_send_record gsr INNER JOIN users u on gsr.recive_uid = u.uid where gsr.gift_id = 76 GROUP BY gsr.recive_uid ORDER BY total desc limit 20 ";
    private static final String sql37 = "select u.uid,u.erban_no as erbanNo,u.nick,u.avatar,SUM(gsr.total_gold_num) as total from gift_send_record gsr INNER JOIN users u on gsr.recive_uid = u.uid where gsr.gift_id = 77 GROUP BY gsr.recive_uid ORDER BY total desc limit 20 ";
    private static final String sql38 = "select u.uid,u.erban_no as erbanNo,u.nick,u.avatar,SUM(gsr.total_gold_num) as total from gift_send_record gsr INNER JOIN users u on gsr.recive_uid = u.uid where gsr.gift_id = 78 GROUP BY gsr.recive_uid ORDER BY total desc limit 20 ";
    private static final String sql39 = "select u.uid,u.erban_no as erbanNo,u.nick,u.avatar,SUM(gsr.total_gold_num) as total from gift_send_record gsr INNER JOIN users u on gsr.recive_uid = u.uid where gsr.gift_id = 79 GROUP BY gsr.recive_uid ORDER BY total desc limit 20 ";
    private static final String sql40 = "select u.uid,u.erban_no as erbanNo,u.nick,u.avatar,SUM(gsr.total_gold_num) as total from gift_send_record gsr INNER JOIN users u on gsr.recive_uid = u.uid where gsr.gift_id = 80 GROUP BY gsr.recive_uid ORDER BY total desc limit 20 ";
    private static final String sql41 = "select u.uid,u.erban_no as erbanNo,u.nick,u.avatar,SUM(gsr.total_gold_num) as total from gift_send_record gsr INNER JOIN users u on gsr.recive_uid = u.uid where gsr.gift_id = 81 GROUP BY gsr.recive_uid ORDER BY total desc limit 20 ";

    private static final String sql42 = "select u.uid,u.erban_no as erbanNo,u.nick,u.avatar,SUM(gsr.total_gold_num) as total from gift_send_record gsr INNER JOIN users u on gsr.recive_uid = u.uid where gsr.gift_id = 82 GROUP BY gsr.recive_uid ORDER BY total desc limit 20 ";
    private static final String sql43 = "select u.uid,u.erban_no as erbanNo,u.nick,u.avatar,SUM(gsr.total_gold_num) as total from gift_send_record gsr INNER JOIN users u on gsr.recive_uid = u.uid where gsr.gift_id = 83 GROUP BY gsr.recive_uid ORDER BY total desc limit 20 ";
    private static final String sql44 = "select u.uid,u.erban_no as erbanNo,u.nick,u.avatar,SUM(gsr.total_gold_num) as total from gift_send_record gsr INNER JOIN users u on gsr.recive_uid = u.uid where gsr.gift_id = 84 GROUP BY gsr.recive_uid ORDER BY total desc limit 20 ";

    @Override
    public List<RankActVo> getListByJedisId(String jedisId) {
        if ("36".equals(jedisId)) {
            return getList(RedisKey.activity_html.getKey(), jedisId, sql36);
        } else if ("37".equals(jedisId)) {
            return getList(RedisKey.activity_html.getKey(), jedisId, sql37);
        } else if ("38".equals(jedisId)) {
            return getList(RedisKey.activity_html.getKey(), jedisId, sql38);
        } else if ("39".equals(jedisId)) {
            return getList(RedisKey.activity_html.getKey(), jedisId, sql39);
        } else if ("40".equals(jedisId)) {
            return getList(RedisKey.activity_html.getKey(), jedisId, sql40);
        } else if ("41".equals(jedisId)) {
            return getList(RedisKey.activity_html.getKey(), jedisId, sql41);
        } else if ("42".equals(jedisId)) {
            return getList(RedisKey.activity_html.getKey(), jedisId, sql42);
        } else if ("43".equals(jedisId)) {
            return getList(RedisKey.activity_html.getKey(), jedisId, sql43);
        } else if ("44".equals(jedisId)) {
            return getList(RedisKey.activity_html.getKey(), jedisId, sql44);
        } else if ("96".equals(jedisId)) {// 上周排行版数据，刷新是每周一0点0分刷新
            Date startTime = DateTimeUtil.getCurrentMonday(0, 0, 0);
            Date endTime = DateTimeUtil.getCurrentSunday(23, 59, 59);
            return getList(RedisKey.activity_html.getKey(), jedisId, sql98, DateTimeUtil.getLastDay(startTime, 7), DateTimeUtil.getLastDay(endTime, 7));
        } else if ("97".equals(jedisId)) {// 上周排行版数据，刷新是每周一0点0分刷新
            Date startTime = DateTimeUtil.getCurrentMonday(0, 0, 0);
            Date endTime = DateTimeUtil.getCurrentSunday(23, 59, 59);
            return getList(RedisKey.activity_html.getKey(), jedisId, sql99, DateTimeUtil.getLastDay(startTime, 7), DateTimeUtil.getLastDay(endTime, 7));
        } else if ("98".equals(jedisId)) {
            Map<String, Date> map = getTwoWeek();
            if (map != null) {
                return getList(RedisKey.activity_html.getKey(), jedisId, sql98, map.get("startDate"), map.get("endDate"));
            } else {
                return new ArrayList<>();
            }
        } else if ("99".equals(jedisId)) {
            Map<String, Date> map = getTwoWeek();
            if (map != null) {
                return getList(RedisKey.activity_html.getKey(), jedisId, sql99, map.get("startDate"), map.get("endDate"));
            } else {
                return new ArrayList<>();
            }
        } else {
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

    public Map<String, Date> getTwoWeek() {// 改成一周
        Map<String, Date> map = new HashMap<>();
        String start = sysConfService.getSysConfValueById("two_week_start");
        String end = sysConfService.getSysConfValueById("two_week_end");
        if (StringUtils.isBlank(start) || StringUtils.isBlank(end)) {
            return null;
        }
        Date startDate = DateTimeUtil.convertStrToDate(start);
        Date endDate = DateTimeUtil.convertStrToDate(end);
        Date now = new Date();
        if (now.getTime() - endDate.getTime() > 0) { // 今天比结束时间大，就刷新缓存
            startDate = DateTimeUtil.formatBeginDate(DateTimeUtil.getNextDay(endDate, 1));
            endDate = DateTimeUtil.formatEndDate(DateTimeUtil.getNextDay(startDate, 6));
            sysConfService.setConfValueById("two_week_start", DateTimeUtil.convertDate(startDate));
            sysConfService.setConfValueById("two_week_end", DateTimeUtil.convertDate(endDate));
        }
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        return map;
    }

    public void refreshLast() {
        Date startTime = DateTimeUtil.getCurrentMonday(0, 0, 0);
        Date endTime = DateTimeUtil.getCurrentSunday(23, 59, 59);
        refresh(RedisKey.activity_html.getKey(), "96", sql98, DateTimeUtil.getLastDay(startTime, 7), DateTimeUtil.getLastDay(endTime, 7));// 牌照房的两周排行
        refresh(RedisKey.activity_html.getKey(), "97", sql99, DateTimeUtil.getLastDay(startTime, 7), DateTimeUtil.getLastDay(endTime, 7));// 审核牌照房的两周排行
    }

    public void refreshAll() {
        try {
            Map<String, Date> map = getTwoWeek();
            if (map != null) {
                refresh(RedisKey.activity_html.getKey(), "98", sql98, map.get("startDate"), map.get("endDate"));// 牌照房的两周排行
                refresh(RedisKey.activity_html.getKey(), "99", sql99, map.get("startDate"), map.get("endDate"));// 审核牌照房的两周排行
            }
        } catch (Exception e) {
            logger.error("refreshTwoWeek error " + e.getMessage());
        }

        refresh(RedisKey.activity_html.getKey(), "42", sql42);
        refresh(RedisKey.activity_html.getKey(), "43", sql43);
        refresh(RedisKey.activity_html.getKey(), "44", sql44);

    }

    public BusiResult queryList(Integer type) {
        return new BusiResult(BusiStatus.SUCCESS, getListByJedisId(type.toString()));
    }

}
