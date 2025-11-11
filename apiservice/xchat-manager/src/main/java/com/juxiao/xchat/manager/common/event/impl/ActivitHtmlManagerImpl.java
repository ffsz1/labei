package com.juxiao.xchat.manager.common.event.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.DateFormatUtils;
import com.juxiao.xchat.base.utils.DateTimeUtils;
import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.event.ActivityHtmlDAO;
import com.juxiao.xchat.dao.event.dto.ActivityHtmlDTO;
import com.juxiao.xchat.dao.item.GiftSendRecordDao;
import com.juxiao.xchat.dao.item.domain.RankActDO;
import com.juxiao.xchat.dao.item.query.RoomRankQuery;
import com.juxiao.xchat.dao.room.dto.RoomTagDTO;
import com.juxiao.xchat.dao.sysconf.dto.SysConfDTO;
import com.juxiao.xchat.dao.sysconf.enumeration.SysConfigId;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.base.CacheListBaseManager;
import com.juxiao.xchat.manager.common.event.ActivityHtmlManager;
import com.juxiao.xchat.manager.common.event.vo.ActivityHtmlVO;
import com.juxiao.xchat.manager.common.event.vo.RankActVo;
import com.juxiao.xchat.manager.common.level.LevelManager;
import com.juxiao.xchat.manager.common.room.RoomTagManager;
import com.juxiao.xchat.manager.common.sysconf.SysConfManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivitHtmlManagerImpl extends CacheListBaseManager<RankActDO, RankActDO> implements ActivityHtmlManager {
    @Resource
    private Gson gson;

    @Resource
    private GiftSendRecordDao giftSendRecordDao;

    @Resource
    private RedisManager redisManager;

    @Resource
    private RoomTagManager roomTagManager;

    @Resource
    private LevelManager levelManager;

    @Resource
    private SysConfManager sysConfManager;

    @Resource
    private ActivityHtmlDAO activityHtmlDAO;

    private static String sql98 = "SELECT u.uid,u.erban_no as erbanNo,u.nick,u.avatar,SUM(o.sum_gold) as total FROM " +
            "one_day_room_recv_sum o INNER JOIN users u on o.uid = u.uid INNER JOIN room r on o.uid = r.uid where r" +
            ".is_permit_room = 1 and (o.create_time BETWEEN ? and ?) GROUP BY o.uid ORDER BY total DESC LIMIT 0, 50";

    private static String sql99 = "SELECT u.uid,u.erban_no as erbanNo,u.nick,u.avatar,SUM(o.sum_gold) as total FROM " +
            "one_day_room_recv_sum o INNER JOIN users u on o.uid = u.uid INNER JOIN room r on o.uid = r.uid where r" +
            ".is_permit_room = 3 and (o.create_time BETWEEN ? and ?) GROUP BY o.uid ORDER BY total DESC LIMIT 0, 50";

    private static String sql36 = "select u.uid,u.erban_no as erbanNo,u.nick,u.avatar,SUM(gsr.total_gold_num) as " +
            "total from gift_send_record gsr INNER JOIN users u on gsr.recive_uid = u.uid where gsr.gift_id = 76 " +
            "GROUP BY gsr.recive_uid ORDER BY total desc limit 20 ";

    private static String sql37 = "select u.uid,u.erban_no as erbanNo,u.nick,u.avatar,SUM(gsr.total_gold_num) as " +
            "total from gift_send_record gsr INNER JOIN users u on gsr.recive_uid = u.uid where gsr.gift_id = 77 " +
            "GROUP BY gsr.recive_uid ORDER BY total desc limit 20 ";

    private static String sql38 = "select u.uid,u.erban_no as erbanNo,u.nick,u.avatar,SUM(gsr.total_gold_num) as " +
            "total from gift_send_record gsr INNER JOIN users u on gsr.recive_uid = u.uid where gsr.gift_id = 78 " +
            "GROUP BY gsr.recive_uid ORDER BY total desc limit 20 ";

    private static String sql39 = "select u.uid,u.erban_no as erbanNo,u.nick,u.avatar,SUM(gsr.total_gold_num) as " +
            "total from gift_send_record gsr INNER JOIN users u on gsr.recive_uid = u.uid where gsr.gift_id = 79 " +
            "GROUP BY gsr.recive_uid ORDER BY total desc limit 20 ";

    private static String sql40 = "select u.uid,u.erban_no as erbanNo,u.nick,u.avatar,SUM(gsr.total_gold_num) as " +
            "total from gift_send_record gsr INNER JOIN users u on gsr.recive_uid = u.uid where gsr.gift_id = 80 " +
            "GROUP BY gsr.recive_uid ORDER BY total desc limit 20 ";

    private static String sql41 = "select u.uid,u.erban_no as erbanNo,u.nick,u.avatar,SUM(gsr.total_gold_num) as " +
            "total from gift_send_record gsr INNER JOIN users u on gsr.recive_uid = u.uid where gsr.gift_id = 81 " +
            "GROUP BY gsr.recive_uid ORDER BY total desc limit 20 ";

    private static String sql42 = "select u.uid,u.erban_no as erbanNo,u.nick,u.avatar,SUM(gsr.total_gold_num) as " +
            "total from gift_send_record gsr INNER JOIN users u on gsr.recive_uid = u.uid where gsr.gift_id = 82 " +
            "GROUP BY gsr.recive_uid ORDER BY total desc limit 20 ";

    private static String sql43 = "select u.uid,u.erban_no as erbanNo,u.nick,u.avatar,SUM(gsr.total_gold_num) as " +
            "total from gift_send_record gsr INNER JOIN users u on gsr.recive_uid = u.uid where gsr.gift_id = 83 " +
            "GROUP BY gsr.recive_uid ORDER BY total desc limit 20 ";

    private static String sql44 = "select u.uid,u.erban_no as erbanNo,u.nick,u.avatar,SUM(gsr.total_gold_num) as " +
            "total from gift_send_record gsr INNER JOIN users u on gsr.recive_uid = u.uid where gsr.gift_id = 84 " +
            "GROUP BY gsr.recive_uid ORDER BY total desc limit 20 ";

    private static String sql45 = "select u.uid,u.erban_no as erbanNo,u.nick,u.avatar,SUM(gsr.total_gold_num) as " +
            "total from gift_send_record gsr INNER JOIN users u on gsr.recive_uid = u.uid where gsr.gift_id = 86 " +
            "GROUP BY gsr.recive_uid ORDER BY total desc limit 20";

    private static String sql46 = "select u.uid,u.erban_no as erbanNo,u.nick,u.avatar,SUM(gsr.total_gold_num) as " +
            "total from gift_send_record gsr INNER JOIN users u on gsr.recive_uid = u.uid where gsr.gift_id = 87 " +
            "GROUP BY gsr.recive_uid ORDER BY total desc limit 20";

    private static String sql47 = "select u.uid,u.erban_no as erbanNo,u.nick,u.avatar,SUM(gsr.total_gold_num) as " +
            "total from gift_send_record gsr INNER JOIN users u on gsr.recive_uid = u.uid where gsr.gift_id = 88 " +
            "GROUP BY gsr.recive_uid ORDER BY total desc limit 20";

    private static String sql48 = "SELECT u.uid, u.erban_no AS erbanNo, u.nick, u.avatar, SUM( gsr.total_gold_num ) " +
            "AS total FROM gift_send_record gsr INNER JOIN users u ON gsr.recive_uid = u.uid WHERE gsr.gift_id IN ( " +
            "86, 87, 88 ) AND gsr.create_time > '2018-10-16 14:00:00' AND gsr.create_time < '2018-10-19 23:59:59' " +
            "GROUP BY gsr.recive_uid ORDER BY total DESC LIMIT 20";

    private static String sql49 = "select u.uid,u.erban_no as erbanNo,u.nick,u.avatar,SUM(gsr.total_gold_num) as " +
            "total from gift_send_record gsr INNER JOIN users u on gsr.recive_uid = u.uid where gsr.gift_id = 93 " +
            "GROUP BY gsr.recive_uid ORDER BY total desc limit 20";

    private static String sql50 = "select u.uid,u.erban_no as erbanNo,u.nick,u.avatar,SUM(gsr.total_gold_num) as " +
            "total from gift_send_record gsr INNER JOIN users u on gsr.recive_uid = u.uid where gsr.gift_id = 94 " +
            "GROUP BY gsr.recive_uid ORDER BY total desc limit 20";

    private static String sql51 = "select u.uid,u.erban_no as erbanNo,u.nick,u.avatar,SUM(gsr.total_gold_num) as " +
            "total from gift_send_record gsr INNER JOIN users u on gsr.recive_uid = u.uid where gsr.gift_id = 95 " +
            "GROUP BY gsr.recive_uid ORDER BY total desc limit 20";

    @Override
    public List<RankActDO> getListByJedisId(String jedisId) {
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
        } else if ("45".equals(jedisId)) {
            return getList(RedisKey.activity_html.getKey(), jedisId, sql45);
        } else if ("46".equals(jedisId)) {
            return getList(RedisKey.activity_html.getKey(), jedisId, sql46);
        } else if ("47".equals(jedisId)) {
            return getList(RedisKey.activity_html.getKey(), jedisId, sql47);
        } else if ("48".equals(jedisId)) {
            return getList(RedisKey.activity_html.getKey(), jedisId, sql48);
        } else if ("49".equals(jedisId)) {
            return getList(RedisKey.activity_html.getKey(), jedisId, sql49);
        } else if ("50".equals(jedisId)) {
            return getList(RedisKey.activity_html.getKey(), jedisId, sql50);
        } else if ("51".equals(jedisId)) {
            return getList(RedisKey.activity_html.getKey(), jedisId, sql51);
        } else if ("96".equals(jedisId)) {// 上周排行版数据，刷新是每周一0点0分刷新
            Date startTime = DateTimeUtils.getCurrentMonday(0, 0, 0);
            Date endTime = DateTimeUtils.getCurrentSunday(23, 59, 59);
            return getList(RedisKey.activity_html.getKey(), jedisId, sql98, DateTimeUtils.getLastDay(startTime, 7),
                    DateTimeUtils.getLastDay(endTime, 7));
        } else if ("97".equals(jedisId)) {// 上周排行版数据，刷新是每周一0点0分刷新
            Date startTime = DateTimeUtils.getCurrentMonday(0, 0, 0);
            Date endTime = DateTimeUtils.getCurrentSunday(23, 59, 59);
            return getList(RedisKey.activity_html.getKey(), jedisId, sql99, DateTimeUtils.getLastDay(startTime, 7),
                    DateTimeUtils.getLastDay(endTime, 7));
        } else if ("98".equals(jedisId)) {
            Map<String, Date> map = getOneWeek();
            if (map != null) {
                return getList(RedisKey.activity_html.getKey(), jedisId, sql98, map.get("startDate"), map.get(
                        "endDate"));
            } else {
                return Lists.newArrayList();
            }
        } else if ("99".equals(jedisId)) {
            Map<String, Date> map = getOneWeek();
            if (map != null) {
                return getList(RedisKey.activity_html.getKey(), jedisId, sql99, map.get("startDate"), map.get(
                        "endDate"));
            } else {
                return Lists.newArrayList();
            }
        } else {
            return Lists.newArrayList();
        }
    }

    @Override
    public RankActDO entityToCache(RankActDO entity) {
        entity.setExperLevel(levelManager.getUserExperienceLevelSeq(entity.getUid()));
        entity.setCharmLevel(levelManager.getUserCharmLevelSeq(entity.getUid()));
        return entity;
    }

    public Map<String, Date> getOneWeek() {// 改成一周
        Map<String, Date> map = new HashMap<>();
        SysConfDTO sysConfDTO1 = sysConfManager.getSysConf(SysConfigId.two_week_start);
        String start = sysConfDTO1 == null ? null : sysConfDTO1.getConfigValue();
        SysConfDTO sysConfDTO2 = sysConfManager.getSysConf(SysConfigId.two_week_end);
        String end = sysConfDTO2 == null ? null : sysConfDTO2.getConfigValue();
        if (StringUtils.isBlank(start) || StringUtils.isBlank(end)) {
            return null;
        }
        Date startDate = DateTimeUtils.convertStrToDate(start);
        Date endDate = DateTimeUtils.convertStrToDate(end);
        Date now = new Date();
        if (now.getTime() - endDate.getTime() > 0) { // 今天比结束时间大，就刷新缓存
            startDate = DateTimeUtils.formatBeginDate(DateTimeUtils.getLastDay(endDate, -1));
            endDate = DateTimeUtils.formatEndDate(DateTimeUtils.getLastDay(startDate, -6));
            sysConfManager.setConfValueById(SysConfigId.two_week_start, DateTimeUtils.convertDate(startDate));
            sysConfManager.setConfValueById(SysConfigId.two_week_end, DateTimeUtils.convertDate(endDate));
        }
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        return map;
    }

    @Override
    @TargetDataSource(name = "ds2")
    public void refreshLast() {
        Date startTime = DateTimeUtils.getCurrentMonday(0, 0, 0);
        Date endTime = DateTimeUtils.getCurrentSunday(23, 59, 59);
        refresh(RedisKey.activity_html.getKey(), "96", sql98, DateTimeUtils.getLastDay(startTime, 7),
                DateTimeUtils.getLastDay(endTime, 7));// 牌照房的一周排行
        refresh(RedisKey.activity_html.getKey(), "97", sql99, DateTimeUtils.getLastDay(startTime, 7),
                DateTimeUtils.getLastDay(endTime, 7));// 审核牌照房的一周排行
    }

    @Override
    @TargetDataSource(name = "ds2")
    public void refreshOneWeek() {
        Map<String, Date> map = getOneWeek();
        if (map != null) {
            refresh(RedisKey.activity_html.getKey(), "98", sql98, map.get("startDate"), map.get("endDate"));// 牌照房的一周排行
            refresh(RedisKey.activity_html.getKey(), "99", sql99, map.get("startDate"), map.get("endDate"));//
            // 审核牌照房的一周排行
        }
    }

    @Override
    @TargetDataSource(name = "ds2")
    public void refreshAll() {
        // refresh(RedisKey.activity_html.getKey(), "36", sql36);
        // refresh(RedisKey.activity_html.getKey(), "37", sql37);
        // refresh(RedisKey.activity_html.getKey(), "38", sql38);
        // refresh(RedisKey.activity_html.getKey(), "39", sql39);
        // refresh(RedisKey.activity_html.getKey(), "40", sql40);
        // refresh(RedisKey.activity_html.getKey(), "41", sql41);
        // refresh(RedisKey.activity_html.getKey(), "42", sql42);
        // refresh(RedisKey.activity_html.getKey(), "43", sql43);
        // refresh(RedisKey.activity_html.getKey(), "44", sql44);
        // refresh(RedisKey.activity_html.getKey(), "45", sql45);
        // refresh(RedisKey.activity_html.getKey(), "46", sql46);
        // refresh(RedisKey.activity_html.getKey(), "47", sql47);
        // refresh(RedisKey.activity_html.getKey(), "48", sql48);
        // refresh(RedisKey.activity_html.getKey(), "49", sql49);
        // refresh(RedisKey.activity_html.getKey(), "50", sql50);
        // refresh(RedisKey.activity_html.getKey(), "51", sql51);
    }

    @Override
    public List<RankActDO> queryList(Integer type) {
        return getListByJedisId(type.toString());
    }

    @Override
    public List<RankActVo> getRoomDayRank(Integer type) {
        if (type == null) {
            return Lists.newArrayList();
        }
        String result = redisManager.hget(RedisKey.room_day_rank.getKey(), type.toString());
        List<RankActVo> list;
        if (StringUtils.isBlank(result)) {
            String date = DateFormatUtils.YYYY_MM_DD.date2Str(new Date());
            list = getRoomRankFroDB(type, date);
            redisManager.hset(RedisKey.room_day_rank.getKey(), type.toString(), gson.toJson(list));
        } else {
            list = gson.fromJson(result, new TypeToken<List<RankActVo>>() {
            }.getType());
        }
        return list;
    }

    /**
     * 获取分类日排行信息--从数据库中读取
     *
     * @param tagId 标签ID
     * @param date  时间
     * @return
     */
    public List<RankActVo> getRoomRankFroDB(Integer tagId, String date) {
        RoomRankQuery query = new RoomRankQuery();
        query.setTagId(tagId);
        query.setDate(date);
        query.setTotal(20);
        List<RankActDO> list = giftSendRecordDao.roomRank(query);
        RankActVo vo;
        List<RankActVo> result = Lists.newArrayList();
        for (RankActDO dto : list) {
            vo = new RankActVo();
            BeanUtils.copyProperties(dto, vo);
            vo.setExperLevel(levelManager.getUserExperienceLevelSeq(dto.getUid()));
            vo.setCharmLevel(levelManager.getUserCharmLevelSeq(dto.getUid()));
            result.add(vo);
        }
        return result;
    }

    /**
     * 刷新房间分类日排行
     */
    @Override
    public void refreshRoomRank() {
        String date = DateFormatUtils.YYYY_MM_DD.date2Str(new Date());
        List<RoomTagDTO> list = roomTagManager.getSearchTags();
        List<RankActVo> vos;
        for (RoomTagDTO dto : list) {
            vos = getRoomRankFroDB(dto.getId(), date);
            redisManager.hset(RedisKey.room_day_rank.getKey(), dto.getId().toString(), gson.toJson(vos));
        }
    }

    @Override
    public ActivityHtmlVO getActivityInfo(String activityId) {
        ActivityHtmlVO activityHtmlVO = new ActivityHtmlVO();
        ActivityHtmlDTO activityHtmlDTO = activityHtmlDAO.queryActivity(activityId);
        BeanUtils.copyProperties(activityHtmlDTO, activityHtmlVO);
        return activityHtmlVO;
    }
}
